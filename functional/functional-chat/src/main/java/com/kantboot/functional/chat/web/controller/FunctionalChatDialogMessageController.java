package com.kantboot.functional.chat.web.controller;

import com.kantboot.functional.chat.domain.dto.DialogMessageDTO;
import com.kantboot.functional.chat.service.IFunctionalChatDialogMessageService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/functional-chat-web/message")
@AuthInit(name = "消息", description = "消息", sourceLanguageCode = "zh_CN")
public class FunctionalChatDialogMessageController {

    @Resource
    private IFunctionalChatDialogMessageService messageService;

    @AuthInit(name = "发送消息", description = "发送消息", allPass = true)
    @RequestMapping("/send")
    public RestResult<?> sendMessage(@RequestBody DialogMessageDTO dto) {
        messageService.send(dto);
        return RestResult.success(null, CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "获取最大消息列表", description = "获取最大消息列表", allPass = true)
    @RequestMapping("/getMaxList")
    public RestResult<?> getMaxList(@RequestParam("dialogId") Long dialogId,@RequestParam("minId") Long minId) {
        return RestResult.success(messageService.getMaxList(dialogId,minId), CommonSuccessStateConsts.GET_SUCCESS);
    }


}
