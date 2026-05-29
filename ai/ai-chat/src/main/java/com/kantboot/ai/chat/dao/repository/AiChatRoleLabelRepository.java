package com.kantboot.ai.chat.dao.repository;

import com.kantboot.ai.chat.domain.entity.AiChatRoleLabel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiChatRoleLabelRepository extends JpaRepository<AiChatRoleLabel, Long> {
    List<AiChatRoleLabel> findByRoleId(Long roleId);
}
