package com.kantboot.user.account.web.controller;

import com.kantboot.user.account.service.IUserAccountRegisterService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户账号注册控制器
 */
@AuthInit(name = "用户账号注册", description = "用户账号注册", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/user-account-web/userAccountRegister")
public class UserAccountRegisterController {

    @Resource
    private IUserAccountRegisterService service;

    /**
     * 根据用户密码注册
     * 一般不开放
     */
    @AuthInit(name = "根据用户名密码注册", description = "根据用户名密码注册", sourceLanguageCode = "zh_CN", noNeedLogin = true)
    @RequestMapping("/registerByUsernameAndPassword")
    public RestResult<Object> registerByUsernameAndPassword(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        return RestResult.success(service.registerByUsernameAndPassword(username, password),
                CommonSuccessStateConsts.REGISTER_SUCCESS);
    }

}
