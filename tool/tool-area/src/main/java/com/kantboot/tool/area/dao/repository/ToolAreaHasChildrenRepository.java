package com.kantboot.tool.area.dao.repository;

import com.kantboot.tool.area.domain.entity.ToolAreaHasChildren;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToolAreaHasChildrenRepository
    extends JpaRepository<ToolAreaHasChildren,Long> {

    /**
     * 根据parentCode查询
     */
    List<ToolAreaHasChildren> findByParentCode(String parentCode);

}
