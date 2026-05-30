package com.kantboot.ai.agent.dao.repository;

import com.kantboot.ai.agent.domain.entity.AiAgentStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiAgentStepRepository extends JpaRepository<AiAgentStep, Long> {
    List<AiAgentStep> findBySessionIdOrderByStepIndexAsc(Long sessionId);
}
