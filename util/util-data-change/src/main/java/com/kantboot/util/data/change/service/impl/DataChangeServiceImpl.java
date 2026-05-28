package com.kantboot.util.data.change.service.impl;

import cn.hutool.core.util.IdUtil;
import com.kantboot.util.cache.CacheUtil;
import com.kantboot.util.data.change.service.IDataChangeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 数据变化服务实现类
 */
@Service
public class DataChangeServiceImpl implements IDataChangeService {

    @Resource
    private CacheUtil cacheUtil;

    /**
     * 记录数据变化
     * @param key 数据变化的键
     */
    @Override
    public void dataChange(String key) {
        if (!"".equals(key)) {
            String uuid = IdUtil.simpleUUID();
            cacheUtil.set("ChangeData:" + key, uuid);
        }
    }

    /**
     * 批量记录数据变化
     * @param keys 数据变化的键数组
     */
    @Override
    public void dataChanges(String[] keys) {
        String uuid = IdUtil.simpleUUID();
        for (String key : keys) {
            cacheUtil.set("ChangeData:" + key, uuid);
        }
    }

    /**
     * 根据键获取UUID
     * @param key 数据变化的键
     * @return UUID
     */
    @Override
    public String getUuidByKey(String key) {
        String uuid = cacheUtil.get("ChangeData:" + key);
        if(uuid==null){
            uuid = IdUtil.simpleUUID();
            cacheUtil.set("ChangeData:" + key, uuid);
        }
        return uuid;
    }
}
