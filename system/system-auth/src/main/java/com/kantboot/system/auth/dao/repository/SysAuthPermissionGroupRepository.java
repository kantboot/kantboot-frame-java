package com.kantboot.system.auth.dao.repository;

import com.kantboot.system.auth.domain.entity.SysAuthPermissionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysAuthPermissionGroupRepository
    extends JpaRepository<SysAuthPermissionGroup, Long> {
}
