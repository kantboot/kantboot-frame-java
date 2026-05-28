package com.kantboot.system.setting.repository;

import com.kantboot.system.setting.domain.entity.SysSettingGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统设置分组数据访问层
 * @author 方某方
 */
@Repository
public interface SysSettingGroupRepository extends JpaRepository<SysSettingGroup,Long> {


    /**
     * 根据编码查询
     * @param code 编码
     * @return 系统设置分组
     */
    SysSettingGroup findByCode(String code);

    /**
     * 查询所有
     */
    @Query("""
        FROM SysSettingGroup r
        ORDER BY r.priority DESC
        """)
    List<SysSettingGroup> findAll();

}
