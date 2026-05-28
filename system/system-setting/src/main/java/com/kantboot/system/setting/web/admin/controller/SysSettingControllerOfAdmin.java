package com.kantboot.system.setting.web.admin.controller;

import com.kantboot.system.setting.domain.entity.SysSetting;
import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统设置管理控制器
 * 提供对系统设置的管理功能
 * 继承自BaseAdminController，提供基本的CRUD操作
 */
@AuthInit(name = "系统设置管理", description = "系统设置管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/system-setting-web/admin/setting")
public class SysSettingControllerOfAdmin extends BaseAdminController<SysSetting, Long> {

    @Resource
    private ISysSettingService service;

    @Override
    @AuthInit(name = "保存（重写了通用后台管理）",description = "重写了通用后台管理", sourceLanguageCode = "zh_CN")
    @RequestMapping("/save")
    public RestResult<?> save(@RequestBody SysSetting entity) {
        return RestResult.success(service.save(entity), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @Override
    @AuthInit(name = "删除（重写了通用后台管理）",description = "重写了通用后台管理", sourceLanguageCode = "zh_CN")
    @RequestMapping("/remove")
    public RestResult<?> remove(@RequestBody SysSetting entity) {
        service.remove(entity);
        return RestResult.success(null, CommonSuccessStateConsts.REMOVE_SUCCESS);
    }

}
