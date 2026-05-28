package com.kantboot.functional.pay.service.impl;

import com.kantboot.functional.pay.consts.PayTransferOrderStatusCodeConst;
import com.kantboot.functional.pay.dao.repository.FunctionalPayTransferOrderRepository;
import com.kantboot.functional.pay.domain.dto.PayOrderGenerateDTO;
import com.kantboot.functional.pay.domain.dto.PayTransferFailDTO;
import com.kantboot.functional.pay.domain.entity.FunctionalPayTransferOrder;
import com.kantboot.functional.pay.exception.FunctionalPayOrderException;
import com.kantboot.functional.pay.service.IFunctionalPayTransferOrderLogService;
import com.kantboot.functional.pay.service.IFunctionalPayTransferOrderService;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.util.cache.CacheUtil;
import com.kantboot.util.event.emit.EventEmit;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class FunctionalPayTransferOrderServiceImpl
        implements IFunctionalPayTransferOrderService {

    @Resource
    private FunctionalPayTransferOrderRepository repository;

    @Resource
    private IFunctionalPayTransferOrderLogService logService;

    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private EventEmit eventEmit;

    @Resource
    private CacheUtil cacheUtil;

    @Override
    public FunctionalPayTransferOrder getById(Long id) {
        FunctionalPayTransferOrder functionalPayTransferOrder = repository.findById(id).orElse(null);
        if (functionalPayTransferOrder == null) {
            throw FunctionalPayOrderException.PAY_TRANSFER_ORDER_NOT_FOUND;
        }
        return functionalPayTransferOrder;
    }

    @Override
    public FunctionalPayTransferOrder generate(PayOrderGenerateDTO dto) {
        // 生成转账订单逻辑
        FunctionalPayTransferOrder functionalPayTransferOrder = new FunctionalPayTransferOrder();
        // 设置用户账号ID
        functionalPayTransferOrder.setUserAccountId(dto.getUserAccountId());
        // 设置金额
        functionalPayTransferOrder.setAmount(dto.getAmount());
        // 设置转账业务编码
        functionalPayTransferOrder.setTransferBusinessCode(dto.getTransferBusinessCode());
        // 设置描述
        functionalPayTransferOrder.setDescription(dto.getDescription());
        // 设置币种
        functionalPayTransferOrder.setCurrency(dto.getCurrency());
        // 设置状态为待处理
        functionalPayTransferOrder.setStatusCode(PayTransferOrderStatusCodeConst.NOT_STARTED);
        // 保存转账订单到数据库
        FunctionalPayTransferOrder save = repository.save(functionalPayTransferOrder);
        // 转账创建中
        eventEmit.to("FunctionalPayTransferOrder:generated:"+dto.getTransferBusinessCode(),save);
        // 保存到日志
        logService.addLog(save);
        return save;
    }

    @Override
    public FunctionalPayTransferOrder generateSelf(PayOrderGenerateDTO dto) {
        Long selfId = userAccountService.getSelfId();
        dto.setUserAccountId(selfId);
        return generate(dto);
    }

    public FunctionalPayTransferOrder confirmTransferMethodCodeById(Long id, String transferMethodCode) {
        FunctionalPayTransferOrder functionalPayTransferOrder = repository.findById(id).orElse(null);
        if (functionalPayTransferOrder == null) {
            throw FunctionalPayOrderException.PAY_TRANSFER_ORDER_NOT_FOUND;
        }
        functionalPayTransferOrder.setTransferMethodCode(transferMethodCode);
        return repository.save(functionalPayTransferOrder);
    }

    @Override
    public FunctionalPayTransferOrder startTransfer(Long id) {
        String key = "FunctionalPayTransferOrder:startTransfer:"+id;
        if (cacheUtil.hasKey(key)) {
            throw FunctionalPayOrderException.PAY_TRANSFER_ORDER_NOT_NOT_STARTED;
        }

        cacheUtil.setEx(key, "1", 3, TimeUnit.MINUTES);

        FunctionalPayTransferOrder functionalPayTransferOrder = repository.findById(id).orElse(null);
        if (functionalPayTransferOrder == null) {
            // 提示订单不存在
            throw FunctionalPayOrderException.PAY_TRANSFER_ORDER_NOT_FOUND;
        }
        // 如过就是PENDING状态，则直接返回不做操作
        if(PayTransferOrderStatusCodeConst.PENDING.equals(functionalPayTransferOrder.getStatusCode())){
            return functionalPayTransferOrder;
        }
        // 如果状态不是未开始，则不进行任何操作
        if (!PayTransferOrderStatusCodeConst.NOT_STARTED.equals(functionalPayTransferOrder.getStatusCode())) {
            throw FunctionalPayOrderException.PAY_TRANSFER_ORDER_NOT_NOT_STARTED;
        }
        functionalPayTransferOrder.setStatusCode(PayTransferOrderStatusCodeConst.PENDING);
        FunctionalPayTransferOrder save = repository.save(functionalPayTransferOrder);
        // 创建转账订单
        eventEmit.to("FunctionalPayTransferOrder:start:"+save.getTransferBusinessCode(),save);
        // 添加到日志中
        logService.addLog(save);
        // 解锁
        cacheUtil.delete(key);
        return save;
    }

    @Override
    public FunctionalPayTransferOrder startTransferCancel(Long id) {
        // 加锁
//        String key = "FunctionalPayTransferOrder:startCancel:"+id;
//        if (cacheUtil.hasKey(key)) {
//            throw FunctionalPayOrderException.PAY_TRANSFER_ORDER_NOT_NOT_STARTED;
//        }
//        cacheUtil.setEx(key, "1", 3, TimeUnit.MINUTES);

        FunctionalPayTransferOrder functionalPayTransferOrder = repository.findById(id).orElse(null);
        if (functionalPayTransferOrder == null) {
            throw FunctionalPayOrderException.PAY_TRANSFER_ORDER_NOT_FOUND;
        }
        if(PayTransferOrderStatusCodeConst.CANCELING.equals(functionalPayTransferOrder.getStatusCode())){
            return functionalPayTransferOrder;
        }
        // 如果不是PENDING状态，则不进行任何操作
        if (!PayTransferOrderStatusCodeConst.PENDING.equals(functionalPayTransferOrder.getStatusCode())) {
            throw FunctionalPayOrderException.PAY_TRANSFER_ORDER_NOT_PENDING;
        }
        functionalPayTransferOrder.setStatusCode(PayTransferOrderStatusCodeConst.CANCELING);
        FunctionalPayTransferOrder save = repository.save(functionalPayTransferOrder);
        // 创建转账订单
        eventEmit.to("FunctionalPayTransferOrder:startCancel:"+save.getTransferBusinessCode(),save);
        // 添加到日志中
        logService.addLog(save);
        return save;
    }

    @Override
    public FunctionalPayTransferOrder transferCancelComplete(Long id) {
//        // 加锁
//        String key = "FunctionalPayTransferOrder:cancelComplete:"+id;
//        if (cacheUtil.hasKey(key)) {
//            throw FunctionalPayOrderException.PAY_TRANSFER_ORDER_NOT_PENDING;
//        }
//        cacheUtil.setEx(key, "1", 3, TimeUnit.MINUTES);

        FunctionalPayTransferOrder functionalPayTransferOrder = repository.findById(id).orElse(null);
        if (functionalPayTransferOrder == null) {
            throw FunctionalPayOrderException.PAY_TRANSFER_ORDER_NOT_FOUND;
        }
        // 如过就是CANCELED状态，则直接返回不做操作
        if(PayTransferOrderStatusCodeConst.CANCELED.equals(functionalPayTransferOrder.getStatusCode())){
            return functionalPayTransferOrder;
        }

        // 如果不是CANCELING状态，则不进行任何操作
        if (!PayTransferOrderStatusCodeConst.CANCELING.equals(functionalPayTransferOrder.getStatusCode())){
            throw FunctionalPayOrderException.PAY_TRANSFER_ORDER_NOT_PENDING;
        }

        functionalPayTransferOrder.setStatusCode(PayTransferOrderStatusCodeConst.CANCELED);
        FunctionalPayTransferOrder save = repository.save(functionalPayTransferOrder);
        // 创建转账订单
        eventEmit.to("FunctionalPayTransferOrder:cancel:"+save.getTransferBusinessCode(),save);
        // 添加到日志中
        logService.addLog(save);
        return save;
    }

    @Override
    public FunctionalPayTransferOrder transferComplete(Long id) {
        // 加锁
        String key = "FunctionalPayTransferOrder:complete:"+id;
        if (cacheUtil.hasKey(key)) {
            throw FunctionalPayOrderException.PAY_TRANSFER_ORDER_NOT_PENDING;
        }
        cacheUtil.setEx(key, "1", 3, TimeUnit.MINUTES);

        FunctionalPayTransferOrder functionalPayTransferOrder = repository.findById(id).orElse(null);
        if (functionalPayTransferOrder == null) {
            throw FunctionalPayOrderException.PAY_TRANSFER_ORDER_NOT_FOUND;
        }
        functionalPayTransferOrder.setStatusCode(PayTransferOrderStatusCodeConst.COMPLETED);
        FunctionalPayTransferOrder save = repository.save(functionalPayTransferOrder);
        // 创建转账订单
        eventEmit.to("FunctionalPayTransferOrder:complete:"+save.getTransferBusinessCode(),save);
        // 添加到日志中
        logService.addLog(save);
        return save;
    }


    @Override
    public FunctionalPayTransferOrder transferFail(PayTransferFailDTO dto) {
        FunctionalPayTransferOrder functionalPayTransferOrder = repository.findById(dto.getTransferOrderId()).orElse(null);
        if (functionalPayTransferOrder == null) {
            throw FunctionalPayOrderException.PAY_TRANSFER_ORDER_NOT_FOUND;
        }
        functionalPayTransferOrder.setId(dto.getTransferOrderId());
        functionalPayTransferOrder.setTransferMethodCode(dto.getTransferMethodCode());
        functionalPayTransferOrder.setStatusCode(PayTransferOrderStatusCodeConst.FAILED);
        functionalPayTransferOrder.setFailReasonCode(functionalPayTransferOrder.getFailReasonCode());
        functionalPayTransferOrder.setFailReason(functionalPayTransferOrder.getFailReason());
        FunctionalPayTransferOrder save = repository.save(functionalPayTransferOrder);
        // 创建转账订单
        eventEmit.to("FunctionalPayTransferOrder:failed:"+save.getTransferBusinessCode(),save);
        // 添加到日志中
        logService.addLog(save);
        return save;
    }

    @Override
    public List<FunctionalPayTransferOrder> getByUserAccountIdAndStatusCode(Long userAccountId, String statusCode) {
        return repository.findByUserAccountIdAndStatusCodeOrderByIdDesc(userAccountId, statusCode);
    }

    @Override
    public List<FunctionalPayTransferOrder> getSelfByStatusCode(String statusCode) {
        Long selfId = userAccountService.getSelfId();
        return getByUserAccountIdAndStatusCode(selfId,statusCode);
    }
}
