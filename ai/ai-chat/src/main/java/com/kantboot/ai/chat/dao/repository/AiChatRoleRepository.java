package com.kantboot.ai.chat.dao.repository;

import com.kantboot.ai.chat.domain.entity.AiChatRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AiChatRoleRepository extends JpaRepository<AiChatRole, Long> {
    Optional<AiChatRole> findByCode(String code);
}
