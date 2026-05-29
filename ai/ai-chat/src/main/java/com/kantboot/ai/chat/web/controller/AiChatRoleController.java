package com.kantboot.ai.chat.web.controller;

import com.kantboot.ai.chat.service.IAiChatRoleService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@AuthInit(name = "AI角色", description = "AI角色查询", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/ai-chat-web/role")
public class AiChatRoleController {
    @Resource
    private IAiChatRoleService service;

    @AuthInit(name = "获取所有AI角色", sourceLanguageCode = "zh_CN", allPass = true)
    @GetMapping("/getAll")
    public RestResult<?> getAll() {
        return RestResult.success(service.getAll(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "根据ID获取AI角色", sourceLanguageCode = "zh_CN", allPass = true)
    @GetMapping("/getById")
    public RestResult<?> getById(@RequestParam Long id) {
        return RestResult.success(service.getById(id), CommonSuccessStateConsts.GET_SUCCESS);
    }
}
