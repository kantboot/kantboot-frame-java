package com.kantboot.ai.chat.dao.repository;

import com.kantboot.ai.chat.domain.entity.AiChatRoleLanguageSupport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiChatRoleLanguageSupportRepository extends JpaRepository<AiChatRoleLanguageSupport, Long> {
    List<AiChatRoleLanguageSupport> findByRoleId(Long roleId);
}
