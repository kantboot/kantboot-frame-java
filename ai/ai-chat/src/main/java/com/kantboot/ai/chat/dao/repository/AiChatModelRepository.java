package com.kantboot.ai.chat.dao.repository;

import com.kantboot.ai.chat.domain.entity.AiChatModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiChatModelRepository extends JpaRepository<AiChatModel, Long> {}
