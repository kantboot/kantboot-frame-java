package com.kantboot.system.auth.service;

import com.kantboot.system.auth.domain.entity.SysAuthPermission;

import java.util.List;

public interface ISysAuthRoleService {

    /**
     * 根据ID获取权限
     */
    List<SysAuthPermission> getPermissionsByIds(List<Long> ids);

    /**
     * 根据ID获取URI
     */
    List<String> getUrisByIds(List<Long> ids);

}
