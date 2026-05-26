package com.kantboot.thirdparty.discord.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.thirdparty.discord.util.param.DiscordAccessToken;
import com.kantboot.thirdparty.discord.util.param.DiscordUserInfo;
import com.kantboot.util.http.HttpSendUtil;
import com.kantboot.util.http.domain.config.HttpSendConfig;
import com.kantboot.util.rest.exception.BaseException;

import java.util.Map;

public class ThirdpartyDiscordUtil {

    public static DiscordAccessToken createAccessToken(
            String clientId,
            String clientSecret,
            String redirectUri,
            String code
    ){
        String send = HttpSendUtil.send(new HttpSendConfig()
                .setMethod("POST")
                .setUrl("https://discord.com/api/oauth2/token")
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
        DiscordAccessToken discordAccessToken = new DiscordAccessToken();
        discordAccessToken.setAccessToken(accessToken);
        discordAccessToken.setExpiresIn(expiresIn);
        discordAccessToken.setRefreshToken(refreshToken);
        discordAccessToken.setScope(scope);
        discordAccessToken.setTokenType(tokenType);
        return discordAccessToken;
    }

    public static DiscordUserInfo getUserInfo(String accessToken){
        String send = HttpSendUtil.send(new HttpSendConfig()
                .setMethod("GET")
                .setUrl("https://discord.com/api/v10/users/@me")
                .addHeader("Authorization", "Bearer " + accessToken)
        );

        JSONObject json = JSONObject.parseObject(send);
        if (json.containsKey("error")) {
            JSONObject error = json.getJSONObject("error");
            throw BaseException.of("ThirdpartyGoogleRequestError:getUserInfo",
                    error.getString("message"), "en");
        }
        DiscordUserInfo userInfo = new DiscordUserInfo();
        userInfo.setId(json.getString("id"));
        userInfo.setUsername(json.getString("username"));
        userInfo.setDiscriminator(json.getString("discriminator"));
        userInfo.setAvatar(json.getString("avatar"));
        userInfo.setPublicFlags(json.getInteger("public_flags"));
        userInfo.setFlags(json.getInteger("flags"));
        userInfo.setBanner(json.getString("banner"));
        userInfo.setAccentColor(json.getString("accent_color"));
        userInfo.setGlobalName(json.getString("global_name"));
        userInfo.setCollectibles(json.getString("collectibles"));
        userInfo.setBannerColor(json.getString("banner_color"));
        userInfo.setClan(json.getString("clan"));
        userInfo.setPrimaryGuild(json.getString("primary_guild"));
        userInfo.setMfaEnabled(json.getBoolean("mfa_enabled"));
        userInfo.setLocale(json.getString("locale"));
        userInfo.setPremiumType(json.getInteger("premium_type"));
        userInfo.setEmail(json.getString("email"));
        userInfo.setVerified(json.getBoolean("verified"));
        return userInfo;
    }

}