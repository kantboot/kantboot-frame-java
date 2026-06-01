package com.kantboot.ai.agent.websocket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.ai.agent.domain.dto.AiAgentRunDTO;
import com.kantboot.ai.agent.domain.entity.AiAgentSession;
import com.kantboot.ai.agent.domain.vo.AiAgentStepVO;
import com.kantboot.ai.agent.service.IAiAgentSessionService;
import com.kantboot.ai.agent.service.impl.AiAgentApprovalCoordinator;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 消息协议：
 *
 * 客户端 → 服务端:
 *   {"type":"start","agentId":1,"task":"帮我查看磁盘使用情况"}
 *   {"type":"approve","sessionId":123}
 *   {"type":"reject","sessionId":123}
 *
 * 服务端 → 客户端:
 *   {"type":"session","sessionId":123}
 *   {"type":"step","sessionId":...,"stepIndex":...,"thought":"...","command":"...","status":"...","done":false}
 *   {"type":"error","message":"..."}
 */
@Component
public class AiAgentWebSocketHandler extends TextWebSocketHandler {

    @Resource
    private IAiAgentSessionService sessionService;
    @Resource
    private AiAgentApprovalCoordinator approvalCoordinator;

    private final ConcurrentHashMap<String, Long> wsSessionToAgentSession = new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession wsSession, TextMessage message) throws Exception {
        JSONObject msg = JSON.parseObject(message.getPayload());
        String type = msg.getString("type");

        switch (type) {
            case "start" -> handleStart(wsSession, msg);
            case "approve" -> handleApprove(msg);
            case "reject" -> handleReject(msg);
            default -> send(wsSession, errorMsg("Unknown message type: " + type));
        }
    }

    private void handleStart(WebSocketSession wsSession, JSONObject msg) throws IOException {
        Long agentId = msg.getLong("agentId");
        String task = msg.getString("task");

        AiAgentRunDTO dto = new AiAgentRunDTO().setAgentId(agentId).setTask(task);

        AiAgentSession session = sessionService.runAsync(dto, step -> {
            try {
                JSONObject payload = (JSONObject) JSON.toJSON(step);
                payload.put("type", "step");
                send(wsSession, payload.toJSONString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        wsSessionToAgentSession.put(wsSession.getId(), session.getId());

        JSONObject sessionMsg = new JSONObject();
        sessionMsg.put("type", "session");
        sessionMsg.put("sessionId", session.getId());
        send(wsSession, sessionMsg.toJSONString());
    }

    private void handleApprove(JSONObject msg) {
        Long sessionId = msg.getLong("sessionId");
        if (sessionId != null) {
            approvalCoordinator.approve(sessionId);
        }
    }

    private void handleReject(JSONObject msg) {
        Long sessionId = msg.getLong("sessionId");
        if (sessionId != null) {
            approvalCoordinator.reject(sessionId);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession wsSession, CloseStatus status) {
        Long sessionId = wsSessionToAgentSession.remove(wsSession.getId());
        if (sessionId != null) {
            approvalCoordinator.reject(sessionId);
        }
    }

    private void send(WebSocketSession session, String text) throws IOException {
        if (session.isOpen()) {
            synchronized (session) {
                session.sendMessage(new TextMessage(text));
            }
        }
    }

    private String errorMsg(String message) {
        JSONObject obj = new JSONObject();
        obj.put("type", "error");
        obj.put("message", message);
        return obj.toJSONString();
    }
}
