package com.kantboot.util.data.change.service;

/**
 * 数据变化服务接口
 */
public interface IDataChangeService {

    /**
     * 记录数据变化
     * @param key 数据变化的键
     */
    void dataChange(String key);

    /**
     * 批量记录数据变化
     * @param keys 数据变化的键数组
     */
    void dataChanges(String[] keys);

    /**
     * 根据键获取UUID
     * @param key 数据变化的键
     * @return UUID
     */
    String getUuidByKey(String key);

}
