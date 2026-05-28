package com.kantboot.tool.area.service;

import com.kantboot.tool.area.domain.entity.ToolArea;

import java.math.BigDecimal;
import java.util.List;

public interface IToolAreaService {

    ToolArea getByCode(String code);

    ToolArea getByMinCode(String minCode);

    /**
     * 根据阿尔法二位编码查询
     */
    ToolArea getByAlpha2Code(String alpha2Code);

    /**
     * 根据阿尔法三位编码查询
     */
    ToolArea getByAlpha3Code(String alpha3Code);

    List<ToolArea> getByParentCode(String parentCode);

    /**
     * 根据code模糊查询
     */
    List<ToolArea> getByCodeVague(String codeVague);

    List<ToolArea> getByLevel(Integer level);

}
