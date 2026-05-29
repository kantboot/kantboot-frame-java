package com.kantboot.ai.chat.web.admin.controller;

import com.kantboot.ai.chat.domain.entity.AiChatModel;
import com.kantboot.ai.chat.service.IAiChatModelService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@AuthInit(name = "AI模型配置管理", description = "AI模型配置管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/ai-chat-web/admin/model")
public class AiChatModelController {
    @Resource
    private IAiChatModelService service;

    @AuthInit(name = "保存AI模型配置", sourceLanguageCode = "zh_CN")
    @PostMapping("/save")
    public RestResult<?> save(@RequestBody AiChatModel model) {
        return RestResult.success(service.save(model), CommonSuccessStateConsts.SAVE_SUCCESS);
    }

    @AuthInit(name = "获取所有AI模型配置", sourceLanguageCode = "zh_CN")
    @GetMapping("/getAll")
    public RestResult<?> getAll() {
        return RestResult.success(service.getAll(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "根据ID获取AI模型配置", sourceLanguageCode = "zh_CN")
    @GetMapping("/getById")
    public RestResult<?> getById(@RequestParam Long id) {
        return RestResult.success(service.getById(id), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "删除AI模型配置", sourceLanguageCode = "zh_CN")
    @DeleteMapping("/deleteById")
    public RestResult<?> deleteById(@RequestParam Long id) {
        service.deleteById(id);
        return RestResult.success(null, CommonSuccessStateConsts.REMOVE_SUCCESS);
    }
}
