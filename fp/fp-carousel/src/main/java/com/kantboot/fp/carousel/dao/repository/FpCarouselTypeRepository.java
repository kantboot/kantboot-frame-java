package com.kantboot.fp.carousel.dao.repository;

import com.kantboot.fp.carousel.domain.entity.FpCarouselType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FpCarouselTypeRepository
    extends JpaRepository<FpCarouselType,Long> {
}
