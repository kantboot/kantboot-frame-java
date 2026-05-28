package com.kantboot.thirdparty.wechat.pay.util;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * 商户订单号查询订单参数
 */
@Data
public class WechatOrderByOutTradeNoResult {

//    {"amount":{"currency":"CNY","payer_currency":"CNY","payer_total":10,"total":10},"appid":"wxe5df44af44e7b005","attach":"","bank_type":"OTHERS","mchid":"1607966646","out_trade_no":"495936000372741","payer":{"openid":"oTwcv4yZdnBODLO0uiGbwkxrmn_U"},"promotion_detail":[],"success_time":"2023-12-22T11:45:35+08:00","trade_state":"SUCCESS","trade_state_desc":"支付成功","trade_type":"JSAPI","transaction_id":"4200002112202312221467490410"}
//    {"attach":"","mchid":"1607966646"}

    public static class Amount {
        private String currency;
        private String payer_currency;
        private Integer payer_total;
        private Integer total;
    }

    public static class Payer {
        private String openid;
    }

    @JSONField(name = "amount")
    private Amount amount;

    @JSONField(name = "appid")
    private String appid;

    @JSONField(name = "attach")
    private String attach;

    @JSONField(name = "bank_type")
    private String bankType;

    @JSONField(name = "mchid")
    private String mchid;

    @JSONField(name = "out_trade_no")
    private String outTradeNo;

    @JSONField(name = "payer")
    private Payer payer;

    @JSONField(name = "promotion_detail")
    private Object promotionDetail;

    @JSONField(name = "success_time")
    private String successTime;

    @JSONField(name = "trade_state")
    private String tradeState;

    @JSONField(name = "trade_state_desc")
    private String tradeStateDesc;

    @JSONField(name = "trade_type")
    private String tradeType;

    @JSONField(name = "transaction_id")
    private String transactionId;


}
