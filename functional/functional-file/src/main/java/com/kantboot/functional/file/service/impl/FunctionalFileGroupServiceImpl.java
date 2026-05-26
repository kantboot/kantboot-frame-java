package com.kantboot.functional.file.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kantboot.functional.file.dao.repository.FunctionalFileGroupRepository;
import com.kantboot.functional.file.domain.entity.FunctionalFileGroup;
import com.kantboot.functional.file.exception.FunctionalFileException;
import com.kantboot.functional.file.service.IFunctionalFileGroupService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 文件组管理的Service接口实现类
 * @author FangMoFang
 */
@Service
public class FunctionalFileGroupServiceImpl
    implements IFunctionalFileGroupService {

    @Resource
    private FunctionalFileGroupRepository repository;

    private final static Cache<String, Object> CACHE = Caffeine.newBuilder()
            .expireAfterWrite(7, TimeUnit.DAYS)
            .build();

    @Override
    public FunctionalFileGroup getByCode(String code) {
        Object ifPresent = CACHE.getIfPresent("FunctionalFileGroup:"+code);
        if(ifPresent != null){
            return (FunctionalFileGroup) ifPresent;
        }
        FunctionalFileGroup byCode = repository.findByCode(code);
        if(byCode == null) {
            return null;
        }
        CACHE.put("FunctionalFileGroup:"+code, byCode);
        return byCode;
    }

    @Override
    public String getPathByCode(String code) {
        return getByCode(code).getPath();
    }

    @Override
    public FunctionalFileGroup save(FunctionalFileGroup group) {
        FunctionalFileGroup existingGroup = getByCode(group.getCode());
        if (existingGroup != null) {
            // 更新已有的文件组
            BeanUtil.copyProperties(group, existingGroup, true);
            existingGroup = repository.save(existingGroup);
            CACHE.put(group.getCode(), existingGroup);
            return existingGroup;
        } else {
            // 保存新的文件组
            FunctionalFileGroup savedGroup = repository.save(group);
            CACHE.put(savedGroup.getCode(), savedGroup);
            return savedGroup;
        }
    }

}