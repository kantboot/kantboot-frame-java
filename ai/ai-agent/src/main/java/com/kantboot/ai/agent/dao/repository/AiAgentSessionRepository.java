package com.kantboot.ai.agent.dao.repository;

import com.kantboot.ai.agent.domain.entity.AiAgentSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiAgentSessionRepository extends JpaRepository<AiAgentSession, Long> {
    List<AiAgentSession> findByUserAccountIdOrderByGmtModifiedDesc(Long userAccountId);
    List<AiAgentSession> findByAgentIdOrderByGmtModifiedDesc(Long agentId);
}
