package com.kantboot.ai.agent.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.ai.agent.constants.AiAgentPermissionModeConstants;
import com.kantboot.ai.agent.constants.AiAgentSessionStatusConstants;
import com.kantboot.ai.agent.constants.AiAgentStepStatusConstants;
import com.kantboot.ai.agent.dao.repository.AiAgentRepository;
import com.kantboot.ai.agent.dao.repository.AiAgentSessionRepository;
import com.kantboot.ai.agent.dao.repository.AiAgentStepRepository;
import com.kantboot.ai.agent.domain.dto.AiAgentRunDTO;
import com.kantboot.ai.agent.domain.entity.AiAgent;
import com.kantboot.ai.agent.domain.entity.AiAgentSession;
import com.kantboot.ai.agent.domain.entity.AiAgentStep;
import com.kantboot.ai.agent.domain.vo.AiAgentStepVO;
import com.kantboot.ai.agent.exception.AiAgentException;
import com.kantboot.ai.agent.executor.AiAgentExecutor;
import com.kantboot.ai.agent.service.IAiAgentSessionService;
import com.kantboot.ai.agent.slot.AiAgentExecutorSlot;
import com.kantboot.ai.chat.dao.repository.AiChatModelRepository;
import com.kantboot.ai.chat.dao.repository.AiChatRolePresetsRepository;
import com.kantboot.ai.chat.domain.entity.AiChatModel;
import com.kantboot.ai.chat.domain.entity.AiChatRolePresets;
import com.kantboot.ai.chat.domain.vo.AiChatMessageAllVO;
import com.kantboot.ai.chat.method.AiChatMethod;
import com.kantboot.ai.chat.slot.AiChatSlot;
import com.kantboot.user.account.service.IUserAccountService;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AiAgentSessionServiceImpl implements IAiAgentSessionService {

    private static final Pattern JSON_BLOCK = Pattern.compile("```(?:json)?\\s*(\\{[\\s\\S]*?})\\s*```|^(\\{[\\s\\S]*})$", Pattern.MULTILINE);

    private static final String SYSTEM_PROMPT = """
            You are an AI agent that can execute shell commands to accomplish tasks.
            Each response MUST be a single valid JSON object with exactly these fields:
            - "thought": string - your reasoning about the next step
            - "command": string - the shell command to execute (empty string if done)
            - "done": boolean - true when the task is fully complete

            Example responses:
            {"thought": "I need to check the directory structure first.", "command": "ls -la", "done": false}
            {"thought": "The task is complete. The file has been created successfully.", "command": "", "done": true}

            Rules:
            - ALWAYS respond with a single JSON object and nothing else
            - Do NOT wrap in markdown code blocks
            - If a command fails, analyze the error output and try a different approach
            - When done is true, command must be empty string
            """;

    @Resource
    private AiAgentRepository agentRepository;
    @Resource
    private AiAgentSessionRepository sessionRepository;
    @Resource
    private AiAgentStepRepository stepRepository;
    @Resource
    private AiChatModelRepository modelRepository;
    @Resource
    private AiChatRolePresetsRepository presetsRepository;
    @Resource
    private AiChatSlot chatSlot;
    @Resource
    private AiAgentExecutorSlot executorSlot;
    @Resource
    private IUserAccountService userAccountService;
    @Resource
    private AiAgentApprovalCoordinator approvalCoordinator;

    @Override
    public ResponseEntity<StreamingResponseBody> runOfStream(AiAgentRunDTO dto) {
        AiAgent agent = loadAgent(dto.getAgentId());
        if (AiAgentPermissionModeConstants.ASK.equals(agent.getPermissionMode())) {
            throw AiAgentException.ASK_MODE_SSE_NOT_SUPPORTED;
        }

        Long userAccountId = userAccountService.getSelfId();
        AiAgentSession session = createSession(agent, dto.getTask(), userAccountId);

        StreamingResponseBody body = outputStream -> {
            Consumer<AiAgentStepVO> push = step -> writeToStream(outputStream, step);
            runLoop(agent, session, dto.getTask(), push);
        };

        return ResponseEntity.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(body);
    }

    @Override
    public AiAgentSession runAsync(AiAgentRunDTO dto, Consumer<AiAgentStepVO> pushCallback) {
        AiAgent agent = loadAgent(dto.getAgentId());
        Long userAccountId = userAccountService.getSelfId();
        AiAgentSession session = createSession(agent, dto.getTask(), userAccountId);

        ThreadUtil.execute(() -> runLoop(agent, session, dto.getTask(), pushCallback));

        return session;
    }

    @Override
    public void approve(Long sessionId) {
        approvalCoordinator.approve(sessionId);
    }

    @Override
    public void reject(Long sessionId) {
        approvalCoordinator.reject(sessionId);
    }

    @Override
    public List<AiAgentSession> getSessionsBySelf() {
        return sessionRepository.findByUserAccountIdOrderByGmtModifiedDesc(userAccountService.getSelfId());
    }

    @Override
    public List<AiAgentStep> getStepsBySessionId(Long sessionId) {
        return stepRepository.findBySessionIdOrderByStepIndexAsc(sessionId);
    }

    // -------------------------------------------------------

    private void runLoop(AiAgent agent, AiAgentSession session, String task, Consumer<AiAgentStepVO> push) {
        AiChatModel model = modelRepository.findById(agent.getModelId())
                .orElseThrow(() -> AiAgentException.MODEL_NOT_EXIST);

        List<AiChatMessageAllVO> messages = buildInitialMessages(agent, task);

        try (AiAgentExecutor executor = executorSlot.create(agent)) {
            int maxSteps = agent.getMaxSteps() != null ? agent.getMaxSteps() : 120;

            for (int i = 0; i < maxSteps; i++) {
                // 1. 调用 AI
                StringBuilder aiResponse = new StringBuilder();
                chatSlot.sendMessageHasStream(messages, model, new AiChatMethod() {
                    @Override
                    public void run(String text, String content, Boolean done) {}
                    @Override
                    public void finish(String content) {
                        aiResponse.append(content);
                    }
                });

                // 2. 解析响应
                JSONObject parsed = parseAiResponse(aiResponse.toString());
                String thought = parsed.getString("thought");
                String command = parsed.getString("command");
                boolean done = Boolean.TRUE.equals(parsed.getBoolean("done"));

                // 3. 保存步骤
                AiAgentStep step = stepRepository.save(new AiAgentStep()
                        .setSessionId(session.getId())
                        .setStepIndex(i)
                        .setThought(thought)
                        .setCommand(command)
                        .setStatus(done ? AiAgentStepStatusConstants.DONE : AiAgentStepStatusConstants.THINKING));

                if (done) {
                    push.accept(buildVO(session, step, null, true));
                    finishSession(session, i + 1, AiAgentSessionStatusConstants.FINISHED);
                    return;
                }

                // 4. 权限检查
                boolean allowed = checkPermission(agent, session, command, step, push);
                if (!allowed) {
                    step.setStatus(AiAgentStepStatusConstants.REJECTED);
                    stepRepository.save(step);
                    push.accept(buildVO(session, step, "Command rejected by permission rules.", false));
                    finishSession(session, i + 1, AiAgentSessionStatusConstants.INTERRUPTED);
                    return;
                }

                // 5. 执行命令
                step.setStatus(AiAgentStepStatusConstants.EXECUTING);
                stepRepository.save(step);
                push.accept(buildVO(session, step, null, false));

                String output;
                try {
                    output = executor.execute(command);
                } catch (Exception e) {
                    output = "ERROR: " + e.getMessage();
                    step.setStatus(AiAgentStepStatusConstants.ERROR);
                } finally {
                    step.setStatus(AiAgentStepStatusConstants.DONE);
                }

                step.setOutput(output).setStatus(AiAgentStepStatusConstants.DONE);
                stepRepository.save(step);
                push.accept(buildVO(session, step, output, false));

                // 6. 把结果追加到上下文
                messages.add(new AiChatMessageAllVO().setRole("assistant").setContent(aiResponse.toString()));
                messages.add(new AiChatMessageAllVO().setRole("user")
                        .setContent("Command output:\n" + output + "\n\nContinue with the task."));
            }

            // 超出最大步数
            finishSession(session, maxSteps, AiAgentSessionStatusConstants.INTERRUPTED);
            AiAgentStepVO limitVO = new AiAgentStepVO()
                    .setSessionId(session.getId())
                    .setDone(true)
                    .setErrorMessage("Max steps (" + maxSteps + ") reached.");
            push.accept(limitVO);

        } catch (Exception e) {
            finishSession(session, -1, AiAgentSessionStatusConstants.ERROR);
            AiAgentStepVO errVO = new AiAgentStepVO()
                    .setSessionId(session.getId())
                    .setDone(true)
                    .setErrorMessage(e.getMessage());
            push.accept(errVO);
        }
    }

    private boolean checkPermission(AiAgent agent, AiAgentSession session, String command,
                                    AiAgentStep step, Consumer<AiAgentStepVO> push) {
        return switch (agent.getPermissionMode()) {
            case AiAgentPermissionModeConstants.AUTO -> true;
            case AiAgentPermissionModeConstants.WHITELIST -> {
                List<String> allow = agent.getAllowList();
                yield allow != null && allow.stream().anyMatch(p -> matchPattern(command, p));
            }
            case AiAgentPermissionModeConstants.BLACKLIST -> {
                List<String> block = agent.getBlockList();
                yield block == null || block.stream().noneMatch(p -> matchPattern(command, p));
            }
            case AiAgentPermissionModeConstants.ASK -> {
                step.setStatus(AiAgentStepStatusConstants.WAITING_APPROVAL);
                stepRepository.save(step);
                session.setStatus(AiAgentSessionStatusConstants.WAITING_APPROVAL);
                sessionRepository.save(session);
                push.accept(buildVO(session, step, null, false));
                boolean approved = approvalCoordinator.awaitApproval(session.getId(), 300);
                session.setStatus(AiAgentSessionStatusConstants.RUNNING);
                sessionRepository.save(session);
                yield approved;
            }
            case AiAgentPermissionModeConstants.PLAN -> true;
            default -> false;
        };
    }

    private boolean matchPattern(String command, String pattern) {
        if (command.startsWith(pattern)) return true;
        try {
            return command.matches(pattern);
        } catch (Exception e) {
            return false;
        }
    }

    private List<AiChatMessageAllVO> buildInitialMessages(AiAgent agent, String task) {
        List<AiChatMessageAllVO> messages = new ArrayList<>();
        messages.add(new AiChatMessageAllVO().setRole("system").setContent(SYSTEM_PROMPT));

        if (agent.getRoleId() != null) {
            List<AiChatRolePresets> presets = presetsRepository
                    .findByRoleIdAndLanguageCodeOrderByPriorityAsc(agent.getRoleId(), "zh_CN");
            for (AiChatRolePresets preset : presets) {
                messages.add(new AiChatMessageAllVO().setRole(preset.getRole()).setContent(preset.getContent()));
            }
        }

        messages.add(new AiChatMessageAllVO().setRole("user").setContent("Task: " + task));
        return messages;
    }

    private JSONObject parseAiResponse(String raw) {
        String text = raw.trim();

        // 尝试直接解析
        try {
            JSONObject obj = JSON.parseObject(text);
            if (obj != null && obj.containsKey("thought")) return obj;
        } catch (Exception ignored) {}

        // 尝试从 markdown 代码块提取
        Matcher m = JSON_BLOCK.matcher(text);
        while (m.find()) {
            String candidate = m.group(1) != null ? m.group(1) : m.group(2);
            if (candidate == null) continue;
            try {
                JSONObject obj = JSON.parseObject(candidate.trim());
                if (obj != null && obj.containsKey("thought")) return obj;
            } catch (Exception ignored) {}
        }

        // fallback：包装成 done=true 的对象避免崩溃
        JSONObject fallback = new JSONObject();
        fallback.put("thought", raw);
        fallback.put("command", "");
        fallback.put("done", true);
        return fallback;
    }

    private AiAgentSession createSession(AiAgent agent, String task, Long userAccountId) {
        return sessionRepository.save(new AiAgentSession()
                .setAgentId(agent.getId())
                .setUserAccountId(userAccountId)
                .setTask(task)
                .setStatus(AiAgentSessionStatusConstants.RUNNING));
    }

    private void finishSession(AiAgentSession session, int totalSteps, String status) {
        session.setStatus(status);
        if (totalSteps >= 0) session.setTotalSteps(totalSteps);
        sessionRepository.save(session);
    }

    private AiAgent loadAgent(Long agentId) {
        return agentRepository.findById(agentId).orElseThrow(() -> AiAgentException.AGENT_NOT_EXIST);
    }

    private AiAgentStepVO buildVO(AiAgentSession session, AiAgentStep step, String output, boolean done) {
        return new AiAgentStepVO()
                .setSessionId(session.getId())
                .setStepId(step.getId())
                .setStepIndex(step.getStepIndex())
                .setThought(step.getThought())
                .setCommand(step.getCommand())
                .setOutput(output != null ? output : step.getOutput())
                .setStatus(step.getStatus())
                .setDone(done);
    }

    private void writeToStream(OutputStream out, AiAgentStepVO vo) {
        try {
            String line = "data: " + JSON.toJSONString(vo) + "\n\n";
            out.write(line.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
