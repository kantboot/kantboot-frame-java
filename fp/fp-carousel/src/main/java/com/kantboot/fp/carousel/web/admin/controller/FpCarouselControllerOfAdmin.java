package com.kantboot.fp.carousel.web.admin.controller;

import com.kantboot.fp.carousel.domain.entity.FpCarousel;
import com.kantboot.fp.carousel.service.IFpCarouselService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "轮播图管理",description = "轮播图管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/fp-carousel-web/admin/carousel")
public class FpCarouselControllerOfAdmin
    extends BaseAdminController<FpCarousel,Long> {

    @Resource
    private IFpCarouselService service;

    @Override
    @AuthInit(name = "保存（重写通用管理后台）",description = "保存（重写通用管理后台）", sourceLanguageCode = "zh_CN")
    public RestResult<?> save(@RequestBody FpCarousel fpCarousel) {
        return RestResult.success(service.save(fpCarousel), CommonSuccessStateConsts.SAVE_SUCCESS);
    }

    /**
     * 上移
     */
    @AuthInit(name = "上移",description = "上移", sourceLanguageCode = "zh_CN")
    @RequestMapping("/moveUp")
    public RestResult<?> moveUp(@RequestBody FpCarousel fpCarousel) {
        return RestResult.success(service.moveUp(fpCarousel), CommonSuccessStateConsts.SAVE_SUCCESS);
    }

    /**
     * 下移
     */
    @AuthInit(name = "下移",description = "下移", sourceLanguageCode = "zh_CN")
    @RequestMapping("/moveDown")
    public RestResult<?> moveDown(@RequestBody FpCarousel fpCarousel) {
        return RestResult.success(service.moveDown(fpCarousel), CommonSuccessStateConsts.SAVE_SUCCESS);
    }

}
