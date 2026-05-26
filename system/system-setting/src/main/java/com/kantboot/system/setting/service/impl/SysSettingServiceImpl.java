package com.kantboot.system.setting.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.kantboot.system.setting.domain.entity.SysSetting;
import com.kantboot.system.setting.exception.SystemSettingException;
import com.kantboot.system.setting.repository.SysSettingRepository;
import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.util.rest.exception.BaseException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * 系统设置服务层实现类
 * 提供对系统设置的管理功能
 */
@Slf4j
@Primary
@Service
public class SysSettingServiceImpl implements ISysSettingService {

    // 缓存值
    private final static String CACHE_VALUE = "sysSetting";

    @Resource
    private SysSettingRepository repository;

    @Override
//    @Cacheable(value = CACHE_VALUE, key = "#code")
    public SysSetting getByCode(String code) {
        SysSetting byCode = repository.findByCode(code);
        if (byCode == null) {
            throw BaseException.of("sysSetting.noHasValue:" + code,
                    "不存在此配置，编码：`" + code + "`", "zh_CN");
        }
        return byCode;
    }

    @Override
    public List<SysSetting> getByGroupCode(String groupCode) {
        return repository.findByGroupCode(groupCode);
    }

    @Override
    public HashMap<String, String> getMapByGroupCode(String groupCode) {
        HashMap<String, String> result = new HashMap<>(100);
        List<SysSetting> byGroupCode = repository.findByGroupCode(groupCode);
        for (SysSetting sysSetting : byGroupCode) {
            result.put(sysSetting.getCode(), sysSetting.getValue());
        }
        return result;
    }

    @Override
//    @Cacheable(value = CACHE_VALUE, key = "#code")
    public String getValue(String groupCode, String code) {
        SysSetting byGroupCodeAndCode = repository.findByGroupCodeAndCode(groupCode, code);
        if (byGroupCodeAndCode == null) {
            throw BaseException.of("sysSettingNoHasValue_" + groupCode + "_" + code,
                    "不存在此配置，分组编码：`" + groupCode + "`，编码：`" + code + "`", "zh_CN");
        }
        return byGroupCodeAndCode.getValue();
    }

    @Override
//    @Cacheable(value = CACHE_VALUE, key = "#code")
    public String getValue(String code) {
        SysSetting byCode = repository.findByCode(code);
//        System.err.println(JSON.toJSONString(byCode));
        if (byCode == null) {
            throw BaseException.of("sysSettingNoHasValue_" + code,
                    "不存在此配置，编码：`" + code + "`", "zh_CN");
        }
        return byCode.getValue();
    }

    @Override
//    @Cacheable(value = CACHE_VALUE, key = "#code")
    public String getValueNoThrow(String code) {
        SysSetting byCode = repository.findByCode(code);
        if (byCode == null) {
            return null;
        }
        return byCode.getValue();
    }

    @Override
//    @CacheEvict(value = CACHE_VALUE, key = "#sysSetting.code")
    public SysSetting save(SysSetting sysSetting) {
        if(sysSetting.getId()==null&& StrUtil.isEmpty(sysSetting.getCode())){
            throw SystemSettingException.CODE_NOT_EMPTY;
        }
        try{
            return repository.save(sysSetting);
        } catch (DataIntegrityViolationException e) {
            throw SystemSettingException.CODE_EXIST;
        }
    }

    @Override
//    @CacheEvict(value = CACHE_VALUE, key = "#code")
    public void setValue(String groupCode, String code, String value) {
        SysSetting byGroupCodeAndCode = repository.findByGroupCodeAndCode(groupCode, code);
        byGroupCodeAndCode.setValue(value);
    }

    @Override
//    @CacheEvict(value = CACHE_VALUE, key = "#code")
    public void setValue(String code, String value) {
        SysSetting byCode = repository.findByCode(code);
        if(byCode == null){
            repository.save(new SysSetting().setCode(code).setValue(value));
            return;
        }
        byCode.setValue(value);
        repository.save(byCode);
    }

    @Override
//    @CacheEvict(value = CACHE_VALUE, key = "#code")
    public void removeByCode(String code) {
        repository.removeByCode(code);
    }

    @Override
    public void remove(SysSetting entity) {
        SysSetting sysSetting = repository.findById(entity.getId()).orElse(null);
        if (sysSetting == null) {
            return;
        }
        removeByCode(sysSetting.getCode());
    }

    @Override
    public void init(List<SysSetting> settingList) {
        log.info("(SystemSettingInit) KANTBOOT-初始化系统设置");
        if(settingList == null||settingList.isEmpty()){
            log.info("(SystemSettingInit) KANTBOOT-初始化系统设置，列表为空，跳过");
            return;
        }
        for (SysSetting sysSetting : settingList) {
            SysSetting byCode = repository.findByCode(sysSetting.getCode());
            if (byCode == null) {
                // 如果不存在，则保存
                save(sysSetting);
                log.info("(SystemSettingInit) KANTBOOT-初始化系统设置，保存：{}", sysSetting.getCode());
                continue;
            }
            if(StrUtil.isEmpty(byCode.getValue())){
                // 如果值不存在，则保存
                sysSetting.setId(byCode.getId());
                save(sysSetting);
                log.info("(SystemSettingInit) KANTBOOT-初始化系统设置，保存：{}", sysSetting.getCode());
                continue;
            }

            log.info("(SystemSettingInit) KANTBOOT-初始化系统设置，对应CODE已存在，跳过：{}", sysSetting.getCode());
        }
        log.info("(SystemSettingInit) KANTBOOT-初始化系统设置完成");
    }

}
