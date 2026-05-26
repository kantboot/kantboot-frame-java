package com.kantboot.functional.pay.exception;

import com.kantboot.util.rest.exception.BaseException;

/**
 * 支付订单异常
 */
public class FunctionalPayOrderException {

    /**
     * 支付订单未找到
     * payOrderNotFound
     */
    public static final BaseException PAY_ORDER_NOT_FOUND = BaseException.of("payOrderNotFound", "The payment order was not found");

    /**
     * 支付订单不是未支付状态
     * payOrderNotUnpaid
     */
    public static final BaseException PAY_ORDER_NOT_UNPAID = BaseException.of("payOrderNotUnpaid", "The payment order is not in the unpaid state");

    /**
     * 订单异常
     */
    public static final BaseException PAY_ORDER_EXCEPTION = BaseException.of("payOrderException", "Payment order exception");

    /**
     * 订单正在进行其它操作
     */
    public static final BaseException PAY_ORDER_OPERATING = BaseException.of("payOrderOperating", "The payment order is being operated");

    /**
     * 该订单已退款
     */
    public static final BaseException PAY_ORDER_REFUNDED = BaseException.of("payOrderRefunded", "The payment order has been refunded");

    /**
     * 该订单正在退款中
     */
    public static final BaseException PAY_ORDER_REFUNDING = BaseException.of("payOrderRefunding", "The payment order is being refunded");

    /**
     * 该订单并未支付完成
     */
    public static final BaseException PAY_ORDER_NOT_PAID = BaseException.of("payOrderNotPaid", "The payment order has not been paid");

    /**
     * 等待退款确认
     */
    public static final BaseException PAY_ORDER_WAIT_REFUND_CHECK = BaseException.of("payOrderWaitRefundCheck", "Waiting for refund confirmation");

    /**
     * 转账订单不存在
     */
    public static final BaseException PAY_TRANSFER_ORDER_NOT_FOUND = BaseException.of("payTransferOrderNotFound", "The pay transfer order was not found");

    /**
     * 转账订单不是未开始状态
     */
    public static final BaseException PAY_TRANSFER_ORDER_NOT_NOT_STARTED = BaseException.of("payTransferOrderNotNotStarted", "The pay transfer order is not in the not started state");

    /**
     * 订单不是PAY_TRANSFER_ORDER_NOT_PENDING
     */
    public static final BaseException PAY_TRANSFER_ORDER_NOT_PENDING = BaseException.of("payTransferOrderNotPending", "The pay transfer order is not in the pending state");


}