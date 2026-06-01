package com.kantboot.ai.agent.service.impl;

import com.kantboot.ai.agent.dao.repository.AiAgentRepository;
import com.kantboot.ai.agent.domain.dto.AiAgentCreateDTO;
import com.kantboot.ai.agent.domain.entity.AiAgent;
import com.kantboot.ai.agent.exception.AiAgentException;
import com.kantboot.ai.agent.service.IAiAgentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiAgentServiceImpl implements IAiAgentService {

    @Resource
    private AiAgentRepository agentRepository;

    @Override
    public AiAgent create(AiAgentCreateDTO dto) {
        AiAgent agent = new AiAgent()
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setExecutorType(dto.getExecutorType())
                .setSshHost(dto.getSshHost())
                .setSshPort(dto.getSshPort())
                .setSshUser(dto.getSshUser())
                .setSshPassword(dto.getSshPassword())
                .setSshWorkDir(dto.getSshWorkDir())
                .setLocalWorkDir(dto.getLocalWorkDir())
                .setModelId(dto.getModelId())
                .setRoleId(dto.getRoleId())
                .setPermissionMode(dto.getPermissionMode())
                .setAllowList(dto.getAllowList())
                .setBlockList(dto.getBlockList());
        if (dto.getMaxSteps() != null) {
            agent.setMaxSteps(dto.getMaxSteps());
        }
        return agentRepository.save(agent);
    }

    @Override
    public AiAgent update(Long id, AiAgentCreateDTO dto) {
        AiAgent agent = agentRepository.findById(id)
                .orElseThrow(() -> AiAgentException.AGENT_NOT_EXIST);
        agent.setName(dto.getName())
                .setDescription(dto.getDescription())
                .setExecutorType(dto.getExecutorType())
                .setSshHost(dto.getSshHost())
                .setSshPort(dto.getSshPort())
                .setSshUser(dto.getSshUser())
                .setSshPassword(dto.getSshPassword())
                .setSshWorkDir(dto.getSshWorkDir())
                .setLocalWorkDir(dto.getLocalWorkDir())
                .setModelId(dto.getModelId())
                .setRoleId(dto.getRoleId())
                .setPermissionMode(dto.getPermissionMode())
                .setAllowList(dto.getAllowList())
                .setBlockList(dto.getBlockList());
        if (dto.getMaxSteps() != null) {
            agent.setMaxSteps(dto.getMaxSteps());
        }
        return agentRepository.save(agent);
    }

    @Override
    public void deleteById(Long id) {
        agentRepository.deleteById(id);
    }

    @Override
    public AiAgent getById(Long id) {
        return agentRepository.findById(id).orElseThrow(() -> AiAgentException.AGENT_NOT_EXIST);
    }

    @Override
    public List<AiAgent> getAll() {
        return agentRepository.findAll();
    }
}
