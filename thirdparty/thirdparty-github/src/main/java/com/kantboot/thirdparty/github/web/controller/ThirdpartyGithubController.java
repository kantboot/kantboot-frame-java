package com.kantboot.thirdparty.github.web.controller;

import com.kantboot.thirdparty.github.service.IThirdpartyService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "GitHub第三方", description = "GitHub第三方", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/thirdparty-github-web/github")
public class ThirdpartyGithubController {

    @Resource
    private IThirdpartyService thirdpartyService;

    @AuthInit(name = "登录", description = "通过GitHub登录", sourceLanguageCode = "zh_CN",noNeedLogin = true)
    @RequestMapping("/login")
    public RestResult<?> login(@RequestParam("code") String code) {
        return RestResult.success(thirdpartyService.login(code), CommonSuccessStateConsts.LOGIN_SUCCESS);
    }

    @AuthInit(name = "获取GitHub客户端ID", description = "获取GitHub客户端ID", sourceLanguageCode = "zh_CN",noNeedLogin = true)
    @RequestMapping("/getClientId")
    public RestResult<String> getClientId() {
        return RestResult.success(thirdpartyService.getClientId(), CommonSuccessStateConsts.GET_SUCCESS);
    }

}
