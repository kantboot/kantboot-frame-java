package com.kantboot.fp.carousel.web.controller;

import com.kantboot.fp.carousel.service.IFpCarouselService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "轮播图", description = "轮播图", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/fp-carousel-web/carousel")
public class FpCarouselController {

    @Resource
    private IFpCarouselService service;

    /**
     * 根据类型编码获取轮播图
     */
    @AuthInit(name = "根据类型编码获取轮播图", description = "根据类型编码获取轮播图", sourceLanguageCode = "zh_CN",noNeedLogin = true)
    @RequestMapping("/getByTypeCode")
    public RestResult<?> getByTypeCode(@RequestParam("typeCode") String typeCode) {
        return RestResult.success(service.getByTypeCode(typeCode), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 根据ID获取
     */
    @AuthInit(name = "根据ID获取", description = "根据ID获取", sourceLanguageCode = "zh_CN",noNeedLogin = true)
    @RequestMapping("/getById")
    public RestResult<?> getById(@RequestParam("id") Long id) {
        return RestResult.success(service.getById(id), CommonSuccessStateConsts.GET_SUCCESS);
    }



}
