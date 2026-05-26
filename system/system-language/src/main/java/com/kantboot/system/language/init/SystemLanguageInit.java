package com.kantboot.system.language.init;

import com.kantboot.init.KantbootInit;
import com.kantboot.system.language.consts.SystemLanguageInitConsts;
import com.kantboot.system.language.dao.repository.SysLanguageLocalizedRepository;
import com.kantboot.system.language.dao.repository.SysLanguageRepository;
import com.kantboot.system.language.domain.entity.SysLanguage;
import com.kantboot.system.language.domain.entity.SysLanguageLocalized;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class SystemLanguageInit {

    @Resource
    private SysLanguageRepository languageRepository;

    @Resource
    private SysLanguageLocalizedRepository localizedRepository;

    @Resource
    private KantbootInit kantbootInit;

    @PostConstruct
    public void init() {
        // 如果kantbootInit的init属性为false，则不进行初始化
        if (!kantbootInit.isInit()) {
            log.info("KantbootInit的init属性为false，跳过系统语言初始化");
            return;
        }
        Thread.ofVirtual()
                .name("SystemLanguageInit")
                .start(() -> {
                    initSysLanguage();
                    initSysLanguageLocalized();
                    initAnnotation();
       });
    }

    private void initSysLanguage(){
        // 检测系统语言列表（SysLanguage）是否为空
        log.info("检测系统语言列表（SysLanguage）是否为空");
        List<SysLanguage> all = languageRepository.findAll();
        if (!all.isEmpty()) {
            log.info("系统语言列表（SysLanguage）不为空，跳过初始化");
            return;
        }
        log.info("系统语言列表（SysLanguage）为空，开始初始化");
        languageRepository.saveAll(SystemLanguageInitConsts.LANGUAGE_INIT_LIST);
        log.info("系统语言列表（SysLanguage）初始化完成");
    }

    private void initSysLanguageLocalized(){
        // 检测系统语言国际化列表（SysLanguageLocalized）是否为空
        System.out.println("检测系统语言本地化列表（SysLanguageLocalized）是否为空");
        List<SysLanguageLocalized> all = localizedRepository.findAll();
        if (!all.isEmpty()) {
            System.out.println("系统语言本地化列表（SysLanguageLocalized）不为空，跳过初始化");
            return;
        }
        System.out.println("系统语言本地化列表（SysLanguageLocalized）为空，开始初始化");
        localizedRepository.saveAll(SystemLanguageInitConsts.LANGUAGE_LOCALIZED_INIT_LIST);
        System.out.println("系统语言本地化列表（SysLanguageLocalized）初始化完成");
    }

    private void initAnnotation(){
        // 检测I18nTopKey注解是否存在
        log.info("检测I18nTopKey注解是否存在");

    }

}