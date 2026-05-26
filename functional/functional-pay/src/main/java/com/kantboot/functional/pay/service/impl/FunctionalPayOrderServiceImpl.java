package com.kantboot.functional.pay.service.impl;

import com.kantboot.functional.pay.domain.dto.PayCompleteDTO;
import com.kantboot.functional.pay.consts.PayOrderStatusCodeConsts;
import com.kantboot.functional.pay.dao.repository.FunctionalPayOrderRepository;
import com.kantboot.functional.pay.domain.dto.PayOrderGenerateDTO;
import com.kantboot.functional.pay.domain.dto.PayRefundDTO;
import com.kantboot.functional.pay.domain.dto.PayRefundFailDTO;
import com.kantboot.functional.pay.domain.entity.FunctionalPayOrder;
import com.kantboot.functional.pay.exception.FunctionalPayOrderException;
import com.kantboot.functional.pay.service.IFunctionalPayOrderLogService;
import com.kantboot.functional.pay.service.IFunctionalPayOrderService;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.util.event.emit.EventEmit;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FunctionalPayOrderServiceImpl
    implements IFunctionalPayOrderService {

    @Resource
    private FunctionalPayOrderRepository repository;

    @Resource
    private IFunctionalPayOrderLogService logService;

    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private EventEmit eventEmit;


    @Override
    public FunctionalPayOrder generate(PayOrderGenerateDTO dto) {
        // 生成支付订单逻辑
        FunctionalPayOrder order = new FunctionalPayOrder();
        // 设置用户账号ID
        order.setUserAccountId(dto.getUserAccountId());
        // 设置订单金额
        order.setAmount(dto.getAmount());
        // 设置业务编码
        order.setBusinessCode(dto.getTransferBusinessCode());
        // 设置订单描述
        order.setDescription(dto.getDescription());
        // 设置货币
        order.setCurrency(dto.getCurrency());
        // 设置订单状态为“未支付”
        order.setStatusCode(PayOrderStatusCodeConsts.UNPAID);

        // 保存支付订单到数据库
        FunctionalPayOrder save = repository.save(order);
        // 添加进订单日志
        logService.addLog(save);
        return save;
    }

    @Override
    public FunctionalPayOrder generateSelf(PayOrderGenerateDTO dto) {
        Long selfId = userAccountService.getSelfId();
        dto.setUserAccountId(selfId);
        return generate(dto);
    }

    @Override
    public FunctionalPayOrder getById(Long id) {
        FunctionalPayOrder functionalPayOrder = repository.findById(id).orElse(null);
        if(functionalPayOrder == null) {
            // 提示点个单号不存在
            throw FunctionalPayOrderException.PAY_ORDER_NOT_UNPAID;
        }
        return functionalPayOrder;
    }

    @Override
    public FunctionalPayOrder getUnpaidById(Long id) {
        FunctionalPayOrder functionalPayOrder = getById(id);
        if(functionalPayOrder==null||!PayOrderStatusCodeConsts.UNPAID.equals(functionalPayOrder.getStatusCode())) {
            // 提示单号不存在
            throw FunctionalPayOrderException.PAY_ORDER_NOT_UNPAID;
        }
        return functionalPayOrder;
    }

    @Override
    public FunctionalPayOrder updatePayMethodCodeById(Long id, String payMethodCode) {
        FunctionalPayOrder functionalPayOrder = getById(id);
        functionalPayOrder.setPayMethodCode(payMethodCode);
        return repository.save(functionalPayOrder);
    }

    @Override
    public void payComplete(PayCompleteDTO dto) {
        Long payOrderId = dto.getPayOrderId();
        String payMethodCode = dto.getPayMethodCode();
        String payMethodAdditionalInfo = dto.getPayMethodAdditionalInfo();
        BigDecimal fee = dto.getFee();

        FunctionalPayOrder functionalPayOrder = repository.findById(payOrderId)
                .orElseThrow(() -> FunctionalPayOrderException.PAY_ORDER_NOT_FOUND);

        if(PayOrderStatusCodeConsts.PAID.equals(functionalPayOrder.getStatusCode())){
            // 提示订单状态为已支付，提示订单异常
            // TODO 订单异常 要增加处理方式
//            throw FunctionalPayOrderException.PAY_ORDER_EXCEPTION;
            return;
        }

        // 设置订单状态为已支付
        functionalPayOrder.setStatusCode(PayOrderStatusCodeConsts.PAID);
        // 设置订单状态的支付方式
        functionalPayOrder.setPayMethodCode(payMethodCode);
        // 设置支付方式的额外信息
        functionalPayOrder.setPayMethodAdditionalInfo(payMethodAdditionalInfo);
        // 设置支付汇率
        functionalPayOrder.setFee(fee);
        // 设置订单实付金额
        functionalPayOrder.setPaidAmount(functionalPayOrder.getAmount().add(fee));

        // 保存支付订单到数据库
        repository.save(functionalPayOrder);
        // 添加支付订单日志
        logService.addLog(functionalPayOrder);

        // 通知对应的业务系统
        eventEmit.to("FunctionalPayOrder:paid:"+functionalPayOrder.getBusinessCode(),payOrderId);
    }



    @Override
    public FunctionalPayOrder update(FunctionalPayOrder functionalPayOrder) {
        return repository.save(functionalPayOrder);
    }

    @Override
    public List<FunctionalPayOrder> getSelfOfPaid() {
        Long selfId = userAccountService.getSelfId();
        return repository.findByUserAccountIdAndStatusCode(selfId,PayOrderStatusCodeConsts.PAID);
    }

    /**
     * 发起退款申请：写入退款信息 -> refund_checking -> 通知业务做退款校验/确认
     */
    @Override
    @Transactional
    public void startRefund(PayRefundDTO dto) {
        if (dto == null || dto.getPayOrderId() == null) {
            throw new IllegalArgumentException("payOrderId is required");
        }

        FunctionalPayOrder order = repository.findByIdForUpdate(dto.getPayOrderId())
                .orElseThrow(() -> FunctionalPayOrderException.PAY_ORDER_NOT_FOUND);

        // 幂等：如果已经进入退款流程或已退款，直接返回
        if (PayOrderStatusCodeConsts.REFUND_CHECKING.equals(order.getStatusCode())
                || PayOrderStatusCodeConsts.REFUNDING.equals(order.getStatusCode())
                || PayOrderStatusCodeConsts.REFUNDED.equals(order.getStatusCode())) {
            return;
        }

        // 只有已支付订单才能退款
        if (!PayOrderStatusCodeConsts.PAID.equals(order.getStatusCode())) {
            throw new IllegalStateException("Order status not refundable: " + order.getStatusCode());
        }

        BigDecimal paidAmount = order.getPaidAmount() != null ? order.getPaidAmount() : order.getAmount();
        if (paidAmount == null) {
            throw new IllegalStateException("paidAmount/amount is null");
        }

        // 退款金额：dto 有就用 dto，没有就默认全额退（按实付）
        BigDecimal refundAmount = dto.getAmount() != null ? dto.getAmount() : paidAmount;
        if (refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("refundAmount must be > 0");
        }
        if (refundAmount.compareTo(paidAmount) > 0) {
            throw new IllegalArgumentException("refundAmount exceeds paidAmount");
        }

        // 写入退款申请信息
        order.setRefundReasonCode(dto.getReasonCode());
        order.setRefundReasonDescription(dto.getReasonDescription());
        order.setRefundAmount(refundAmount);

        // 是否全额退款
        order.setIsAllRefund(refundAmount.compareTo(paidAmount) == 0);

        // 计算实际退款金额：是否扣手续费（按你的字段语义）
        BigDecimal fee = order.getFee() == null ? BigDecimal.ZERO : order.getFee();
        BigDecimal actualRefundAmount = refundAmount;

        if (Boolean.TRUE.equals(order.getIsSubtractFeeWhenRefund())) {
            actualRefundAmount = refundAmount.subtract(fee);
            if (actualRefundAmount.compareTo(BigDecimal.ZERO) < 0) {
                actualRefundAmount = BigDecimal.ZERO;
            }
        }

        order.setActualRefundAmount(actualRefundAmount);

        // 进入退款校验阶段（业务系统确认）
        order.setStatusCode(PayOrderStatusCodeConsts.REFUND_CHECKING);

        repository.save(order);
        logService.addLog(order);

        // 通知业务系统：请校验是否允许退款（库存/发票/权益回收等）
        // 业务监听后，校验通过就调用 refundCheckingPass(payOrderId, additionalInfo)
        eventEmit.to("FunctionalPayOrder:refundChecking:" + order.getBusinessCode(), order.getId());
        refundCheckingPass(order.getId());
    }

    /**
     * 业务校验通过：refund_checking -> refunding，并触发三方退款
     * （由你的业务监听器在校验通过后调用）
     */
    @Override
    @Transactional
    public void refundCheckingPass(Long payOrderId) {
        FunctionalPayOrder order = repository.findByIdForUpdate(payOrderId)
                .orElseThrow(() -> FunctionalPayOrderException.PAY_ORDER_NOT_FOUND);

        // 幂等：已经在 refunding/refunded 就不重复
        if (PayOrderStatusCodeConsts.REFUNDING.equals(order.getStatusCode())
                || PayOrderStatusCodeConsts.REFUNDED.equals(order.getStatusCode())) {
            return;
        }

        // 必须处于 refund_checking
        if (!PayOrderStatusCodeConsts.REFUND_CHECKING.equals(order.getStatusCode())) {
            throw new IllegalStateException("Order status not in refund_checking: " + order.getStatusCode());
        }

        order.setStatusCode(PayOrderStatusCodeConsts.REFUNDING);

        repository.save(order);
        logService.addLog(order);

        // 通知三方支付适配层：开始退款（微信/PayPal等）
        // 三方模块拿 payOrderId 去调用退款接口，成功后回调 refundComplete
        eventEmit.to("FunctionalPayOrder:refundStart:" + order.getPayMethodCode(), order.getId());
    }

    /**
     * 三方退款成功回调：refunding -> refunded，写入最终信息
     */
    @Override
    @Transactional
    public void refundComplete(PayRefundDTO dto) {
        if (dto == null || dto.getPayOrderId() == null) {
            throw new IllegalArgumentException("payOrderId is required");
        }

        FunctionalPayOrder order = repository.findById(dto.getPayOrderId())
                .orElseThrow(() -> FunctionalPayOrderException.PAY_ORDER_NOT_FOUND);

        // 幂等：已经 refunded 直接返回
        if (PayOrderStatusCodeConsts.REFUNDED.equals(order.getStatusCode())) {
            return;
        }

        // 必须处于 refunding
        if (!PayOrderStatusCodeConsts.REFUNDING.equals(order.getStatusCode())) {
            throw new IllegalStateException("Order status not in refunding: " + order.getStatusCode());
        }

        // 回写（允许三方回调覆盖/补充）
        if (dto.getReasonCode() != null) order.setRefundReasonCode(dto.getReasonCode());
        if (dto.getReasonDescription() != null) order.setRefundReasonDescription(dto.getReasonDescription());
        if (dto.getAmount() != null) order.setRefundAmount(dto.getAmount());

        // 附加信息当成三方回执
         order.setRefundAdditionalInfo(dto.getAdditionalInfo());

        // 最终状态
        order.setStatusCode(PayOrderStatusCodeConsts.REFUNDED);

        repository.save(order);
        logService.addLog(order);

        System.err.println(order.getBusinessCode()+"==buss");
        // 通知业务系统：退款完成（回收权益/关单/发短信等）
        eventEmit.to("FunctionalPayOrder:refunded:" + order.getBusinessCode(), order.getId());
    }


    /**
     * 退款失败（可选但强烈建议加上）：进入 error 并记录原因
     */
    @Override
    @Transactional
    public void refundFail(PayRefundFailDTO dto) {
        Long payOrderId = dto.getPayOrderId();
        String failReasonCode = dto.getReasonCode();
        String failReasonDesc = dto.getReasonDescription();
        FunctionalPayOrder order = repository.findByIdForUpdate(payOrderId)
                .orElseThrow(() -> FunctionalPayOrderException.PAY_ORDER_NOT_FOUND);

        // 如果都 refunded 了就别改回失败
        if (PayOrderStatusCodeConsts.REFUNDED.equals(order.getStatusCode())) {
            return;
        }

        order.setStatusCode(PayOrderStatusCodeConsts.ERROR);
        order.setRefundReasonCode(failReasonCode);
        order.setRefundReasonDescription(failReasonDesc);

        repository.save(order);
        logService.addLog(order);

        eventEmit.to("FunctionalPayOrder:refundFailed:" + order.getBusinessCode(), order.getId());
    }
}
