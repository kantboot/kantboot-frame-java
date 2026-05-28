package com.kantboot.user.account.web.admin.controller;

import com.kantboot.user.account.domain.entity.UserAccountAuthRole;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "用户账号权限角色管理",description = "用户账号权限角色管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/user-account-web/admin/userAccountAuthRole")
public class UserAccountAuthRoleControllerOfAdmin extends BaseAdminController<UserAccountAuthRole,Long> {

}
