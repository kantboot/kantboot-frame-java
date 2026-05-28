package com.kantboot.fp.carousel.web.admin.controller;

import com.kantboot.fp.carousel.domain.entity.FpCarouselType;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "轮播图类型管理",description = "轮播图类型管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/fp-carousel-web/admin/carouselType")
public class FpCarouselTypeControllerOfAdmin
    extends BaseAdminController<FpCarouselType,Long> {
}
