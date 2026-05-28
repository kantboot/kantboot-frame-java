package com.kantboot.functional.pay.service;

import com.kantboot.functional.pay.domain.dto.PayCompleteDTO;
import com.kantboot.functional.pay.domain.dto.PayOrderGenerateDTO;
import com.kantboot.functional.pay.domain.dto.PayRefundDTO;
import com.kantboot.functional.pay.domain.dto.PayRefundFailDTO;
import com.kantboot.functional.pay.domain.entity.FunctionalPayOrder;

import java.util.List;

public interface IFunctionalPayOrderService {

    /**
     * 生成支付订单
     */
    FunctionalPayOrder generate(PayOrderGenerateDTO dto);

    /**
     * 生成自身支付订单
     */
    FunctionalPayOrder generateSelf(PayOrderGenerateDTO dto);

    /**
     * 根据ID获取订单
     */
    FunctionalPayOrder getById(Long id);

    /**
     * 获取未支付的订单
     */
    FunctionalPayOrder getUnpaidById(Long id);

    /**
     * 修改订单支付方式
     */
    FunctionalPayOrder updatePayMethodCodeById(Long id, String payMethodCode);

    /**
     * 对应的支付方式已支付
     */
    void payComplete(PayCompleteDTO dto);

    /**
     * 开始退款
     */
    void startRefund(PayRefundDTO dto);

    /**
     * 退款前检查通过
     */
    void refundCheckingPass(Long payOrderId);

    /**
     * 退款完成
     */
    void refundComplete(PayRefundDTO dto);

    /**
     * 退款失败
     */
    void refundFail(PayRefundFailDTO dto);

    /**
     * 修改订单
     */
    FunctionalPayOrder update(FunctionalPayOrder functionalPayOrder);

    List<FunctionalPayOrder> getSelfOfPaid();

}