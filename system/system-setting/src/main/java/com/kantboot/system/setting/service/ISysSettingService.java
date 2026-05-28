package com.kantboot.system.setting.service;

import com.kantboot.system.setting.domain.entity.SysSetting;

import java.util.HashMap;
import java.util.List;

/**
 * 系统设置服务层接口
 * 提供对系统设置的管理功能
 */
public interface ISysSettingService {

    /**
     * 根据Code获取设置
     * 
     * @param code 编码
     * @return 设置
     */
    SysSetting getByCode(String code);

    List<SysSetting> getByGroupCode(String groupCode);

    /**
     * 根据分组获取所有设置，以map形式返回
     *
     * @param groupCode 分组编码
     * @return 所有设置
     */
    HashMap<String, String> getMapByGroupCode(String groupCode);

    /**
     * 根据分组和编码获取值
     * 修改成了将code设成唯一索引，不再需要groupCode
     *
     * @param groupCode 分组编码
     * @param code      编码
     * @return 设置
     */
    @Deprecated
    String getValue(String groupCode, String code);

    /**
     * 根据编码获取值
     *
     * @param code 编码
     * @return 设置
     */
    String getValue(String code);

    /**
     * 根據编码获取值（不抛出异常）
     * @param code 编码
     *             Code
     * @return 设置
     */
    String getValueNoThrow(String code);

    /**
     * 保存系统设置
     *
     * @param sysSetting 系统设置实体
     * @return 保存后的系统设置实体
     */
    SysSetting save(SysSetting sysSetting);

    /**
     * 设置值
     *
     * @param groupCode 分组编码
     * @param code      编码
     * @param value     值
     */
    void setValue(String groupCode, String code, String value);

    /**
     * 设置值
     *
     * @param code  编码
     * @param value 值
     */
    void setValue(String code, String value);

    /**
     * 删除设置
     *
     * @param code 编码
     */
    void removeByCode(String code);

    /**
     * 删除设置
     */
    void remove(SysSetting entity);

    /**
     * init
     */
    void init(List<SysSetting> settingList);
}
