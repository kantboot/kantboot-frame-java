package com.kantboot.system.dict.web.controller;

import com.kantboot.system.dict.service.ISysDictService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "字典",description ="字典", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/system-dict-web/dict")
public class SysDictController {

    @Resource
    private ISysDictService service;

    /**
     * 获取字典
     * @param dictGroupCode 字典组编码
     * @return 字典列表
     */
    @AuthInit(name = "获取字典",description ="获取字典",sourceLanguageCode = "zh_CN",noNeedLogin = true)
    @RequestMapping("/getDict")
    public RestResult<?> getDict(@RequestParam("dictGroupCode") String dictGroupCode) {
        return RestResult.success(service.getDict(dictGroupCode), CommonSuccessStateConsts.GET_SUCCESS);
    }


}
