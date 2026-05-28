package com.kantboot.functional.chat.web.controller;

import com.kantboot.functional.chat.service.IFunctionalChatReadService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/functional-chat-web/read")
@AuthInit(name = "消息", description = "消息", sourceLanguageCode = "zh_CN")
public class FunctionalChatReadController {

    @Resource
    private IFunctionalChatReadService service;

    @AuthInit(name = "标记消息为已读", description = "标记消息为已读", allPass = true)
    @RequestMapping("/markAsReadSelf")
    public RestResult<?> markAsReadSelf(@RequestParam("dialogId") Long dialogId){
        service.markAsReadSelf(dialogId);
        return RestResult.success(null, CommonSuccessStateConsts.GET_SUCCESS);
    }

}
