package com.kantboot.functional.icon.service.impl;

import com.kantboot.functional.icon.dao.repository.FunctionalIconGroupRepository;
import com.kantboot.functional.icon.domain.entity.FunctionalIconGroup;
import com.kantboot.functional.icon.service.IFunctionalIconGroupService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FunctionalIconGroupServiceImpl
    implements IFunctionalIconGroupService {

    @Resource
    private FunctionalIconGroupRepository repository;

    @Override
    public List<FunctionalIconGroup> getAll() {
        return repository.findAll();
    }
}
