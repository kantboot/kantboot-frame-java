package com.kantboot.ai.chat.dao.repository;

import com.kantboot.ai.chat.domain.entity.AiChatDialog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiChatDialogRepository extends JpaRepository<AiChatDialog, Long> {
    List<AiChatDialog> findByUserAccountIdOrderByGmtModifiedDesc(Long userAccountId);
}
