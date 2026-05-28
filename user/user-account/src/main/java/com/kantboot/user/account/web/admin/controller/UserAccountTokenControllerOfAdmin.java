package com.kantboot.user.account.web.admin.controller;

import com.kantboot.user.account.domain.entity.UserAccountToken;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "用户账户令牌")
@RestController
@RequestMapping("/user-account-web/admin/token")
public class UserAccountTokenControllerOfAdmin
    extends BaseAdminController<UserAccountToken, Long> {
}
