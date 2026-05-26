package com.kantboot.system.dict.dao.repository;

import com.kantboot.system.dict.domain.entity.SysDict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysDictRepository extends JpaRepository<SysDict,Long> {

    /**
     * 根据分组编码获取所有设置
     * @param groupCode 分组编码
     * @return 所有设置
     */
    List<SysDict> findByGroupCode(String groupCode);

    /**
     * 根据分组编码和编码获取设置
     * @param groupCode 分组编码
     * @param code 编码
     * @return 设置
     */
    SysDict findByGroupCodeAndCode(String groupCode, String code);

    @Query("""
    FROM SysDict t
    WHERE (:#{#param.groupCode} IS NULL OR t.groupCode LIKE CONCAT('%',:#{#param.groupCode},'%') OR :#{#param.groupCode} = '')
        AND (:#{#param.code} IS NULL OR t.code LIKE CONCAT('%',:#{#param.code},'%') OR :#{#param.code} = '')
        AND (:#{#param.value} IS NULL OR t.value LIKE CONCAT('%',:#{#param.value},'%') OR :#{#param.value} = '')
        AND (:#{#param.description} IS NULL OR t.description LIKE CONCAT('%',:#{#param.description},'%') OR :#{#param.description} = '')
    """)
    Page<SysDict> getBodyData(@Param("param") SysDict param, Pageable pageable);
}
