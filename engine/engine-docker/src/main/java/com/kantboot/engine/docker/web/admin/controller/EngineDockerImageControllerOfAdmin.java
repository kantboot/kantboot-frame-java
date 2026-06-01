package com.kantboot.engine.docker.web.admin.controller;

import com.kantboot.engine.docker.domain.entity.EngineDockerImage;
import com.kantboot.engine.docker.service.IEngineDockerImageService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/engine-docker-web/admin/image")
@AuthInit(name = "Docker镜像管理")
public class EngineDockerImageControllerOfAdmin {

    @Resource
    private IEngineDockerImageService imageService;

    @RequestMapping("/list")
    @AuthInit(name = "获取镜像列表")
    public RestResult<List<EngineDockerImage>> list() {
        return RestResult.success(imageService.list(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/inspect")
    @AuthInit(name = "查看镜像详情")
    public RestResult<EngineDockerImage> inspect(@RequestParam String id) {
        return RestResult.success(imageService.inspect(id), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/remove")
    @AuthInit(name = "删除镜像")
    public RestResult<?> remove(
            @RequestParam String id,
            @RequestParam(defaultValue = "false") boolean force,
            @RequestParam(defaultValue = "false") boolean noPrune) {
        imageService.remove(id, force, noPrune);
        return RestResult.success(null, CommonSuccessStateConsts.REMOVE_SUCCESS);
    }

    @RequestMapping("/pull")
    @AuthInit(name = "拉取镜像")
    public RestResult<?> pull(
            @RequestParam String repository,
            @RequestParam(defaultValue = "latest") String tag) {
        imageService.pull(repository, tag);
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    @RequestMapping("/prune")
    @AuthInit(name = "清理未使用镜像")
    public RestResult<List<String>> prune() {
        return RestResult.success(imageService.prune(), CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

}
