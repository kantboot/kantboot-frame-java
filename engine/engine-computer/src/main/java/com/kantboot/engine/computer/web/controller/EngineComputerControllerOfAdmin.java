package com.kantboot.engine.computer.web.controller;

import com.kantboot.engine.computer.service.IEngineComputerService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/engine-computer-web/admin/computer")
@AuthInit(name = "本机信息")
public class EngineComputerControllerOfAdmin {

    @Resource
    private IEngineComputerService service;

    @RequestMapping("/getSystemInfo")
    @AuthInit(name = "获取系统信息")
    public RestResult<?> getSystemInfo() {
        return RestResult.success(service.getSystemInfo(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/getInfo")
    @AuthInit(name = "获取本机信息")
    public RestResult<?> getInfo() {
        return RestResult.success(service.getInfo(), CommonSuccessStateConsts.GET_SUCCESS);
    }

}
