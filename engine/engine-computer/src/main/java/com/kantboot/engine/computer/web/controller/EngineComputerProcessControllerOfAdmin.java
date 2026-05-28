package com.kantboot.engine.computer.web.controller;

import com.kantboot.engine.computer.service.IEngineComputerProcessService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/engine-computer-web/admin/process")
@AuthInit(name = "进程信息",sourceLanguageCode = "zh_CN")
public class EngineComputerProcessControllerOfAdmin {

    @Resource
    private IEngineComputerProcessService service;

    @RequestMapping("/getList")
    @AuthInit(name = "获取进程列表",sourceLanguageCode = "zh_CN")
    public RestResult<?> getList() {
        return RestResult.success(service.getList(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/getByPid")
    @AuthInit(name = "根据pid获取进程信息",sourceLanguageCode = "zh_CN")
    public RestResult<?> getByPid(@RequestParam("pid") int pid) {
        return RestResult.success(service.getByPid(pid), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/killByPid")
    @AuthInit(name = "根据pid杀死进程",sourceLanguageCode = "zh_CN")
    public RestResult<?> killByPid(@RequestParam("pid") int pid) {
        return RestResult.success(service.killByPid(pid), CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    @RequestMapping("/getThreadsByPid")
    @AuthInit(name = "根据pid获取线程详情",sourceLanguageCode = "zh_CN")
    public RestResult<?> getThreadsByPid(@RequestParam("pid") int pid) {
        return RestResult.success(service.getThreadsByPid(pid), CommonSuccessStateConsts.GET_SUCCESS);
    }

}
