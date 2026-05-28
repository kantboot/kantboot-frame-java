package com.kantboot.thirdparty.baidu.translate.web.admin.controller;


import com.kantboot.thirdparty.baidu.translate.service.IThirdpartyBaiduTranslateService;
import com.kantboot.thirdparty.baidu.translate.setting.ThirdpartyBaiduTranslateSetting;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/thirdparty-baidu-translate-web/admin/baiduTranslate")
public class ThirdpartyBaiduTranslateControllerOfAdmin {

    @Resource
    private IThirdpartyBaiduTranslateService service;

    /**
     * 获取百度翻译的设置
     */
    @RequestMapping("/getSetting")
    public RestResult<?> getSetting() {
        return RestResult.success(service.getSetting(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/setSetting")
    public RestResult<?> setSetting(@RequestBody ThirdpartyBaiduTranslateSetting setting) {
        service.setSetting(setting);
        return RestResult.success(null, CommonSuccessStateConsts.SAVE_SUCCESS);
    }

}
