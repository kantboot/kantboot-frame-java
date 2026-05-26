package com.kantboot.system.language.service;

import com.kantboot.system.language.domain.entity.SysLanguage;
import com.kantboot.system.language.domain.entity.SysLanguageLocalized;

import java.util.List;

public interface ISysLanguageService {

    List<SysLanguage> getBySupport();

    List<SysLanguageLocalized> getLocalizedList();

    String localizedCodeToLanguageCode(String code);

    /**
     * 获取所有语言
     */
    List<SysLanguage> getAll();

}
