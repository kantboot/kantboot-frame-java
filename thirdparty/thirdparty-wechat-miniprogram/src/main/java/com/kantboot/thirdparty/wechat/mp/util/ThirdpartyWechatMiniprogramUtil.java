package com.kantboot.thirdparty.wechat.mp.util;


import com.alibaba.fastjson2.JSONObject;
import com.kantboot.thirdparty.wechat.mp.util.domain.ThirdpartyWechatMiniprogramPhone;
import com.kantboot.thirdparty.wechat.util.ThirdpartyWechatUtil;
import com.kantboot.thirdparty.wechat.util.domain.ThirdpartyWechatSession;
import com.kantboot.thirdparty.wechat.util.domain.ThirdpartyWechatToken;
import com.kantboot.util.http.HttpSendUtil;
import com.kantboot.util.http.domain.config.HttpSendConfig;

import java.util.Map;

/**
 * 微信小程序工具类
 */
public class ThirdpartyWechatMiniprogramUtil {

    public static ThirdpartyWechatMiniprogramPhone getPhoneNumber(String accessToken,String code) {
        String apiUrl = "https://api.weixin.qq.com/wxa/business/getuserphonenumber" +
                "?access_token=" + accessToken;
        String send = HttpSendUtil.send(new HttpSendConfig()
                .setUrl(apiUrl)
                .setMethod("POST")
                .setBody(Map.of("code", code))
        );
        JSONObject jsonObject = JSONObject.parseObject(send);
        if (jsonObject.getInteger("errcode") != null&& jsonObject.getInteger("errcode") != 0) {
            Integer errcode = jsonObject.getInteger("errcode");
            throw com.kantboot.util.rest.exception.BaseException.of("thirdpartyWechatMpRequestError:mp:getPhoneNumber:" + errcode,
                    jsonObject.getString("errmsg")
                    , "zh_CN");
        }
        ThirdpartyWechatMiniprogramPhone phone = new ThirdpartyWechatMiniprogramPhone();
        JSONObject phoneInfo = jsonObject.getJSONObject("phone_info");
        phone.setPhoneNumber(phoneInfo.getString("phoneNumber"));
        phone.setPurePhoneNumber(phoneInfo.getString("purePhoneNumber"));
        phone.setCountryCode(phoneInfo.getString("countryCode"));
        JSONObject watermark = phoneInfo.getJSONObject("watermark");
        ThirdpartyWechatMiniprogramPhone.Watermark wm = new ThirdpartyWechatMiniprogramPhone.Watermark();
        wm.setAppid(watermark.getString("appid"));
        wm.setTimestamp(watermark.getLong("timestamp"));
        return phone;
    }

    /**
     * 登录凭证校验
     * @param appid 小程序AppID
     * @param secret 小程序AppSecret
     * @param jsCode 登录时获取的 code
     * @return 包含 session_key、openid、unionid（若已绑定到微信开放平台帐号下会返回）
     */
    public static ThirdpartyWechatSession jscode2session(String appid, String secret, String jsCode) {
        String apiUrl = "https://api.weixin.qq.com/sns/jscode2session" +
                "?appid=" + appid +
                "&secret=" + secret +
                "&js_code=" + jsCode +
                "&grant_type=authorization_code";
        String send = HttpSendUtil.send(new HttpSendConfig().setUrl(apiUrl).setMethod("GET"));
        JSONObject jsonObject = JSONObject.parseObject(send);
        if (jsonObject.getInteger("errcode") != null&& jsonObject.getInteger("errcode") != 0) {
            Integer errcode = jsonObject.getInteger("errcode");
            throw com.kantboot.util.rest.exception.BaseException.of("thirdpartyWechatMpRequestError:mp:jscode2session:" + errcode,
                    jsonObject.getString("errmsg")
                    , "zh_CN");
        }
        ThirdpartyWechatSession session = new ThirdpartyWechatSession();
        session.setSessionKey(jsonObject.getString("session_key"));
        session.setUnionId(jsonObject.getString("unionid"));
        session.setOpenId(jsonObject.getString("openid"));
        return session;
    }

    /**
     * 获取用户手机号
     * @param appid 小程序AppID
     * @param secret 小程序AppSecret
     * @param code 通过 wx.getPhoneNumber 获取到的 code
     * @return 包含用户手机号等信息
     */
    public static ThirdpartyWechatMiniprogramPhone getPhoneNumber(String appid, String secret, String code) {
        ThirdpartyWechatToken token = ThirdpartyWechatUtil.createToken(appid, secret);
        String accessToken = token.getAccessToken();
        return getPhoneNumber(accessToken, code);
    }

}
