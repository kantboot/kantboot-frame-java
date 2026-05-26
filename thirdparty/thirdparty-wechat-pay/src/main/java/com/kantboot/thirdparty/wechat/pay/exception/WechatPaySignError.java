package com.kantboot.thirdparty.wechat.pay.exception;


import com.kantboot.util.rest.exception.BaseException;

/**
 * 微信支付签名错误
 */
public class WechatPaySignError extends BaseException {

    public WechatPaySignError(String message) {
        super.setStateCode("wechatPaySignError");
        super.setMessage(message);
        super.setLanguageCode("zh_CN");
    }

}
