package com.kantboot.system.auth.service;

import com.kantboot.system.auth.domain.entity.SysAuthUri;

import java.util.List;

public interface ISysAuthUriService {

    /**
     * 根据URI获取
     */
    SysAuthUri getByUri(String uri);

    /**
     * 保存
     */
    void save(SysAuthUri sysAuthUri);

    void remove(SysAuthUri sysAuthUri);

    List<SysAuthUri> getByNoNeedLogin(Boolean noNeedLogin);

}
