package com.kantboot.system.auth.web.admin.controller;

import com.kantboot.system.auth.domain.entity.SysAuthUri;
import com.kantboot.system.auth.service.ISysAuthUriService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "URI管理",description = "URI管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/system-auth-web/admin/uri")
public class SysAuthUriControllerOfAdmin extends BaseAdminController<SysAuthUri,Long> {

    @Resource
    private ISysAuthUriService service;

    @Override
    @RequestMapping("remove")
    public RestResult<?> remove(@RequestBody SysAuthUri entity) {
        service.remove(entity);
        return RestResult.success(null, CommonSuccessStateConsts.REMOVE_SUCCESS);
    }

}
