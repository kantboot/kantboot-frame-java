package com.kantboot.ai.chat.service;

import com.kantboot.ai.chat.domain.entity.AiChatRole;
import java.util.List;

public interface IAiChatRoleService {
    AiChatRole save(AiChatRole role);
    AiChatRole getById(Long id);
    List<AiChatRole> getAll();
    void deleteById(Long id);
}
