package com.kantboot.engine.jvm.web.controller;

import com.kantboot.engine.jvm.service.IEngineJvmService;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/engine-jvm-web/admin/jvm")
public class EngineJvmControllerOfAdmin {

    @Resource
    private IEngineJvmService service;

    @RequestMapping("/getMemoryInfo")
    public RestResult<?> getMemoryInfo() {
        return RestResult.success(service.getMemoryInfo(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/getThreadInfos")
    public RestResult<?> getThreadInfos() {
        return RestResult.success(service.getThreadInfos(), CommonSuccessStateConsts.GET_SUCCESS);
    }

}
