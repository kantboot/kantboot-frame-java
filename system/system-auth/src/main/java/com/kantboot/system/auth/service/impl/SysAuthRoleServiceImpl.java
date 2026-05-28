package com.kantboot.system.auth.service.impl;

import com.kantboot.system.auth.dao.repository.SysAuthPermissionRepository;
import com.kantboot.system.auth.dao.repository.SysAuthRoleRepository;
import com.kantboot.system.auth.dao.repository.SysAuthUriRepository;
import com.kantboot.system.auth.domain.entity.SysAuthPermission;
import com.kantboot.system.auth.domain.entity.SysAuthRole;
import com.kantboot.system.auth.domain.entity.SysAuthUri;
import com.kantboot.system.auth.service.ISysAuthRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysAuthRoleServiceImpl
        implements ISysAuthRoleService {

    @Resource
    private SysAuthRoleRepository repository;

    @Resource
    private SysAuthPermissionRepository permissionRepository;

    @Resource
    private SysAuthUriRepository uriRepository;

    @Override
    public List<SysAuthPermission> getPermissionsByIds(List<Long> ids) {
        if(ids==null){
            return List.of();
        }
        List<SysAuthRole> byIds = repository.findByIdIn(ids);
        if (byIds == null) {
            return List.of();
        }
        List<String> permissionCode = new ArrayList<>();
        for (SysAuthRole byId : byIds) {
            permissionCode.addAll(byId.getPermissionCodes());
        }
        return permissionRepository.findByCodeIn(permissionCode);
    }

    @Override
    public List<String> getUrisByIds(List<Long> ids) {
        List<SysAuthPermission> permissionsByIds = getPermissionsByIds(ids);
        List<Long> uriIds = new ArrayList<>();
        for (SysAuthPermission permissionsById : permissionsByIds) {
            uriIds.addAll(permissionsById.getUriIds());
        }
        List<SysAuthUri> byIdIn = uriRepository.findByIdIn(uriIds);
        List<String> uris = new ArrayList<>();
        for (SysAuthUri sysAuthUri : byIdIn) {
            uris.add(sysAuthUri.getUri());
        }
        return uris;
    }

}
