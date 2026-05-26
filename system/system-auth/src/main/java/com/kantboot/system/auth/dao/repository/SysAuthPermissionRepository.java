package com.kantboot.system.auth.dao.repository;

import com.kantboot.system.auth.domain.entity.SysAuthPermission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 权限数据访问接口
 * Permission Data Access Interface
 * 该接口用于访问权限数据
 * This interface is used to access permission data
 * 
 */
public interface SysAuthPermissionRepository
        extends JpaRepository<SysAuthPermission, Long> {

    /**
     * 根据编码获取权限
     */
    SysAuthPermission getByCode(String code);

    /**
     * 根据编码列表获取权限列表
     */
    java.util.List<SysAuthPermission> findByCodeIn(java.util.List<String> codes);

}
