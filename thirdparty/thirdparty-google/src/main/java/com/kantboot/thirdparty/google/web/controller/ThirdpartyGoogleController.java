package com.kantboot.thirdparty.google.web.controller;

import com.kantboot.thirdparty.google.service.IThirdpartyGoogleService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "Google第三方", description = "Google第三方", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/thirdparty-google-web/google")
public class ThirdpartyGoogleController {

    @Resource
    private IThirdpartyGoogleService service;

    @AuthInit(name = "获取Google客户端ID", description = "获取Google客户端ID", sourceLanguageCode = "zh_CN", noNeedLogin = true)
    @RequestMapping("/getClientId")
    public RestResult<?> getClientId() {
        return RestResult.success(service.getClientId(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name="登录",description = "通过Google登录",sourceLanguageCode = "zh_CN",noNeedLogin = true)
    @RequestMapping("/login")
    public RestResult<?> login(
            @RequestParam("redirectUri") String redirectUri,
            @RequestParam("code") String code){
        return RestResult.success(service.login(redirectUri,code), CommonSuccessStateConsts.LOGIN_SUCCESS);
    }

}
