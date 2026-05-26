package com.kantboot.user.balance.web.admin.controller;

import com.kantboot.user.balance.domain.entity.UserBalanceType;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "余额类型管理", description = "余额类型管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/user-balance-web/admin/userBalanceType")
public class UserAccountBalanceTypeControllerOfAdmin
        extends BaseAdminController<UserBalanceType,Long> {

}
