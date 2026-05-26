package com.kantboot.thirdparty.wechat.mp.web.admin.controller;

import com.kantboot.thirdparty.wechat.mp.domain.entity.ThirdpartyWechatMiniprogramRequest;
import com.kantboot.thirdparty.wechat.mp.service.IThirdpartyWechatMiniprogramRequestService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AuthInit(name = "微信小程序第三方请求-管理端", description = "微信小程序第三方请求-管理端", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/thirdparty-wechat-miniprogram-web/admin/request")
public class ThirdpartyWechatMiniprogramRequestControllerOfAdmin
    extends BaseAdminController<ThirdpartyWechatMiniprogramRequest,Long> {

    @Resource
    private IThirdpartyWechatMiniprogramRequestService service;

    @AuthInit(name="根据请求code获取实体",description = "根据请求code获取实体",sourceLanguageCode = "zh_CN")
    @RequestMapping("/getByCode")
    public RestResult<?> getByCode(@RequestParam("code") String code) {
        return RestResult.success(service.getByCode(code), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name="执行请求",description = "执行请求",sourceLanguageCode = "zh_CN")
    @RequestMapping("/execute")
    public RestResult<?> execute(@RequestBody Map<String, Object> map) {
        String code = (String) map.get("code");
        Map<String, String> queries = (Map<String, String>) map.get("queries");
        Map<String, String> bodies = (Map<String, String>) map.get("bodies");
        return RestResult.success(service.execute(code, queries, bodies), CommonSuccessStateConsts.EXECUTE_SUCCESS);
    }


}
