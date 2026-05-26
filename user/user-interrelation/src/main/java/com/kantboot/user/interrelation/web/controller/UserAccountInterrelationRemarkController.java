package com.kantboot.user.interrelation.web.controller;

import com.kantboot.user.interrelation.service.IUserAccountInterrelationRemarkService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "用户账号关系备注", description = "用户账号关系八日住", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/user-interrelation-web/interrelationRemark")
public class UserAccountInterrelationRemarkController {

    @Resource
    private IUserAccountInterrelationRemarkService service;

    @AuthInit(name = "设置备注", description = "设置备注", sourceLanguageCode = "zh_CN")
    @RequestMapping("/setRemark")
    public RestResult<?> setRemark(
            @RequestParam("userAccountIdOfRemark") Long userAccountIdOfRemark,
            @RequestParam("remark") String remark) {
        service.setRemark(userAccountIdOfRemark, remark);
        return RestResult.success(null, CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "获取备注", description = "获取备注", sourceLanguageCode = "zh_CN")
    @RequestMapping("/getRemark")
    public RestResult<?> getRemark(
            @RequestParam("userAccountIdOfRemark") Long userAccountIdOfRemark) {
        return RestResult.success(service.getRemark(userAccountIdOfRemark), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "获取当前用户的备注", description = "获取当前用户的备注", sourceLanguageCode = "zh_CN")
    @RequestMapping("/getBySelf")
    public RestResult<?> getBySelf() {
        return RestResult.success(service.getBySelf(), CommonSuccessStateConsts.GET_SUCCESS);
    }

}
