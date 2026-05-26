package com.kantboot.engine.database.web.admin.controller;

import com.kantboot.engine.database.service.IEngineDatabaseService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/engine-database-web/admin/database")
@AuthInit(
        name = "引擎数据库管理",
        description = "引擎数据库管理相关接口"
)
public class EngineDatabaseControllerOfAdmin {

    @Resource
    private IEngineDatabaseService service;

    @RequestMapping("/getInfo")
    @AuthInit(
            name = "获取数据库信息",
            description = "获取数据库信息"
    )
    public RestResult<?> getInfo() {
        return RestResult.success(service.getInfo(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/getPoolInfo")
    @AuthInit(
            name = "获取数据库连接池信息",
            description = "获取数据库连接池信息"
    )
    public RestResult<?> getPoolInfo() {
        return RestResult.success(service.getPoolInfo(), CommonSuccessStateConsts.GET_SUCCESS);
    }

}
