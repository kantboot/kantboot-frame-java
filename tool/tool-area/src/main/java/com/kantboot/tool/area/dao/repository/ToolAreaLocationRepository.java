package com.kantboot.tool.area.dao.repository;

import com.kantboot.tool.area.domain.entity.ToolAreaLocation;
import com.kantboot.util.location.domain.LocationMaxMin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ToolAreaLocationRepository
    extends JpaRepository<ToolAreaLocation,Long> {

    /**
     * 根据位置和距离查询附件被记录的位置
     */
    @Query("""
            select tal from ToolAreaLocation tal
            where tal.longitude between :#{#param.longitudeMin} and :#{#param.longitudeMax}
            and tal.latitude between :#{#param.latitudeMin} and :#{#param.latitudeMax}
        """)
    List<ToolAreaLocation> findByMaxMinLocation(@Param("param") LocationMaxMin locationMaxMin);

}
