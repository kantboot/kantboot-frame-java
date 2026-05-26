package com.kantboot.tool.area.web.controller;

import com.kantboot.tool.area.domain.entity.ToolAreaLocation;
import com.kantboot.tool.area.service.IToolAreaLocationService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name="地理位置管理",description = "地理位置管理")
@RestController
@RequestMapping("/tool-area-web/location")
public class ToolAreaLocationController
    extends BaseAdminController<ToolAreaLocation,Long> {

    @Resource
    private IToolAreaLocationService service;

    @AuthInit(name = "获取自身位置", description = "获取自身位置",allPass = true)
    @RequestMapping("/getLocationBySelf")
    public RestResult<?> getLocationBySelf() {
        return RestResult.success(
                service.getLocationBySelf(), CommonSuccessStateConsts.GET_SUCCESS);
    }


}
