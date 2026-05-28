package com.kantboot.system.auth.web.admin.controller;

import com.kantboot.system.auth.domain.entity.SysAuthPermissionGroup;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "权限组管理", description = "权限组管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/system-auth-web/admin/permissionGroup")
public class SysAuthPermissionGroupControllerOfAdmin
    extends BaseAdminController<SysAuthPermissionGroup, Long>
{
}
