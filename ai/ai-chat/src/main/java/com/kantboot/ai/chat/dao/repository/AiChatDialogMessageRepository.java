package com.kantboot.ai.chat.dao.repository;

import com.kantboot.ai.chat.domain.entity.AiChatDialogMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiChatDialogMessageRepository extends JpaRepository<AiChatDialogMessage, Long> {
    List<AiChatDialogMessage> findByDialogIdOrderByIdAsc(Long dialogId);
}
