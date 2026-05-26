package com.kantboot.fp.carousel.dao.repository;

import com.kantboot.fp.carousel.domain.entity.FpCarousel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FpCarouselRepository
    extends JpaRepository<FpCarousel,Long> {

    /**
     * 获取比sort大的轮播图
     */
    @Query("""
    FROM FpCarousel f
    WHERE f.sort > ?1
    ORDER BY f.sort ASC
    """)
    Page<FpCarousel> findBySortAsc(Long sort, Pageable pageable);

    /**
     * 获取比sort小的轮播图
     */
    @Query("""
    FROM FpCarousel f
    WHERE f.sort < ?1
    ORDER BY f.sort DESC
    """)
    Page<FpCarousel> findBySortDesc(Long sort, Pageable pageable);

    /**
     * 根据类型编码获取轮播图
     */
    List<FpCarousel> findByTypeCode(String typeCode);

}
