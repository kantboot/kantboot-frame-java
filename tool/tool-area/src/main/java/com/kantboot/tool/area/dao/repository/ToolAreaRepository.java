package com.kantboot.tool.area.dao.repository;

import com.kantboot.tool.area.domain.entity.ToolArea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToolAreaRepository
    extends JpaRepository<ToolArea,Long> {

    /**
     * 根据code查询
     */
    ToolArea findByCode(String code);

    /**
     * 根据minCode查询
     */
    ToolArea findByMinCode(String minCode);

    /**
     * 根据阿尔法二位编码查询
     */
    ToolArea findByAlpha2Code(String alpha2Code);

    /**
     * 根据阿尔法三位编码查询
     */
    ToolArea findByAlpha3Code(String alpha3Code);

    /**
     * 根据父级code查询
     */
    java.util.List<ToolArea> findByParentCode(String parentCode);

    /**
     * 根据code模糊查询
     */
    java.util.List<ToolArea> findByCodeLike(String codeVague);

    /**
     * 根据等级获取
     */
    java.util.List<ToolArea> findByLevel(Integer level);

}
