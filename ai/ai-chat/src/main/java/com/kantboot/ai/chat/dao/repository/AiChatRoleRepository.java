package com.kantboot.ai.chat.dao.repository;

import com.kantboot.ai.chat.domain.entity.AiChatRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiChatRoleRepository extends JpaRepository<AiChatRole, Long> {}
