package com.kantboot.fp.carousel.service;

import com.kantboot.fp.carousel.domain.entity.FpCarousel;

import java.util.List;

public interface IFpCarouselService {

    FpCarousel save(FpCarousel fpCarousel);

    /**
     * 上移
     */
    FpCarousel moveUp(FpCarousel fpCarousel);

    /**
     * 下移
     */
    FpCarousel moveDown(FpCarousel fpCarousel);

    /**
     * 根据类型编码获取轮播图
     */
    List<FpCarousel> getByTypeCode(String typeCode);

    /**
     * 根据ID获取
     */
    FpCarousel getById(Long id);

}
