package com.kantboot.engine.docker.web.admin.controller;

import com.kantboot.engine.docker.domain.entity.EngineDockerContainer;
import com.kantboot.engine.docker.service.IEngineDockerContainerService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/engine-docker-web/admin/container")
@AuthInit(name = "Docker容器管理")
public class EngineDockerContainerControllerOfAdmin {

    @Resource
    private IEngineDockerContainerService containerService;

    @RequestMapping("/list")
    @AuthInit(name = "获取容器列表")
    public RestResult<List<EngineDockerContainer>> list(
            @RequestParam(defaultValue = "true") boolean all) {
        return RestResult.success(containerService.list(all), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/inspect")
    @AuthInit(name = "查看容器详情")
    public RestResult<EngineDockerContainer> inspect(@RequestParam String id) {
        return RestResult.success(containerService.inspect(id), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/start")
    @AuthInit(name = "启动容器")
    public RestResult<?> start(@RequestParam String id) {
        containerService.start(id);
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    @RequestMapping("/stop")
    @AuthInit(name = "停止容器")
    public RestResult<?> stop(@RequestParam String id) {
        containerService.stop(id);
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    @RequestMapping("/restart")
    @AuthInit(name = "重启容器")
    public RestResult<?> restart(@RequestParam String id) {
        containerService.restart(id);
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    @RequestMapping("/pause")
    @AuthInit(name = "暂停容器")
    public RestResult<?> pause(@RequestParam String id) {
        containerService.pause(id);
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    @RequestMapping("/unpause")
    @AuthInit(name = "恢复容器")
    public RestResult<?> unpause(@RequestParam String id) {
        containerService.unpause(id);
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    @RequestMapping("/remove")
    @AuthInit(name = "删除容器")
    public RestResult<?> remove(
            @RequestParam String id,
            @RequestParam(defaultValue = "false") boolean force,
            @RequestParam(defaultValue = "false") boolean removeVolumes) {
        containerService.remove(id, force, removeVolumes);
        return RestResult.success(null, CommonSuccessStateConsts.REMOVE_SUCCESS);
    }

    @RequestMapping("/logs")
    @AuthInit(name = "查看容器日志")
    public RestResult<String> logs(
            @RequestParam String id,
            @RequestParam(defaultValue = "100") int tail) {
        return RestResult.success(containerService.logs(id, tail), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/exec")
    @AuthInit(name = "容器内执行命令")
    public RestResult<String> exec(
            @RequestParam String id,
            @RequestParam String[] command) {
        return RestResult.success(containerService.exec(id, command), CommonSuccessStateConsts.GET_SUCCESS);
    }

}
