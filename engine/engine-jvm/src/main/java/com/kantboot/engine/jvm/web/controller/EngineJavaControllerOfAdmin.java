package com.kantboot.engine.jvm.web.controller;

import com.kantboot.engine.jvm.service.IEngineJavaService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/engine-jvm-web/admin/java")
@AuthInit(name = "Java信息", sourceLanguageCode = "zh_CN")
public class EngineJavaControllerOfAdmin {

    @Resource
    private IEngineJavaService service;

    @RequestMapping("/getInfo")
    @AuthInit(name = "获取Java信息", sourceLanguageCode = "zh_CN")
    public RestResult<?> getInfo() {
        return RestResult.success(service.getInfo(), CommonSuccessStateConsts.GET_SUCCESS);
    }

}
