package com.kantboot.thirdparty.wechat.pay.util;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * 微信退款结果
 */
@Data
public class WechatRefundsResult {



    /**
     * ORIGINAL: 原路退款
     */
    public static final String CHANNEL_ORIGINAL = "ORIGINAL";

    /**
     * BALANCE: 退回到余额
     */
    public static final String CHANNEL_BALANCE = "BALANCE";

    /**
     * OTHER_BALANCE: 原账户异常退到其他余额账户
     */
    public static final String CHANNEL_OTHER_BALANCE = "OTHER_BALANCE";

    /**
     * OTHER_BANKCARD: 原银行卡异常退到其他银行卡
     */
    public static final String CHANNEL_OTHER_BANKCARD = "OTHER_BANKCARD";

    /**
     * SUCCESS: 退款成功
     */
    public static final String STATUS_SUCCESS = "SUCCESS";

    /**
     * CLOSED: 退款关闭
     */
    public static final String STATUS_CLOSED = "CLOSED";

    /**
     * PROCESSING: 退款处理中
     */
    public static final String STATUS_PROCESSING = "PROCESSING";

    /**
     * ABNORMAL: 退款异常
     */
    public static final String STATUS_ABNORMAL = "ABNORMAL";

    /**
     * UNSETTLED: 未结算资金
     */
    public static final String FUNDS_ACCOUNT_UNSETTLED = "UNSETTLED";

    /**
     * AVAILABLE: 可用余额
     */
    public static final String FUNDS_ACCOUNT_AVAILABLE = "AVAILABLE";

    /**
     * UNAVAILABLE: 不可用余额
     */
    public static final String FUNDS_ACCOUNT_UNAVAILABLE = "UNAVAILABLE";

    /**
     * OPERATION: 运营户
     */
    public static final String FUNDS_ACCOUNT_OPERATION = "OPERATION";

    /**
     * BASIC: 基本账户（含可用余额和不可用余额）
     */
    public static final String FUNDS_ACCOUNT_BASIC = "BASIC";

    /**
     * ECNY_BASIC: 数字人民币基本账户
     */
    public static final String FUNDS_ACCOUNT_ECNY_BASIC = "ECNY_BASIC";

    /**
     * 【微信支付退款号】 微信支付退款号
     */
    @JSONField(name = "refund_id")
    private String refundId;

    /**
     * 【商户退款号】 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     */
    @JSONField(name = "out_refund_no")
    private String outRefundNo;


    /**
     * 【微信订单号】 微信订单号
     */
    @JSONField(name = "transaction_id")
    private String transactionId;

    /**
     * 【商户订单号】 商户系统内部的订单号,商户系统内部唯一,只能是数字,大小写字母_-|*@ ，同一商户号下唯一。
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;


    /**
     * 【退款渠道】 退款渠道
     *     ORIGINAL: 原路退款
     *     BALANCE: 退回到余额
     *     OTHER_BALANCE: 原账户异常退到其他余额账户
     *     OTHER_BANKCARD: 原银行卡异常退到其他银行卡
     */
    @JSONField(name = "channel")
    private String channel;

    @Data
    public static class Amount {
        /**
         * 【订单金额】 订单总金额，单位为分
         */
        @JSONField(name = "total")
        private Integer total;

        /**
         * 【退款金额】 退款标价金额，单位为分，可以做
         * 部分退款
         */
        @JSONField(name = "refund")
        private Integer refund;

     }

    /**
     * 【退款入账账户】 取当前退款单的退款入账方，有以下几种情况：
     * 1）退回银行卡：{银行名称}{卡类型}{卡尾号}
     * 2）退回支付用户零钱:支付用户零钱
     * 3）退还商户:商户基本账户商户结算银行账户
     * 4）退回支付用户零钱通:支付用户零钱通
     */
    @JSONField(name = "user_received_account")
    private String userReceivedAccount;


    /**
     * 【退款成功时间】 退款成功时间，退款状态status为SUCCESS（退款成功）时，返回该字段。
     * 遵循rfc3339标准格式，格式为YYYY-MM-DDTHH:mm:ss+TIMEZONE，
     * YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，
     * HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，
     * 领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，
     * 北京时间2015年5月20日13点29分35秒。
     */
    @JSONField(name = "success_time")
    private String successTime;

    /**
     * 【退款创建时间】 退款受理时间，遵循rfc3339标准格式，格式为YYYY-MM-DDTHH:mm:ss+TIMEZONE，
     * YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，
     * HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，
     * 领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，
     * 北京时间2015年5月20日13点29分35秒。
     */
    @JSONField(name = "create_time")
    private String createTime;

    /**
     * 【退款状态】 退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，
     * 可前往商户平台（pay.weixin.qq.com）-交易中心，手动处理此笔退款。
     * 可选取值：
     * SUCCESS: 退款成功
     * CLOSED: 退款关闭
     * PROCESSING: 退款处理中
     * ABNORMAL: 退款异常
     */
    @JSONField(name = "status")
    private String status;

    /**
     * 【资金账户】 退款所使用资金对应的资金账户类型
     * 可选取值：
     * UNSETTLED: 未结算资金
     * AVAILABLE: 可用余额
     * UNAVAILABLE: 不可用余额
     * OPERATION: 运营户
     * BASIC: 基本账户（含可用余额和不可用余额）
     * ECNY_BASIC: 数字人民币基本账户
     */
    @JSONField(name = "funds_account")
    private String fundsAccount;


}
