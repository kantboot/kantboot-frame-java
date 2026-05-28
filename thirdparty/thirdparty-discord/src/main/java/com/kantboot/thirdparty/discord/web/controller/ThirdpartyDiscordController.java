package com.kantboot.thirdparty.discord.web.controller;

import com.kantboot.thirdparty.discord.service.IThirdpartyDiscordService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "Discord第三方", description = "Discord第三方", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/thirdparty-discord-web/discord")
public class ThirdpartyDiscordController {

    @Resource
    private IThirdpartyDiscordService service;

    @AuthInit(name = "获取Discord客户端ID", description = "获取Discord客户端ID", sourceLanguageCode = "zh_CN", noNeedLogin = true)
    @RequestMapping("/getClientId")
    public RestResult<?> getClientId() {
        return RestResult.success(service.getClientId(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "登录", description = "通过Discord登录", sourceLanguageCode = "zh_CN", noNeedLogin = true)
    @RequestMapping("/login")
    public RestResult<?> login(
            @RequestParam("redirectUri") String redirectUri,
            @RequestParam("code") String code) {
        return RestResult.success(service.login(redirectUri,code), CommonSuccessStateConsts.LOGIN_SUCCESS);
    }



}
