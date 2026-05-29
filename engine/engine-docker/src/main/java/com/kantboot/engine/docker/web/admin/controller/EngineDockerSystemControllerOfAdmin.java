package com.kantboot.engine.docker.web.admin.controller;

import com.kantboot.engine.docker.domain.entity.EngineDockerInfo;
import com.kantboot.engine.docker.domain.entity.EngineDockerSystemUsage;
import com.kantboot.engine.docker.service.IEngineDockerSystemService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/engine-docker-web/admin/system")
@AuthInit(name = "Docker系统管理")
public class EngineDockerSystemControllerOfAdmin {

    @Resource
    private IEngineDockerSystemService systemService;

    @RequestMapping("/getInfo")
    @AuthInit(name = "获取Docker系统信息")
    public RestResult<EngineDockerInfo> getInfo() {
        return RestResult.success(systemService.getInfo(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/getUsage")
    @AuthInit(name = "获取磁盘使用情况")
    public RestResult<EngineDockerSystemUsage> getUsage() {
        return RestResult.success(systemService.getUsage(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/getVersion")
    @AuthInit(name = "获取Docker版本")
    public RestResult<String> getVersion() {
        return RestResult.success(systemService.getVersion(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/pruneAll")
    @AuthInit(name = "清理所有未使用数据")
    public RestResult<String> pruneAll() {
        return RestResult.success(systemService.pruneAll(), CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

}
