package com.kantboot.ai.agent.dao.repository;

import com.kantboot.ai.agent.domain.entity.AiAgent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiAgentRepository extends JpaRepository<AiAgent, Long> {
}
