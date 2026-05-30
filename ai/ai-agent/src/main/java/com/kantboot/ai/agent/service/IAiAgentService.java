package com.kantboot.ai.agent.service;

import com.kantboot.ai.agent.domain.dto.AiAgentCreateDTO;
import com.kantboot.ai.agent.domain.entity.AiAgent;

import java.util.List;

public interface IAiAgentService {
    AiAgent create(AiAgentCreateDTO dto);
    AiAgent update(Long id, AiAgentCreateDTO dto);
    void deleteById(Long id);
    AiAgent getById(Long id);
    List<AiAgent> getAll();
}
