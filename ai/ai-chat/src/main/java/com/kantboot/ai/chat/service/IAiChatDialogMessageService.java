package com.kantboot.ai.chat.service;

import com.kantboot.ai.chat.domain.entity.AiChatDialogMessage;
import java.util.List;

public interface IAiChatDialogMessageService {
    List<AiChatDialogMessage> getByDialogId(Long dialogId);
}
