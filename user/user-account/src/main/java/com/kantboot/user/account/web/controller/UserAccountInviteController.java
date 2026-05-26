package com.kantboot.user.account.web.controller;

import com.kantboot.user.account.service.IUserAccountInviteService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "用户账号邀请", description = "用户账号邀请", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/user-account-web/userAccountInvite")
public class UserAccountInviteController {

    @Resource
    private IUserAccountInviteService service;

    /**
     * 初始化直属码
     */
    @AuthInit(name = "初始化直属码", description = "初始化直属码", sourceLanguageCode = "zh_CN")
    @RequestMapping("/initDirectCodeSelf")
    public RestResult<Void> initDirectCodeSelf() {
        service.initDirectCodeSelf();
        return RestResult.success(null, CommonSuccessStateConsts.CHANGE_SUCCESS);
    }

//    setInviterSelf
    /**
     * 设置邀请人
     */
    @AuthInit(name = "设置邀请人", description = "设置邀请人", sourceLanguageCode = "zh_CN",allPass = true)
    @RequestMapping("/setInviterSelf")
    public RestResult<?> setInviterSelf(@RequestParam("userAccountIdOfInviter") Long userAccountIdOfInviter) {
        service.setInviterSelf(userAccountIdOfInviter);
        return RestResult.success(null, CommonSuccessStateConsts.SET_SUCCESS);
    }

    /**
     * 获取所有邀请到的人
     */
    @AuthInit(name = "获取所有邀请到的人", description = "获取所有邀请到的人", sourceLanguageCode = "zh_CN",allPass = true)
    @RequestMapping("/getAllInviterByUserAccountId")
    public RestResult<Object> getAllInviterByUserAccountId(@RequestParam("userAccountId") Long userAccountId) {
        return RestResult.success(service.getAllInviterByUserAccountId(userAccountId),CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 获取用户自身所有邀请到人
     */
    @AuthInit(name = "获取用户自身所有邀请到人", description = "获取用户自身所有邀请到人", sourceLanguageCode = "zh_CN")
    @RequestMapping("/getAllInviterBySelf")
    public RestResult<Object> getAllInviterBySelf() {
        return RestResult.success(service.getAllInviterBySelf(),CommonSuccessStateConsts.GET_SUCCESS);
    }

}
