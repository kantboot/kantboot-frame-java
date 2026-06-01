package com.kantboot.ai.chat.service.impl;

import com.kantboot.ai.chat.dao.repository.AiChatDialogMessageRepository;
import com.kantboot.ai.chat.domain.entity.AiChatDialogMessage;
import com.kantboot.ai.chat.service.IAiChatDialogMessageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AiChatDialogMessageServiceImpl implements IAiChatDialogMessageService {

    @Resource
    private AiChatDialogMessageRepository repository;

    @Override
    public List<AiChatDialogMessage> getByDialogId(Long dialogId) {
        return repository.findByDialogIdOrderByIdAsc(dialogId);
    }
}
