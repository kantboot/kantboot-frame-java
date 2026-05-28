package com.kantboot.user.interrelation.web.controller;

import com.kantboot.user.interrelation.service.IUserAccountInterrelationBlackService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-interrelation-web/black")
@AuthInit(name = "用户账号拉黑关系", description = "用户账号拉黑关系", sourceLanguageCode = "zh_CN")
public class UserAccountInterrelationBlackController {

    @Resource
    private IUserAccountInterrelationBlackService service;

    @RequestMapping("/isBlack")
    @AuthInit(name = "是否拉黑", description = "是否拉黑", sourceLanguageCode = "zh_CN", allPass = true)
    public RestResult<?> isBlack(@RequestParam("blackUserAccountId") Long blackUserAccountId) {
        return RestResult.success(service.isBlack(blackUserAccountId), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/black")
    @AuthInit(name = "拉黑", description = "拉黑", sourceLanguageCode = "zh_CN", allPass = true)
    public RestResult<?> black(@RequestParam("blackUserAccountId") Long blackUserAccountId) {
        service.black(blackUserAccountId);
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    @RequestMapping("/isBeBlack")
    @AuthInit(name = "是否被拉黑", description = "是否被拉黑", sourceLanguageCode = "zh_CN", allPass = true)
    public RestResult<?> isBeBlack(@RequestParam("userAccountId") Long userAccountId) {
        return RestResult.success(service.isBeBlack(userAccountId), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/getBySelf")
    @AuthInit(name = "获取拉黑列表", description = "获取拉黑列表", sourceLanguageCode = "zh_CN", allPass = true)
    public RestResult<?> getBySelf() {
        return RestResult.success(service.getBySelf(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/unBlack")
    @AuthInit(name = "移出黑名单", description = "移出黑名单", sourceLanguageCode = "zh_CN", allPass = true)
    public RestResult<?> unBlack(@RequestParam("blackUserAccountId") Long blackUserAccountId) {
        service.unBlack(blackUserAccountId);
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }


}
