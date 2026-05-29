package com.kantboot.ai.chat.web.controller;

import com.kantboot.ai.chat.domain.dto.AiChatCreateDialogDTO;
import com.kantboot.ai.chat.domain.dto.AiChatSendMessageDTO;
import com.kantboot.ai.chat.service.IAiChatDialogService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@AuthInit(name = "AI对话", description = "AI对话管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/ai-chat-web/dialog")
public class AiChatDialogController {
    @Resource
    private IAiChatDialogService service;

    @AuthInit(name = "创建AI对话", sourceLanguageCode = "zh_CN", allPass = true)
    @PostMapping("/create")
    public RestResult<?> create(@RequestBody AiChatCreateDialogDTO dto) {
        return RestResult.success(service.createDialog(dto), CommonSuccessStateConsts.SAVE_SUCCESS);
    }

    @AuthInit(name = "获取我的对话列表", sourceLanguageCode = "zh_CN", allPass = true)
    @GetMapping("/getBySelf")
    public RestResult<?> getBySelf() {
        return RestResult.success(service.getBySelf(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "根据ID获取对话", sourceLanguageCode = "zh_CN", allPass = true)
    @GetMapping("/getById")
    public RestResult<?> getById(@RequestParam Long id) {
        return RestResult.success(service.getById(id), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "删除对话", sourceLanguageCode = "zh_CN", allPass = true)
    @DeleteMapping("/deleteById")
    public RestResult<?> deleteById(@RequestParam Long id) {
        service.deleteById(id);
        return RestResult.success(null, CommonSuccessStateConsts.REMOVE_SUCCESS);
    }

    @AuthInit(name = "发送消息（WebSocket）", sourceLanguageCode = "zh_CN", allPass = true)
    @PostMapping("/sendMessage")
    public RestResult<?> sendMessage(@RequestBody AiChatSendMessageDTO dto) {
        service.sendMessage(dto);
        return RestResult.success(null, CommonSuccessStateConsts.SEND_SUCCESS);
    }

    @AuthInit(name = "发送消息（SSE流式）", sourceLanguageCode = "zh_CN", allPass = true)
    @PostMapping("/sendMessageOfStream")
    public ResponseEntity<StreamingResponseBody> sendMessageOfStream(@RequestBody AiChatSendMessageDTO dto) {
        return service.sendMessageOfStream(dto);
    }
}
