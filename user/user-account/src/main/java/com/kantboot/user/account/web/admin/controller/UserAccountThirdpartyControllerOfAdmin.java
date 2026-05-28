package com.kantboot.user.account.web.admin.controller;

import com.kantboot.user.account.domain.entity.UserAccountThirdparty;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-account-admin/admin/thirdparty")
public class UserAccountThirdpartyControllerOfAdmin
    extends BaseAdminController<UserAccountThirdparty, Long> {
}
