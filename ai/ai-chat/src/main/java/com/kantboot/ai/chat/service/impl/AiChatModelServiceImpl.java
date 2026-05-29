package com.kantboot.ai.chat.service.impl;

import com.kantboot.ai.chat.dao.repository.AiChatModelRepository;
import com.kantboot.ai.chat.domain.entity.AiChatModel;
import com.kantboot.ai.chat.exception.AiChatException;
import com.kantboot.ai.chat.service.IAiChatModelService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AiChatModelServiceImpl implements IAiChatModelService {

    @Resource
    private AiChatModelRepository repository;

    @Override
    public AiChatModel save(AiChatModel model) {
        return repository.save(model);
    }

    @Override
    public AiChatModel getById(Long id) {
        return repository.findById(id).orElseThrow(() -> AiChatException.MODEL_NOT_EXIST);
    }

    @Override
    public List<AiChatModel> getAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
