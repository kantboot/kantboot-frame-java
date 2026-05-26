package com.kantboot.thirdparty.wechat.pay.util;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 商户对resource对象进行解密后，得到的资源对象示例
 * @author 方某方
 */
@Data
public class PayNotifyResult implements Serializable {

    /**
     * 返回状态码
     * 错误码，SUCCESS为清算机构接收成功，其他错误码为失败。
     * 示例值：FAIL
     */
    @JSONField(name="code")
    private String code;

    /**
     * 返回信息
     * 返回信息，如非空，为错误原因。
     * 示例值：失败
     */
    @JSONField(name="message")
    private String message;

    /**
     * 微信支付订单号
     * 微信支付系统生成的订单号。
     * 示例值：1217752501201407033233368018
     */
    @JSONField(name="transaction_id")
    private String transactionId;

    /**
     * 订单金额
     * 订单金额信息。
     */
    @JSONField(name="amount")
    private Amount amount;

    @Data
    public static class Amount{

        /**
         * 用户支付金额
         * 用户支付金额
         * 示例值：100
         */
        @JSONField(name="payer_total")
        private Integer payerTotal;

        /**
         * 总金额
         * 订单总金额，单位为分。
         * 示例值：100
         */
        @JSONField(name="total")
        private Integer total;

        /**
         * 货币类型
         * CNY：人民币，境内商户号仅支持人民币。
         * 示例值：CNY
         */
        @JSONField(name="currency")
        private String currency;

        /**
         * 用户支付币种
         * 用户支付币种
         * 示例值：CNY
         */
        @JSONField(name="payer_currency")
        private String payerCurrency;
    }

    /**
     * 商户号
     * 直连商户的商户号，由微信支付生成并下发。
     * 示例值：1230000109
     */
    @JSONField(name="mchid")
    private String mchid;

    /**
     * 交易状态
     * 交易状态，枚举值：
     * SUCCESS：支付成功
     * REFUND：转入退款
     * NOTPAY：未支付
     * CLOSED：已关闭
     * REVOKED：已撤销（付款码支付）
     * USERPAYING：用户支付中（付款码支付）
     * PAYERROR：支付失败(其他原因，如银行返回失败)
     * 示例值：SUCCESS
     */
    @JSONField(name="trade_state")
    private String tradeState;

    /**
     * 付款银行
     * 银行类型，采用字符串类型的银行标识。
     * 银行表示请参考《银行类型对照表》
     * 银行类型对照表 https://pay.weixin.qq.com/wiki/doc/apiv3/terms_definition/chapter1_1_3.shtml#part-6
     * 示例值：CMC
     */
    @JSONField(name="bank_type")
    private String bankType;

    /**
     * 优惠功能
     * 优惠功能，享受优惠时返回该字段。
     */
    @JSONField(name="promotion_detail")
    private PromotionDetail[] promotionDetail;

    @Data
    public static class PromotionDetail{
        /**
         * 优惠券面额
         * 优惠券面额
         * 示例值：100
         */
        @JSONField(name="amount")
        private Integer amount;

        /**
         * 微信出资
         * 微信出资，单位为分
         * 示例值：0
         */
        @JSONField(name="wechatpay_contribute")
        private Integer wechatpayContribute;

        /**
         * 券ID
         * 券ID
         * 示例值：109519
         */
        @JSONField(name="coupon_id")
        private String couponId;

        /**
         * 优惠范围
         * GLOBAL：全场代金券
         * SINGLE：单品优惠
         * 示例值：SINGLE
         */
        @JSONField(name="scope")
        private String scope;

        /**
         * 商户出资
         * 商户出资，单位为分
         * 示例值：0
         */
        @JSONField(name="merchant_contribute")
        private Integer merchantContribute;

        /**
         * 优惠名称
         * 优惠名称
         * 示例值：单品惠-6
         */
        @JSONField(name="name")
        private String name;

        /**
         * 其他出资
         * 其他出资，单位为分
         * 示例值：0
         */
        @JSONField(name="other_contribute")
        private Integer otherContribute;

        /**
         * 优惠币种
         * CNY：人民币，境内商户号仅支持人民币。
         * 示例值：CNY
         */
        @JSONField(name="currency")
        private String currency;

        /**
         * 活动ID
         * 活动ID
         * 示例值：931386
         */
        @JSONField(name="stock_id")
        private String stockId;

        /**
         * 单品列表
         * 单品列表信息
         */
        @JSONField(name="goods_detail")
        private GoodsDetail[] goodsDetail;

        @Data
        public static class GoodsDetail{
            @JSONField(name="goods_remark")
            private String goodsRemark;
            @JSONField(name="quantity")
            private Integer quantity;
            @JSONField(name="discount_amount")
            private Integer discountAmount;
            @JSONField(name="goods_id")
            private String goodsId;
            @JSONField(name="unit_price")
            private Integer unitPrice;
        }

    }

    @JSONField(name="success_time")
    private String successTime;

    @JSONField(name="payer")
    private Payer payer;

    @Data
    public static class Payer{
        @JSONField(name="openid")
        private String openid;
    }

    @JSONField(name="out_trade_no")
    private String outTradeNo;

    @JSONField(name="appid")
    private String appid;

    @JSONField(name="trade_state_desc")
    private String tradeStateDesc;

    @JSONField(name="trade_type")
    private String tradeType;

    @JSONField(name="attach")
    private String attach;

    @JSONField(name="scene_info")
    private SceneInfo sceneInfo;

    @Data
    public static class SceneInfo{
        @JSONField(name="device_id")
        private String deviceId;
    }



}
