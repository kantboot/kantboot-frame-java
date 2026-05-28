package com.kantboot.system.auth.service.impl;

import com.alibaba.fastjson2.JSON;
import com.kantboot.system.auth.dao.repository.SysAuthUriRepository;
import com.kantboot.system.auth.domain.entity.SysAuthUri;
import com.kantboot.system.auth.service.ISysAuthUriService;
import com.kantboot.util.cache.CacheUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SysAuthUriServiceImpl implements ISysAuthUriService {

    @Resource
    private SysAuthUriRepository repository;

    @Resource
    private CacheUtil cacheUtil;

    @Override
    public SysAuthUri getByUri(String uri) {
        String redisKey = "sysAuthUri:" + uri;
//
//        // 尝试从缓存获取，如果Redis连接失败则直接查询数据库
//        try {
//            String jsonStr = cacheUtil.get(redisKey);
//            if (jsonStr != null) {
//                return JSON.parseObject(jsonStr, SysAuthUri.class);
//            }
//        } catch (Exception e) {
//            // Redis连接失败时不影响业务逻辑，直接查询数据库
//        }
        
        SysAuthUri byUri = repository.getFirstByUri(uri);
        if (byUri != null) {
            // 尝试缓存结果，失败时不影响返回
            try {
                cacheUtil.set(redisKey, JSON.toJSONString(byUri));
            } catch (Exception e) {
                // Redis连接失败时忽略缓存操作
            }
        }
        return byUri;
    }

    @Override
    public void save(SysAuthUri sysAuthUri) {
        repository.save(sysAuthUri);
        // 尝试清除缓存，失败时不影响保存操作
        try {
            cacheUtil.delete("sysAuthUri:" + sysAuthUri.getUri());
        } catch (Exception e) {
            // Redis连接失败时忽略缓存清除操作
        }
    }

    @Override
    public void remove(SysAuthUri sysAuthUri) {
        Optional<SysAuthUri> byId = repository.findById(sysAuthUri.getId());
        if (byId.isEmpty()) {
            return;
        }
        sysAuthUri = byId.get();
        repository.delete(sysAuthUri);
        // 尝试清除缓存，失败时不影响删除操作
        try {
            cacheUtil.delete("sysAuthUri:" + sysAuthUri.getUri());
        } catch (Exception e) {
            // Redis连接失败时忽略缓存清除操作
        }
    }

    @Override
    public List<SysAuthUri> getByNoNeedLogin(Boolean noNeedLogin) {
        return repository.findByNoNeedLogin(noNeedLogin);
    }
}
