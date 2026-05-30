package com.kantboot.ai.agent.service;

import com.kantboot.ai.agent.domain.dto.AiAgentRunDTO;
import com.kantboot.ai.agent.domain.entity.AiAgentSession;
import com.kantboot.ai.agent.domain.entity.AiAgentStep;
import com.kantboot.ai.agent.domain.vo.AiAgentStepVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;
import java.util.function.Consumer;

public interface IAiAgentSessionService {

    /** SSE 流式运行（不支持 ASK/PLAN 模式） */
    ResponseEntity<StreamingResponseBody> runOfStream(AiAgentRunDTO dto);

    /** 异步运行，通过回调推送步骤（WebSocket 使用） */
    AiAgentSession runAsync(AiAgentRunDTO dto, Consumer<AiAgentStepVO> pushCallback);

    /** ASK 模式：批准当前等待的步骤 */
    void approve(Long sessionId);

    /** ASK 模式：拒绝当前等待的步骤 */
    void reject(Long sessionId);

    List<AiAgentSession> getSessionsBySelf();
    List<AiAgentStep> getStepsBySessionId(Long sessionId);
}
