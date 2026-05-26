package com.kantboot.functional.pay.consts;

/**
 * 支付订单状态码常量
 */
public class PayOrderStatusCodeConsts {

    /**
     * 订单状态：未支付 unpaid
     */
    public static final String UNPAID = "unpaid";

    /**
     * 订单状态：已支付 paid
     */
    public static final String PAID = "paid";

    /**
     * 订单状态：等待退款确认 refund_checking
     */
    public static final String REFUND_CHECKING = "refund_checking";

    /**
     * 订单状态：退款中 refunding
     */
    public static final String REFUNDING = "refunding";

    /**
     * 订单状态：已退款 refunded
     */
    public static final String REFUNDED = "refunded";

    /**
     * 订单状态：已取消 canceled
     */
    public static final String CANCELED = "canceled";

    /**
     * 订单状态：异常 error
     */
    public static final String ERROR = "error";

}