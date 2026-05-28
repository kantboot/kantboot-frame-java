package com.kantboot.system.auth.dao.repository;

import com.kantboot.system.auth.domain.entity.SysAuthUri;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysAuthUriRepository
        extends JpaRepository<SysAuthUri,Long> {

    /**
     * 根据URI获取
     */
    SysAuthUri getFirstByUri(String uri);

    /**
     * 获取无需登录的URI
     */
    List<SysAuthUri> findByNoNeedLogin(Boolean noNeedLogin);

    /**
     * 根据ID列表获取
     */
    List<SysAuthUri> findByIdIn(List<Long> ids);

}
