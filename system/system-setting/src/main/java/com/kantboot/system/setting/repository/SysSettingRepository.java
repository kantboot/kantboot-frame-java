package com.kantboot.system.setting.repository;

import com.kantboot.system.setting.domain.entity.SysSetting;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统设置数据访问层
 * @author 方某方
 */
@Repository
public interface SysSettingRepository extends JpaRepository<SysSetting,Long>
{

    /**
     * 根据分组编码查询
     * @param groupCode 分组编码
     * @return 系统设置
     */
    List<SysSetting> findByGroupCode(String groupCode);

    /**
     * 根据分组编码和编码查询
     * 由于将code设成唯一索引，不再需要groupCode
     * @param groupCode 分组编码
     * @param code 编码
     * @return 系统设置
     */
    SysSetting findByGroupCodeAndCode(String groupCode, String code);

    /**
     * 根据编码查询
     * @param code 编码
     * @return 系统设置
     */
    SysSetting findByCode(String code);

    /**
     * 根据编码删除
     * @param code 编码
     */
    @Modifying
    @Transactional
    void removeByCode(String code);

}
