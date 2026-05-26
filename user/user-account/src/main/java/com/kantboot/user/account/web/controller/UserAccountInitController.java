package com.kantboot.user.account.web.controller;

import com.kantboot.user.account.service.IUserAccountInitService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "用户账号初始化",description = "用户账号初始化",sourceLanguageCode = "zh-CN")
@RestController
@RequestMapping("/user-account-web/userAccountInit")
public class UserAccountInitController {

    @Resource
    private IUserAccountInitService userAccountInitService;

    @AuthInit(name = "初始化用户账号",description = "初始化用户账号",sourceLanguageCode = "zh-CN")
    @RequestMapping("/initByUsernameAndPassword")
    public RestResult<?> initByUsernameAndPassword(@RequestParam("username") String username, @RequestParam("password") String password) {
        return RestResult.success(userAccountInitService.initByUsernameAndPassword(username, password), CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    @AuthInit(name = "初始化用户账号",description = "初始化用户账号",sourceLanguageCode = "zh-CN")
    @RequestMapping("/initByEmailAndPassword")
    public RestResult<?> initByEmailAndPassword(@RequestParam("email") String email, @RequestParam("password") String password) {
        return RestResult.success(userAccountInitService.initByEmailAndPassword(email, password), CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

}
