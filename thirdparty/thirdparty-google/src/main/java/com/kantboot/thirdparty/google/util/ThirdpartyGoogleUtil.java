package com.kantboot.thirdparty.google.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.thirdparty.google.util.domain.GoogleAccessToken;
import com.kantboot.thirdparty.google.util.domain.GoogleUserInfo;
import com.kantboot.util.http.HttpSendUtil;
import com.kantboot.util.http.domain.config.HttpSendConfig;
import com.kantboot.util.rest.exception.BaseException;

import java.util.Map;

public class ThirdpartyGoogleUtil {

    public static GoogleAccessToken createAccessToken(
            String clientId,
            String clientSecret,
            String redirectUri,
            String code
    ){
        String send = HttpSendUtil.send(new HttpSendConfig()
                .setMethod("POST")
                .setUrl("https://oauth2.googleapis.com/token")
                .setContentType("application/x-www-form-urlencoded")
                .setBody(Map.of(
                        "grant_type", "authorization_code",
                        "client_id", clientId,
                        "client_secret", clientSecret,
                        "redirect_uri", redirectUri,
                        "code", code))
        );

        JSONObject jsonObject = JSONObject.parseObject(send);
        String errorStr = jsonObject.getString("error");
        if(StrUtil.isNotEmpty(errorStr)){
           throw BaseException.of("ThirdpartyGoogleRequestError:createAccessToken:"+errorStr,
                   jsonObject.getString("error_description"), "en");
        }
        String accessToken = jsonObject.getString("access_token");
        Long expiresIn = jsonObject.getLong("expires_in");
        String refreshToken = jsonObject.getString("refresh_token");
        String scope = jsonObject.getString("scope");
        String tokenType = jsonObject.getString("token_type");
        String idToken = jsonObject.getString("id_token");
        GoogleAccessToken googleAccessToken = new GoogleAccessToken();
        googleAccessToken.setAccessToken(accessToken);
        googleAccessToken.setExpiresIn(expiresIn);
        googleAccessToken.setRefreshToken(refreshToken);
        googleAccessToken.setScope(scope);
        googleAccessToken.setTokenType(tokenType);
        googleAccessToken.setIdToken(idToken);
        return googleAccessToken;
    }

    public static GoogleUserInfo getUserInfo(String accessToken){
        String send = HttpSendUtil.send(new HttpSendConfig()
                .setMethod("GET")
                .setUrl("https://www.googleapis.com/oauth2/v2/userinfo")
                .addHeader("Authorization", "Bearer " + accessToken)
        );

        JSONObject json = JSONObject.parseObject(send);
        if (json.containsKey("error")) {
            JSONObject error = json.getJSONObject("error");
            throw BaseException.of("ThirdpartyGoogleRequestError:getUserInfo",
                    error.getString("message"), "en");
        }

        GoogleUserInfo userInfo = new GoogleUserInfo();
        userInfo.setId(json.getString("id"));
        userInfo.setEmail(json.getString("email"));
        userInfo.setVerifiedEmail(json.getBoolean("verified_email"));
        userInfo.setName(json.getString("name"));
        userInfo.setGivenName(json.getString("given_name"));
        userInfo.setPicture(json.getString("picture"));

        return userInfo;
    }

}
