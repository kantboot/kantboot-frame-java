package com.kantboot.thirdparty.wechat.pay.exception;


import com.kantboot.util.rest.exception.BaseException;

/**
 * 微信支付参数错误
 */
public class WechatPayParamError extends BaseException {

    public WechatPayParamError(String message) {
//        super("wechatPayParamError", message);
        super.setStateCode("wechatPayParamError");
        super.setMessage(message);
        super.setLanguageCode("zh_CN");
    }

}
