package com.kantboot.functional.pay.consts;

/**
 * 转账订单相关常量
 */
public class PayTransferOrderStatusCodeConst {

    /**
     * 转账订单状态-未开始
     */
    public static final String NOT_STARTED = "not_started";

    /**
     * 转账订单状态-未完成
     */
    public static final String PENDING = "pending";

    /**
     * 转账订单状态-已完成
     */
    public static final String COMPLETED = "completed";

    /**
     * 转账订单状态-取消中
     */
    public static final String CANCELING = "canceling";

    /**
     * 转账订单状态-已取消
     */
    public static final String CANCELED = "canceled";

    /**
     * 转账订单失败-失败
     */
    public static final String FAILED = "failed";

    /**
     * 转账订单状态-异常
     */
    public static final String ERROR = "error";

}
