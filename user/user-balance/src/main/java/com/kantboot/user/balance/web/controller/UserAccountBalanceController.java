package com.kantboot.user.balance.web.controller;

import com.kantboot.user.balance.service.IUserAccountBalanceService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-balance-web/userAccountBalance")
@AuthInit(name = "用户余额",description = "用户余额相关接口",sourceLanguageCode = "zh_CN")
public class UserAccountBalanceController {

    @Resource
    private IUserAccountBalanceService service;

    /**
     * getBySelf
     */
    @RequestMapping("/getBySelf")
    @AuthInit(name = "获取自己的余额",description = "获取自己的余额",sourceLanguageCode = "zh_CN",allPass = true)
    public RestResult<?> getBySelf() {
        return RestResult.success(service.getBySelf(), CommonSuccessStateConsts.GET_SUCCESS);
    }

}
