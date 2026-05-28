package com.kantboot.system.language.service.impl;

import com.kantboot.system.language.dao.repository.SysLanguageLocalizedRepository;
import com.kantboot.system.language.dao.repository.SysLanguageRepository;
import com.kantboot.system.language.domain.entity.SysLanguage;
import com.kantboot.system.language.domain.entity.SysLanguageLocalized;
import com.kantboot.system.language.service.ISysLanguageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysLanguageServiceImpl implements ISysLanguageService {

    @Resource
    private SysLanguageRepository repository;

    @Resource
    private SysLanguageLocalizedRepository localizedRepository;

    @Override
    public List<SysLanguage> getBySupport() {
        return repository.findBySupportTrue();
    }

    @Override
    public List<SysLanguageLocalized> getLocalizedList() {
        return localizedRepository.findAll();
    }

    @Override
    public String localizedCodeToLanguageCode(String code) {
        SysLanguageLocalized localized = localizedRepository.findByCode(code);
        if(localized==null){
            return "en";
        }
        return localized.getLanguageCode();
    }

    @Override
    public List<SysLanguage> getAll() {
        return repository.findAll();
    }

}
