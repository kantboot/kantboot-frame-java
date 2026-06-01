package com.kantboot.engine.docker.web.admin.controller;

import com.kantboot.engine.docker.domain.entity.EngineDockerVolume;
import com.kantboot.engine.docker.service.IEngineDockerVolumeService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/engine-docker-web/admin/volume")
@AuthInit(name = "Docker卷管理")
public class EngineDockerVolumeControllerOfAdmin {

    @Resource
    private IEngineDockerVolumeService volumeService;

    @RequestMapping("/list")
    @AuthInit(name = "获取卷列表")
    public RestResult<List<EngineDockerVolume>> list() {
        return RestResult.success(volumeService.list(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/inspect")
    @AuthInit(name = "查看卷详情")
    public RestResult<EngineDockerVolume> inspect(@RequestParam String name) {
        return RestResult.success(volumeService.inspect(name), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/create")
    @AuthInit(name = "创建卷")
    public RestResult<EngineDockerVolume> create(
            @RequestParam String name,
            @RequestParam(defaultValue = "local") String driver,
            @RequestParam(required = false) Map<String, String> driverOpts,
            @RequestParam(required = false) Map<String, String> labels) {
        return RestResult.success(volumeService.create(name, driver, driverOpts, labels), CommonSuccessStateConsts.SAVE_SUCCESS);
    }

    @RequestMapping("/remove")
    @AuthInit(name = "删除卷")
    public RestResult<?> remove(
            @RequestParam String name,
            @RequestParam(defaultValue = "false") boolean force) {
        volumeService.remove(name, force);
        return RestResult.success(null, CommonSuccessStateConsts.REMOVE_SUCCESS);
    }

    @RequestMapping("/prune")
    @AuthInit(name = "清理未使用卷")
    public RestResult<List<String>> prune() {
        return RestResult.success(volumeService.prune(), CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

}
