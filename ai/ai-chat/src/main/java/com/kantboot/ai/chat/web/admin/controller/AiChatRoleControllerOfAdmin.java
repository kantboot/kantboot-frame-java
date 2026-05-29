package com.kantboot.ai.chat.web.admin.controller;

import com.kantboot.ai.chat.domain.entity.AiChatRole;
import com.kantboot.ai.chat.service.IAiChatRoleService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@AuthInit(name = "AI角色管理", description = "AI角色管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/ai-chat-web/admin/role")
public class AiChatRoleControllerOfAdmin {
    @Resource
    private IAiChatRoleService service;

    @AuthInit(name = "保存AI角色", sourceLanguageCode = "zh_CN")
    @PostMapping("/save")
    public RestResult<?> save(@RequestBody AiChatRole role) {
        return RestResult.success(service.save(role), CommonSuccessStateConsts.SAVE_SUCCESS);
    }

    @AuthInit(name = "删除AI角色", sourceLanguageCode = "zh_CN")
    @DeleteMapping("/deleteById")
    public RestResult<?> deleteById(@RequestParam Long id) {
        service.deleteById(id);
        return RestResult.success(null, CommonSuccessStateConsts.REMOVE_SUCCESS);
    }
}
