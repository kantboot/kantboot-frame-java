package com.kantboot.thirdparty.wechat.mp.web.admin.controller;

import com.kantboot.thirdparty.wechat.mp.service.IThirdpartyWechatMiniprogramService;
import com.kantboot.thirdparty.wechat.mp.setting.ThirdpartyWechatMiniprogramSetting;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "微信小程序第三方管理端", description = "微信小程序第三方管理端", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/thirdparty-wechat-miniprogram-web/admin/wechatMiniprogram")
public class ThirdpartyWechatMiniprogramControllerOfAdmin {


    @Resource
    private IThirdpartyWechatMiniprogramService service;

    @AuthInit(name="获取配置",description = "获取配置",sourceLanguageCode = "zh_CN")
    @RequestMapping("/getSetting")
    public RestResult<?> getSetting() {
        return RestResult.success(service.getSetting(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name="设置配置",description = "设置配置",sourceLanguageCode = "zh_CN")
    @RequestMapping("/setSetting")
    public RestResult<?> setSetting(@RequestBody ThirdpartyWechatMiniprogramSetting setting) {
        service.setSetting(setting);
        return RestResult.success(null, CommonSuccessStateConsts.SET_SUCCESS);
    }


}
