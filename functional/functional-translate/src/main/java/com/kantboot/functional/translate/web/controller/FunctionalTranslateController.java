package com.kantboot.functional.translate.web.controller;

import com.kantboot.functional.translate.service.IFunctionalTranslateService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "翻译",description = "翻译", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/functional-translate-web/translate")
public class FunctionalTranslateController {

    @Resource
    private IFunctionalTranslateService service;

    @AuthInit(name = "翻译文本",description = "翻译文本", sourceLanguageCode = "zh_CN",noNeedLogin = true)
    @RequestMapping("/translateText")
    public RestResult<?> translateText(
            @RequestParam("sourceLanguageCode") String sourceLanguageCode,
            @RequestParam("targetLanguageCode") String targetLanguageCode,
            @RequestParam("sourceText") String sourceText
            ) {
        return RestResult.success(service.translateText(sourceLanguageCode, targetLanguageCode, sourceText), CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

}
