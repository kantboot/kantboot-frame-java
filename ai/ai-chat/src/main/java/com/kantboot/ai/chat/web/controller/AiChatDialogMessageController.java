package com.kantboot.ai.chat.web.controller;

import com.kantboot.ai.chat.service.IAiChatDialogMessageService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@AuthInit(name = "AI消息", description = "AI对话消息查询", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/ai-chat-web/message")
public class AiChatDialogMessageController {
    @Resource
    private IAiChatDialogMessageService service;

    @AuthInit(name = "获取对话消息列表", sourceLanguageCode = "zh_CN", allPass = true)
    @GetMapping("/getByDialogId")
    public RestResult<?> getByDialogId(@RequestParam Long dialogId) {
        return RestResult.success(service.getByDialogId(dialogId), CommonSuccessStateConsts.GET_SUCCESS);
    }
}
