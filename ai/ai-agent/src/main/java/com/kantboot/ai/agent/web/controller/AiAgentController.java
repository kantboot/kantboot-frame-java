package com.kantboot.ai.agent.web.controller;

import com.kantboot.ai.agent.domain.dto.AiAgentRunDTO;
import com.kantboot.ai.agent.service.IAiAgentSessionService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@AuthInit(name = "AI Agent", description = "AI Agent 会话管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/ai-agent-web/session")
public class AiAgentController {

    @Resource
    private IAiAgentSessionService sessionService;

    @AuthInit(name = "SSE 流式运行 Agent", sourceLanguageCode = "zh_CN", allPass = true)
    @PostMapping("/runOfStream")
    public ResponseEntity<StreamingResponseBody> runOfStream(@RequestBody AiAgentRunDTO dto) {
        return sessionService.runOfStream(dto);
    }

    @AuthInit(name = "批准当前步骤（ASK 模式）", sourceLanguageCode = "zh_CN", allPass = true)
    @PostMapping("/approve")
    public RestResult<?> approve(@RequestParam Long sessionId) {
        sessionService.approve(sessionId);
        return RestResult.success(null, CommonSuccessStateConsts.SAVE_SUCCESS);
    }

    @AuthInit(name = "拒绝当前步骤（ASK 模式）", sourceLanguageCode = "zh_CN", allPass = true)
    @PostMapping("/reject")
    public RestResult<?> reject(@RequestParam Long sessionId) {
        sessionService.reject(sessionId);
        return RestResult.success(null, CommonSuccessStateConsts.SAVE_SUCCESS);
    }

    @AuthInit(name = "获取我的会话列表", sourceLanguageCode = "zh_CN", allPass = true)
    @GetMapping("/getBySelf")
    public RestResult<?> getBySelf() {
        return RestResult.success(sessionService.getSessionsBySelf(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "获取会话步骤列表", sourceLanguageCode = "zh_CN", allPass = true)
    @GetMapping("/getSteps")
    public RestResult<?> getSteps(@RequestParam Long sessionId) {
        return RestResult.success(sessionService.getStepsBySessionId(sessionId), CommonSuccessStateConsts.GET_SUCCESS);
    }
}
