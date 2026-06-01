package com.kantboot.ai.agent.web.admin.controller;

import com.kantboot.ai.agent.domain.dto.AiAgentCreateDTO;
import com.kantboot.ai.agent.service.IAiAgentService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@AuthInit(name = "AI Agent 管理", description = "AI Agent 配置管理（管理员）", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/ai-agent-admin/agent")
public class AiAgentAdminController {

    @Resource
    private IAiAgentService agentService;

    @AuthInit(name = "创建 Agent", sourceLanguageCode = "zh_CN")
    @PostMapping("/create")
    public RestResult<?> create(@RequestBody AiAgentCreateDTO dto) {
        return RestResult.success(agentService.create(dto), CommonSuccessStateConsts.SAVE_SUCCESS);
    }

    @AuthInit(name = "更新 Agent", sourceLanguageCode = "zh_CN")
    @PutMapping("/update")
    public RestResult<?> update(@RequestParam Long id, @RequestBody AiAgentCreateDTO dto) {
        return RestResult.success(agentService.update(id, dto), CommonSuccessStateConsts.SAVE_SUCCESS);
    }

    @AuthInit(name = "删除 Agent", sourceLanguageCode = "zh_CN")
    @DeleteMapping("/deleteById")
    public RestResult<?> deleteById(@RequestParam Long id) {
        agentService.deleteById(id);
        return RestResult.success(null, CommonSuccessStateConsts.REMOVE_SUCCESS);
    }

    @AuthInit(name = "获取全部 Agent", sourceLanguageCode = "zh_CN")
    @GetMapping("/getAll")
    public RestResult<?> getAll() {
        return RestResult.success(agentService.getAll(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "根据 ID 获取 Agent", sourceLanguageCode = "zh_CN")
    @GetMapping("/getById")
    public RestResult<?> getById(@RequestParam Long id) {
        return RestResult.success(agentService.getById(id), CommonSuccessStateConsts.GET_SUCCESS);
    }
}
