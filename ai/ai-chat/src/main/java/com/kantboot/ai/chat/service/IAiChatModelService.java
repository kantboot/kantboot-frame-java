package com.kantboot.ai.chat.service;

import com.kantboot.ai.chat.domain.entity.AiChatModel;
import java.util.List;

public interface IAiChatModelService {
    AiChatModel save(AiChatModel model);
    AiChatModel getById(Long id);
    List<AiChatModel> getAll();
    void deleteById(Long id);
}
