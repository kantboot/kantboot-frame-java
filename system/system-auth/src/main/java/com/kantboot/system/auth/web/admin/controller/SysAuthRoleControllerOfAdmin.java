package com.kantboot.system.auth.web.admin.controller;

import com.kantboot.system.auth.domain.entity.SysAuthRole;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "角色管理",description = "角色管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/system-auth-web/admin/role")
public class SysAuthRoleControllerOfAdmin
    extends BaseAdminController<SysAuthRole,Long> {
}
