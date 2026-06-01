package com.kantboot.ai.chat.dao.repository;

import com.kantboot.ai.chat.domain.entity.AiChatRolePresets;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiChatRolePresetsRepository extends JpaRepository<AiChatRolePresets, Long> {
    List<AiChatRolePresets> findByRoleIdAndLanguageCodeOrderByPriorityAsc(Long roleId, String languageCode);
}
