package com.kantboot.thirdparty.wechat.util;

import com.alibaba.fastjson2.JSONObject;
import com.kantboot.thirdparty.wechat.util.domain.ThirdpartyWechatToken;
import com.kantboot.util.http.HttpSendUtil;
import com.kantboot.util.http.domain.config.HttpSendConfig;
import com.kantboot.util.rest.exception.BaseException;

/**
 * 微信工具类
 */
public class ThirdpartyWechatUtil {

    public static ThirdpartyWechatToken createToken(String appid, String appSecret) {
        String apiUrl = "https://api.weixin.qq.com/cgi-bin/token" +
                "?grant_type=client_credential" +
                "&appid=" + appid +
                "&secret=" + appSecret;
        String send = HttpSendUtil.send(
                new HttpSendConfig()
                        .setUrl(apiUrl)
                        .setMethod("GET")
        );
        JSONObject jsonObject = JSONObject.parseObject(send);
        if(jsonObject.getInteger("errcode") != null&& jsonObject.getInteger("errcode") != 0){
            Integer errcode = jsonObject.getInteger("errcode");
            throw BaseException.of("thirdpartyWechatRequestError:base:createToken:"+errcode,
                    jsonObject.getString("errmsg")
                    , "zh_CN");
        }
        ThirdpartyWechatToken token = new ThirdpartyWechatToken();
        token.setAccessToken(jsonObject.getString("access_token"));
        token.setExpiresIn(jsonObject.getInteger("expires_in"));
        return token;
    }

}
