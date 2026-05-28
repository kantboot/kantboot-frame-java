package com.kantboot.system.dict.web.admin.controller;

import com.kantboot.system.dict.domain.entity.SysDict;
import com.kantboot.system.dict.service.ISysDictService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AuthInit(name = "字典管理",description = "字典管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/system-dict-web/admin/dict")
public class SysDictControllerOfAdmin extends BaseAdminController<SysDict,Long> {

    @Resource
    private ISysDictService service;

    @Override
    @RequestMapping("/save")
    public RestResult<?> save(@RequestBody SysDict sysDict) {
        return RestResult.success(service.save(sysDict), CommonSuccessStateConsts.SAVE_SUCCESS);
    }

    @Override
    @RequestMapping("/saveBatch")
    public RestResult<?> saveBatch(@RequestBody List<SysDict> entities) {
        service.saveBatch(entities);
        return RestResult.success(null, CommonSuccessStateConsts.SAVE_SUCCESS);
    }
}