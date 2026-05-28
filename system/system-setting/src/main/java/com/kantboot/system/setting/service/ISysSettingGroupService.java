package com.kantboot.system.setting.service;

import com.kantboot.system.setting.domain.entity.SysSettingGroup;

import java.util.List;

/**
 * 系统设置分组服务层接口
 * 提供对系统设置分组的管理功能
 */
public interface ISysSettingGroupService {

    /**
     * 保存系统设置分组
     *
     * @param entity 系统设置分组实体
     * @return 保存后的系统设置分组实体
     */
    SysSettingGroup save(SysSettingGroup entity);

    /**
     * 初始化
     */
    void init(List<SysSettingGroup> list);

}
