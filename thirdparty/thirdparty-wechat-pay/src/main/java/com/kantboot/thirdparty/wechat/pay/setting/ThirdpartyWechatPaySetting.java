package com.kantboot.thirdparty.wechat.pay.setting;

import cn.hutool.core.bean.BeanUtil;
import com.kantboot.thirdparty.wechat.pay.util.WechatPayPlatformParam;
import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.util.setting.annotation.Setting;
import com.kantboot.util.setting.annotation.SettingGroup;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 懒汉模式
 */
@Data
@Lazy
@Component
@SettingGroup(name = "微信支付设置", description = "微信支付设置", code = "apiWechatPay")
public class ThirdpartyWechatPaySetting implements Serializable {

    /**
     * 微信支付商户号
     */
    @Setting(code = "mchId")
    private String mchId;

    /**
     * 商户密钥
     */
    @Setting(code = "mchKey")
    private String mchKey;

    /**
     * 支付证书序列号
     * https://pay.weixin.qq.com/index.php/core/cert/api_cert#/api-cert-manage
     */
    @Setting(code = "payCertSerialNo")
    private String payCertSerialNo;

    /**
     * 支付通知地址
     */
    @Setting(code = "payNotifyURL")
    private String payNotifyURL;

    /**
     * 转账通知地址
     */
    @Setting(code = "transferNotifyURL")
    private String transferNotifyURL;

    /**
     * 支付私钥
     */
    @Setting(code = "payPrivateKey")
    private String payPrivateKey;

    @Setting(code = "payPublicKey")
    private String payPublicKey;

    @Setting(code = "payPublicKeyId")
    private String payPublicKeyId;

    @Resource
    private ISysSettingService settingService;

    public WechatPayPlatformParam getWechatPayPlatformParam() {
        HashMap<String, String> wechat = settingService.getMapByGroupCode("apiWechatPay");
        return BeanUtil.copyProperties(wechat, WechatPayPlatformParam.class);
    }

    public WechatPayPlatformParam getWechatPayPlatformParamApplet(){
        WechatPayPlatformParam wechatPayPlatformParam = getWechatPayPlatformParam();
        wechatPayPlatformParam.setAppId(settingService.getValue("apiApiWechatApplet.appId"));
        wechatPayPlatformParam.setAppSecret(settingService.getValue("apiApiWechatApplet.appSecret"));
        return wechatPayPlatformParam;
    }

    /**
     * 获取支付私钥
     */
    public String getPayPrivateKey() {
        String value = settingService.getValueNoThrow("apiWechatPay.payPrivateKey");
//        if (value == null) {
//            throw BaseException.of("payCertKeyIsNull", "支付证书密钥为空","zh_CN");
//        }
//        // 去除证书头尾
//        value = value.replace("-----BEGIN PRIVATE KEY-----", "")
//                .replace("-----END PRIVATE KEY-----", "");
//        value=value.trim();
        return value;
    }

    /**
     * 获取支付公钥
     */
    public String getPayPublicKey() {
        String value = settingService.getValueNoThrow("apiWechatPay.payPublicKey");
//        if (value == null) {
//            throw BaseException.of("payCertKeyIsNull", "支付证书密钥为空","zh_CN");
//        }
//        // 去除证书头尾
//        value = value.replace("-----BEGIN PUBLIC KEY-----", "")
//                .replace("-----END PUBLIC KEY-----", "");
//        value=value.trim();
        return value;
    }

}
