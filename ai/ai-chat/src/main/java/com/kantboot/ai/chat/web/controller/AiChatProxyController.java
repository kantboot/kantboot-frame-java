package com.kantboot.ai.chat.web.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.ai.chat.dao.repository.AiChatModelRepository;
import com.kantboot.ai.chat.dao.repository.AiChatRolePresetsRepository;
import com.kantboot.ai.chat.dao.repository.AiChatRoleRepository;
import com.kantboot.ai.chat.domain.entity.AiChatModel;
import com.kantboot.ai.chat.domain.entity.AiChatRole;
import com.kantboot.ai.chat.domain.entity.AiChatRolePresets;
import com.kantboot.ai.chat.domain.vo.AiChatMessageAllVO;
import com.kantboot.ai.chat.exception.AiChatException;
import com.kantboot.ai.chat.method.AiChatMethod;
import com.kantboot.ai.chat.slot.AiChatSlot;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.http.HttpRequestHeaderUtil;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@AuthInit(name = "AI透传代理", description = "OpenAI兼容透传代理接口", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/ai-chat-web/proxy")
public class AiChatProxyController {

    @Resource
    private AiChatRoleRepository roleRepository;
    @Resource
    private AiChatModelRepository modelRepository;
    @Resource
    private AiChatRolePresetsRepository presetsRepository;
    @Resource
    private AiChatSlot chatSlot;
    @Resource
    private HttpRequestHeaderUtil httpRequestHeaderUtil;

    private static final String ROLE_ID_PREFIX = "role-id-";

    @AuthInit(name = "AI透传", description = "透传请求到AI，不存储记录", sourceLanguageCode = "zh_CN", allPass = true)
    @PostMapping("/{roleIdentifier}/chat/completions")
    public ResponseEntity<StreamingResponseBody> proxy(
            @PathVariable String roleIdentifier,
            @RequestBody String requestBodyStr
    ) {
        JSONObject requestBody = JSON.parseObject(requestBodyStr);

        AiChatRole role;
        if (roleIdentifier.startsWith(ROLE_ID_PREFIX)) {
            Long roleId = Long.parseLong(roleIdentifier.substring(ROLE_ID_PREFIX.length()));
            role = roleRepository.findById(roleId).orElseThrow(() -> AiChatException.ROLE_NOT_EXIST);
        } else {
            role = roleRepository.findByCode(roleIdentifier).orElseThrow(() -> AiChatException.ROLE_NOT_EXIST);
        }
        Long roleId = role.getId();

        Long modelId = requestBody.getLong("modelId");
        if (modelId == null) {
            modelId = role.getModelId();
        }
        AiChatModel model = modelRepository.findById(modelId)
                .orElseThrow(() -> AiChatException.MODEL_NOT_EXIST);

        String languageCode = httpRequestHeaderUtil.getLanguageCode();

        List<AiChatMessageAllVO> context = new ArrayList<>();
        List<AiChatRolePresets> presets =
                presetsRepository.findByRoleIdAndLanguageCodeOrderByPriorityAsc(roleId, languageCode);
        for (AiChatRolePresets preset : presets) {
            context.add(new AiChatMessageAllVO().setRole(preset.getRole()).setContent(preset.getContent()));
        }

        var messagesArr = requestBody.getJSONArray("messages");
        if (messagesArr != null) {
            for (int i = 0; i < messagesArr.size(); i++) {
                JSONObject m = messagesArr.getJSONObject(i);
                context.add(new AiChatMessageAllVO().setRole(m.getString("role")).setContent(m.getString("content")));
            }
        }

        boolean stream = Boolean.TRUE.equals(requestBody.getBoolean("stream"));
        AiChatModel finalModel = model;

        StreamingResponseBody body = outputStream -> {
            chatSlot.sendMessageHasStream(context, finalModel, new AiChatMethod() {
                @Override
                public void run(String text, String content, Boolean done) {
                    if (stream) {
                        JSONObject chunk = buildChunk(text, done ? "stop" : null);
                        String line = "data: " + chunk.toJSONString() + "\n\n";
                        try {
                            outputStream.write(line.getBytes(StandardCharsets.UTF_8));
                            outputStream.flush();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void finish(String content) {
                    try {
                        if (stream) {
                            outputStream.write("data: [DONE]\n\n".getBytes(StandardCharsets.UTF_8));
                        } else {
                            outputStream.write(buildResponse(content).toJSONString().getBytes(StandardCharsets.UTF_8));
                        }
                        outputStream.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        };

        MediaType contentType = stream ? MediaType.TEXT_EVENT_STREAM : MediaType.APPLICATION_JSON;
        return ResponseEntity.ok().contentType(contentType).body(body);
    }

    private JSONObject buildChunk(String text, String finishReason) {
        JSONObject delta = new JSONObject();
        delta.put("content", text);
        JSONObject choice = new JSONObject();
        choice.put("delta", delta);
        choice.put("finish_reason", finishReason);
        choice.put("index", 0);
        JSONObject chunk = new JSONObject();
        chunk.put("object", "chat.completion.chunk");
        chunk.put("choices", List.of(choice));
        return chunk;
    }

    private JSONObject buildResponse(String content) {
        JSONObject message = new JSONObject();
        message.put("role", "assistant");
        message.put("content", content);
        JSONObject choice = new JSONObject();
        choice.put("message", message);
        choice.put("finish_reason", "stop");
        choice.put("index", 0);
        JSONObject resp = new JSONObject();
        resp.put("object", "chat.completion");
        resp.put("choices", List.of(choice));
        return resp;
    }
}
