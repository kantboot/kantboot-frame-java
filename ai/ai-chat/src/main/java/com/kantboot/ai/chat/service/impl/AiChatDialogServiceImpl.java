package com.kantboot.ai.chat.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson2.JSON;
import com.kantboot.ai.chat.constants.AiChatDialogMessageStatusCodeConstants;
import com.kantboot.ai.chat.dao.repository.AiChatDialogMessageRepository;
import com.kantboot.ai.chat.dao.repository.AiChatDialogRepository;
import com.kantboot.ai.chat.dao.repository.AiChatModelRepository;
import com.kantboot.ai.chat.dao.repository.AiChatRolePresetsRepository;
import com.kantboot.ai.chat.dao.repository.AiChatRoleRepository;
import com.kantboot.ai.chat.domain.dto.AiChatCreateDialogDTO;
import com.kantboot.ai.chat.domain.dto.AiChatSendMessageDTO;
import com.kantboot.ai.chat.domain.entity.AiChatDialog;
import com.kantboot.ai.chat.domain.entity.AiChatDialogMessage;
import com.kantboot.ai.chat.domain.entity.AiChatModel;
import com.kantboot.ai.chat.domain.entity.AiChatRole;
import com.kantboot.ai.chat.domain.entity.AiChatRolePresets;
import com.kantboot.ai.chat.domain.vo.AiChatMessageAllVO;
import com.kantboot.ai.chat.domain.vo.AiChatTransmissionVO;
import com.kantboot.ai.chat.exception.AiChatException;
import com.kantboot.ai.chat.method.AiChatMethod;
import com.kantboot.ai.chat.service.IAiChatDialogService;
import com.kantboot.ai.chat.slot.AiChatSlot;
import com.kantboot.ai.chat.slot.AiMessagePushSlot;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.util.cache.CacheUtil;
import com.kantboot.util.http.HttpRequestHeaderUtil;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AiChatDialogServiceImpl implements IAiChatDialogService {

    @Resource
    private AiChatDialogRepository dialogRepository;
    @Resource
    private AiChatDialogMessageRepository messageRepository;
    @Resource
    private AiChatRoleRepository roleRepository;
    @Resource
    private AiChatModelRepository modelRepository;
    @Resource
    private AiChatRolePresetsRepository presetsRepository;
    @Resource
    private AiChatSlot chatSlot;
    @Resource
    private AiMessagePushSlot messagePushSlot;
    @Resource
    private IUserAccountService userAccountService;
    @Resource
    private CacheUtil cacheUtil;
    @Resource
    private HttpRequestHeaderUtil httpRequestHeaderUtil;

    @Override
    public AiChatDialog createDialog(AiChatCreateDialogDTO dto) {
        AiChatRole role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> AiChatException.ROLE_NOT_EXIST);

        String rawLanguageCode = dto.getLanguageCode();
        String languageCode = (rawLanguageCode == null || rawLanguageCode.isBlank())
                ? httpRequestHeaderUtil.getLanguageCode()
                : rawLanguageCode;

        // boolean supported = role.getLanguageSupports().stream()
        //         .anyMatch(s -> s.getLanguageCode().equals(languageCode));
        // if (!supported) {
        //     throw AiChatException.LANGUAGE_NOT_SUPPORTED;
        // }

        AiChatDialog dialog = new AiChatDialog()
                .setRoleId(dto.getRoleId())
                .setModelId(dto.getModelId())
                .setUserAccountId(userAccountService.getSelfId())
                .setLanguageCode(languageCode);

        return dialogRepository.save(dialog);
    }

    @Override
    public void sendMessage(AiChatSendMessageDTO dto) {
        lock(dto.getDialogId());

        Long userAccountId = userAccountService.getSelfId();

        AiChatDialog dialog = dialogRepository.findById(dto.getDialogId())
                .orElseThrow(() -> AiChatException.DIALOG_NOT_EXIST);

        AiChatModel model = resolveModel(dialog);
        List<AiChatMessageAllVO> context = buildContext(dialog, dto.getContent());

        saveUserMessage(dto, userAccountId);
        AiChatDialogMessage assistantMsg = saveAssistantPlaceholder(dto.getDialogId());
        updateDialogTimestamp(dialog);

        ThreadUtil.execute(() -> {
            chatSlot.sendMessageHasStream(context, model, new AiChatMethod() {
                @Override
                public void run(String text, String content, Boolean done) {
                    messagePushSlot.push(userAccountId, buildTransmissionVO(dialog, assistantMsg, text, content, done));
                }

                @Override
                public void finish(String content) {
                    assistantMsg.setContent(content)
                            .setStatusCode(AiChatDialogMessageStatusCodeConstants.FINISH);
                    messageRepository.save(assistantMsg);
                    unlock(dto.getDialogId());
                }
            });
        });
    }

    @Override
    public ResponseEntity<StreamingResponseBody> sendMessageOfStream(AiChatSendMessageDTO dto) {
        lock(dto.getDialogId());

        Long userAccountId = userAccountService.getSelfId();

        AiChatDialog dialog = dialogRepository.findById(dto.getDialogId())
                .orElseThrow(() -> AiChatException.DIALOG_NOT_EXIST);

        AiChatModel model = resolveModel(dialog);
        List<AiChatMessageAllVO> context = buildContext(dialog, dto.getContent());

        saveUserMessage(dto, userAccountId);
        AiChatDialogMessage assistantMsg = saveAssistantPlaceholder(dto.getDialogId());
        updateDialogTimestamp(dialog);

        StreamingResponseBody body = outputStream -> {
            try {
                chatSlot.sendMessageHasStream(context, model, new AiChatMethod() {
                    @Override
                    public void run(String text, String content, Boolean done) {
                        String line = "data: " + JSON.toJSONString(buildTransmissionVO(dialog, assistantMsg, text, content, done)) + "\n\n";
                        try {
                            outputStream.write(line.getBytes(StandardCharsets.UTF_8));
                            outputStream.flush();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void finish(String content) {
                        assistantMsg.setContent(content)
                                .setStatusCode(AiChatDialogMessageStatusCodeConstants.FINISH);
                        messageRepository.save(assistantMsg);
                        unlock(dto.getDialogId());
                    }
                });
            } catch (Exception e) {
                assistantMsg.setStatusCode(AiChatDialogMessageStatusCodeConstants.ERROR);
                messageRepository.save(assistantMsg);
                unlock(dto.getDialogId());
                throw e;
            }
        };

        return ResponseEntity.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(body);
    }

    @Override
    public List<AiChatDialog> getBySelf() {
        return dialogRepository.findByUserAccountIdOrderByGmtModifiedDesc(userAccountService.getSelfId());
    }

    @Override
    public AiChatDialog getById(Long id) {
        return dialogRepository.findById(id).orElseThrow(() -> AiChatException.DIALOG_NOT_EXIST);
    }

    @Override
    public void deleteById(Long id) {
        dialogRepository.deleteById(id);
    }

    private AiChatModel resolveModel(AiChatDialog dialog) {
        Long modelId = dialog.getModelId();
        if (modelId == null) {
            AiChatRole role = roleRepository.findById(dialog.getRoleId())
                    .orElseThrow(() -> AiChatException.ROLE_NOT_EXIST);
            modelId = role.getModelId();
        }
        return modelRepository.findById(modelId).orElseThrow(() -> AiChatException.MODEL_NOT_EXIST);
    }

    private List<AiChatMessageAllVO> buildContext(AiChatDialog dialog, String newUserContent) {
        List<AiChatMessageAllVO> context = new ArrayList<>();

        List<AiChatRolePresets> presets =
                presetsRepository.findByRoleIdAndLanguageCodeOrderByPriorityAsc(dialog.getRoleId(), dialog.getLanguageCode());
        for (AiChatRolePresets preset : presets) {
            context.add(new AiChatMessageAllVO().setRole(preset.getRole()).setContent(preset.getContent()));
        }

        List<AiChatDialogMessage> history = messageRepository.findByDialogIdOrderByIdAsc(dialog.getId());
        for (AiChatDialogMessage msg : history) {
            if (AiChatDialogMessageStatusCodeConstants.FINISH.equals(msg.getStatusCode())
                    || "user".equals(msg.getRole())) {
                context.add(new AiChatMessageAllVO().setRole(msg.getRole()).setContent(msg.getContent()));
            }
        }

        context.add(new AiChatMessageAllVO().setRole("user").setContent(newUserContent));
        return context;
    }

    private AiChatDialogMessage saveUserMessage(AiChatSendMessageDTO dto, Long userAccountId) {
        return messageRepository.save(new AiChatDialogMessage()
                .setDialogId(dto.getDialogId())
                .setRole("user")
                .setContent(dto.getContent())
                .setStatusCode(AiChatDialogMessageStatusCodeConstants.FINISH)
                .setUserAccountId(userAccountId));
    }

    private AiChatDialogMessage saveAssistantPlaceholder(Long dialogId) {
        return messageRepository.save(new AiChatDialogMessage()
                .setDialogId(dialogId)
                .setRole("assistant")
                .setContent("")
                .setStatusCode(AiChatDialogMessageStatusCodeConstants.THINKING));
    }

    private void updateDialogTimestamp(AiChatDialog dialog) {
        dialog.setGmtModified(new Date());
        dialogRepository.save(dialog);
    }

    private AiChatTransmissionVO buildTransmissionVO(AiChatDialog dialog, AiChatDialogMessage assistantMsg,
                                                      String text, String content, Boolean done) {
        return new AiChatTransmissionVO()
                .setDialogId(dialog.getId())
                .setMessageId(assistantMsg.getId())
                .setRole("assistant")
                .setText(text)
                .setContent(content)
                .setDone(done);
    }

    private void lock(Long dialogId) {
        if (!cacheUtil.lock("aiChat:sendMessage:" + dialogId, 10, TimeUnit.MINUTES)) {
            throw AiChatException.DIALOG_LOCKED;
        }
    }

    private void unlock(Long dialogId) {
        cacheUtil.unlock("aiChat:sendMessage:" + dialogId);
    }
}
