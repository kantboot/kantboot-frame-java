package com.kantboot.tool.area.web.controller;

import com.kantboot.tool.area.service.IToolAreaService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name="地区管理",description = "地区管理")
@RestController
@RequestMapping("/tool-area-web/area")
public class ToolAreaController {

    @Resource
    private IToolAreaService toolAreaService;


    @AuthInit(name="根据级别获取地区",description = "根据级别获取地区",noNeedLogin = true)
    @RequestMapping("/getByLevel")
    public RestResult<?> getByLevel(@RequestParam("level") Integer level){
        return RestResult.success(toolAreaService.getByLevel(level), CommonSuccessStateConsts.GET_SUCCESS);
    }


}
