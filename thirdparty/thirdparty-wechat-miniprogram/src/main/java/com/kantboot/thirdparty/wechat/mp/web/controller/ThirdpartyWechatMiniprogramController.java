package com.kantboot.thirdparty.wechat.mp.web.controller;

import com.kantboot.thirdparty.wechat.mp.service.IThirdpartyWechatMiniprogramService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AuthInit(name = "微信小程序第三方", description = "微信小程序第三方", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/thirdparty-wechat-miniprogram-web/wechatMiniprogram")
public class ThirdpartyWechatMiniprogramController {

    @Resource
    private IThirdpartyWechatMiniprogramService service;

    @AuthInit(name="通过用户信息登录",description = "通过用户信息登录",sourceLanguageCode = "zh_CN",noNeedLogin = true)
    @RequestMapping("/loginByUserInfo")
    public RestResult<?> loginByUserInfo(@RequestParam("code") String code){
        return RestResult.success(service.loginByUserInfo(code), CommonSuccessStateConsts.LOGIN_SUCCESS);
    }

    @AuthInit(name="通过手机号登录",description = "通过手机号登录",sourceLanguageCode = "zh_CN",noNeedLogin = true)
    @RequestMapping("/loginByPhone")
    public RestResult<?> loginByPhone(@RequestParam("code") String code){
        return RestResult.success(service.loginByPhone(code), CommonSuccessStateConsts.LOGIN_SUCCESS);
    }

    @AuthInit(name="通过手机号和用户信息登录",description = "通过手机号和用户信息登录",sourceLanguageCode = "zh_CN",noNeedLogin = true)
    @RequestMapping("/loginByPhoneAndUserInfo")
    public RestResult<?> loginByPhoneAndUserInfo(@RequestBody Map<String,String> map){
        String phoneCode = map.get("phoneCode");
        String userInfoCode = map.get("userInfoCode");
        return RestResult.success(service.loginByPhoneAndUserInfo(phoneCode,userInfoCode), CommonSuccessStateConsts.LOGIN_SUCCESS);
    }

    @AuthInit(name="绑定微信小程序",description = "绑定微信小程序",sourceLanguageCode = "zh_CN")
    @RequestMapping("/bindByUserInfo")
    public RestResult<?> bindByUserInfo(@RequestParam("code") String code){
        service.bindByUserInfo(code);
        return RestResult.success(null, CommonSuccessStateConsts.BIND_SUCCESS);
    }

}
