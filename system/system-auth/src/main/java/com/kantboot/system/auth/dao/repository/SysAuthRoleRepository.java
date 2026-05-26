package com.kantboot.system.auth.dao.repository;

import com.kantboot.system.auth.domain.entity.SysAuthRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysAuthRoleRepository
    extends JpaRepository<SysAuthRole, Long> {

    /**
     * 根据IDS查询
     */
    List<SysAuthRole> findByIdIn(List<Long> ids);

}
