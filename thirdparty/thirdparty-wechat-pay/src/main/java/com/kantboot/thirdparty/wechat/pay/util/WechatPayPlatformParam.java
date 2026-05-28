package com.kantboot.thirdparty.wechat.pay.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class WechatPayPlatformParam implements Serializable {

    /**
     * 小程序appid
     */
    private String appId;

    /**
     * 小程序appSecret
     */
    private String appSecret;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 商户密钥
     */
    private String mchKey;

    /**
     * 支付证书序列号
     */
    private String payCertSerialNo;

    /**
     * 支付通知地址
     */
    private String payNotifyURL;

    /**
     * 支付私钥
     */
    private String payPrivateKey;

}
