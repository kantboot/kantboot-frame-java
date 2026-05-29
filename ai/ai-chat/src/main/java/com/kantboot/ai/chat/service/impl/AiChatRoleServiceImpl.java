package com.kantboot.ai.chat.service.impl;

import com.kantboot.ai.chat.dao.repository.AiChatRoleRepository;
import com.kantboot.ai.chat.domain.entity.AiChatRole;
import com.kantboot.ai.chat.exception.AiChatException;
import com.kantboot.ai.chat.service.IAiChatRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AiChatRoleServiceImpl implements IAiChatRoleService {

    @Resource
    private AiChatRoleRepository repository;

    @Override
    public AiChatRole save(AiChatRole role) {
        return repository.save(role);
    }

    @Override
    public AiChatRole getById(Long id) {
        return repository.findById(id).orElseThrow(() -> AiChatException.ROLE_NOT_EXIST);
    }

    @Override
    public List<AiChatRole> getAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
