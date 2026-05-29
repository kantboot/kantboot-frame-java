package com.kantboot.ai.chat.service;

import com.kantboot.ai.chat.domain.dto.AiChatCreateDialogDTO;
import com.kantboot.ai.chat.domain.dto.AiChatSendMessageDTO;
import com.kantboot.ai.chat.domain.entity.AiChatDialog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import java.util.List;

public interface IAiChatDialogService {
    AiChatDialog createDialog(AiChatCreateDialogDTO dto);
    void sendMessage(AiChatSendMessageDTO dto);
    ResponseEntity<StreamingResponseBody> sendMessageOfStream(AiChatSendMessageDTO dto);
    List<AiChatDialog> getBySelf();
    AiChatDialog getById(Long id);
    void deleteById(Long id);
}
