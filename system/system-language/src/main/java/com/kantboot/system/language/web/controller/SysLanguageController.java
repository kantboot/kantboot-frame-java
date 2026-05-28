package com.kantboot.system.language.web.controller;

import com.kantboot.system.language.domain.entity.SysLanguage;
import com.kantboot.system.language.domain.entity.SysLanguageLocalized;
import com.kantboot.system.language.service.ISysLanguageService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AuthInit(name = "语言", description = "语言", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/system-language-web/language")
public class SysLanguageController {

    @Resource
    private ISysLanguageService service;

    @AuthInit(name = "获取支持的语言列表", description = "获取支持语言列表", sourceLanguageCode = "zh_CN", noNeedLogin = true)
    @RequestMapping("/getBySupport")
    public RestResult<List<SysLanguage>> getBySupport() {
        return RestResult.success(service.getBySupport(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "获取所有语言", description = "获取所有语言", sourceLanguageCode = "zh_CN", noNeedLogin = true)
    @RequestMapping("/getAll")
    public RestResult<List<SysLanguage>> getAll() {
        return RestResult.success(service.getAll(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 获取所有的语言本地化信息
     *
     * @return 语言本地化信息列表
     */
    @AuthInit(name = "获取所有语言本地化信息", description = "获取所有语言本地化信息", sourceLanguageCode = "zh_CN", noNeedLogin = true)
    @RequestMapping("/getLocalizedList")
    public RestResult<List<SysLanguageLocalized>> getLocalizes() {
        return RestResult.success(service.getLocalizedList(), CommonSuccessStateConsts.GET_SUCCESS);
    }

}
