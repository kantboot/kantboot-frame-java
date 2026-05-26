package com.kantboot.system.setting.service.impl;

import com.kantboot.system.setting.domain.entity.SysSettingGroup;
import com.kantboot.system.setting.repository.SysSettingGroupRepository;
import com.kantboot.system.setting.service.ISysSettingGroupService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统设置分组服务层实现类
 * 提供对系统设置分组的管理功能
 */
@Slf4j
@Service
public class SysSettingGroupServiceImpl
        implements ISysSettingGroupService {

    @Resource
    private SysSettingGroupRepository repository;

    @Override
    public SysSettingGroup save(SysSettingGroup entity) {
        return repository.save(entity);
    }

    @Override
    public void init(List<SysSettingGroup> list) {
        log.info("(SystemSettingInit) KANTBOOT-初始化系统设置分组");
        if(list == null||list.isEmpty()){
            log.info("(SystemSettingInit) KANTBOOT-初始化系统设置分组，列表为空，跳过");
            return;
        }
        for (SysSettingGroup sysSettingGroup : list) {
            SysSettingGroup byCode = repository.findByCode(sysSettingGroup.getCode());
            if (byCode == null) {
                // 如果不存在，则保存
                repository.save(sysSettingGroup);
                log.info("(SystemSettingInit) KANTBOOT-初始化系统设置分组，保存：{}", sysSettingGroup.getCode());
                continue;
            }
            log.info("(SystemSettingInit) KANTBOOT-初始化系统设置分组，对应CODE已存在，跳过：{}", sysSettingGroup.getCode());
        }
        log.info("(SystemSettingInit) KANTBOOT-初始化系统设置分组完成");
    }
}
