package com.kantboot.thirdparty.wechat.pay.util;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PayWechatPayParam implements Serializable {


    /**
     * 服务商应用ID
     */
    @JSONField(name = "appid")
    private String appid;

    /**
     * 服务商户号
     */
    @JSONField(name = "mchid")
    private String mchid;

    private String attach;


    /**
     * 商户订单号
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;

    /**
     * 交易说明
     */
    @JSONField(name = "description")
    private String description;

    /**
     * 通知地址
     */
    @JSONField(name = "notify_url")
    private String notifyUrl;

    /**
     * 总金额
     */
    @JSONField(name = "amountTotal")
    private BigDecimal amountTotal;

    /**
     * 币种
     */
    @JSONField(name = "amountCurrency")
    private String amountCurrency;

    /**
     * 用户标识
     */
    @JSONField(name = "payerOpenid")
    private String payerOpenid;


    public WechatPayParam getWechatPayParam() {
        WechatPayParam wechatPayParam = new WechatPayParam();
        wechatPayParam.setAppid(appid);
        wechatPayParam.setMchid(mchid);
        wechatPayParam.setAttach(attach);
        wechatPayParam.setOutTradeNo(outTradeNo);
        wechatPayParam.setDescription(description);
        wechatPayParam.setNotifyUrl(notifyUrl);

        // 金额
        WechatPayParam.Amount amount = new WechatPayParam.Amount();
        amount.setTotal(amountTotal.multiply(new BigDecimal(100)).intValue());
        amount.setCurrency(amountCurrency);
        wechatPayParam.setAmount(amount);

        // 支付者
        WechatPayParam.Payer payer = new WechatPayParam.Payer();
        payer.setOpenid(payerOpenid);
        wechatPayParam.setPayer(payer);
        return wechatPayParam;
    }

    @SneakyThrows
    public WechatPayResult createResult(String pemStr, String serialNo) {
        return getWechatPayParam().createResult(pemStr, serialNo);
    }

}
