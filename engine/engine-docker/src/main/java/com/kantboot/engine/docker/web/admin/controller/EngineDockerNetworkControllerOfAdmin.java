package com.kantboot.engine.docker.web.admin.controller;

import com.kantboot.engine.docker.domain.entity.EngineDockerNetwork;
import com.kantboot.engine.docker.service.IEngineDockerNetworkService;
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
@RequestMapping("/engine-docker-web/admin/network")
@AuthInit(name = "Docker网络管理")
public class EngineDockerNetworkControllerOfAdmin {

    @Resource
    private IEngineDockerNetworkService networkService;

    @RequestMapping("/list")
    @AuthInit(name = "获取网络列表")
    public RestResult<List<EngineDockerNetwork>> list() {
        return RestResult.success(networkService.list(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/inspect")
    @AuthInit(name = "查看网络详情")
    public RestResult<EngineDockerNetwork> inspect(@RequestParam String id) {
        return RestResult.success(networkService.inspect(id), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/create")
    @AuthInit(name = "创建网络")
    public RestResult<EngineDockerNetwork> create(
            @RequestParam String name,
            @RequestParam(defaultValue = "bridge") String driver,
            @RequestParam(required = false) Map<String, String> options) {
        return RestResult.success(networkService.create(name, driver, options), CommonSuccessStateConsts.SAVE_SUCCESS);
    }

    @RequestMapping("/remove")
    @AuthInit(name = "删除网络")
    public RestResult<?> remove(@RequestParam String id) {
        networkService.remove(id);
        return RestResult.success(null, CommonSuccessStateConsts.REMOVE_SUCCESS);
    }

    @RequestMapping("/prune")
    @AuthInit(name = "清理未使用网络")
    public RestResult<List<String>> prune() {
        return RestResult.success(networkService.prune(), CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

}
