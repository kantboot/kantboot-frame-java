package com.kantboot.functional.chat.web.controller;

import com.kantboot.functional.chat.service.IFunctionalChatUserAccountRelationshipService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/functional-chat-web/userAccountRelationship")
@AuthInit(name = "消息", description = "消息", sourceLanguageCode = "zh_CN")
public class FunctionalChatUserAccountRelationshipController {

    @Resource
    private IFunctionalChatUserAccountRelationshipService service;

    @AuthInit(name = "获取自己的用户账户关系", description = "获取自己的用户账户关系", allPass = true)
    @RequestMapping("/getBySelf")
    public RestResult<?> getBySelf() {
        return RestResult.success(service.getBySelf(), CommonSuccessStateConsts.GET_SUCCESS);
    }

}
