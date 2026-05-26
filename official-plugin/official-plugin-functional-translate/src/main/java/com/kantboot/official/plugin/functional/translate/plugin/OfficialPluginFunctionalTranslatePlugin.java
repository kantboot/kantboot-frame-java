package com.kantboot.official.plugin.functional.translate.plugin;

import com.kantboot.functional.translate.slot.FunctionalTranslateSlot;
import com.kantboot.thirdparty.baidu.translate.service.IThirdpartyBaiduTranslateService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 官方插件 - 功能 - 翻译 插件
 * 对于 FunctionalTranslateSlot 的实现
 * 使用了百度翻译
 */
@Configuration
public class OfficialPluginFunctionalTranslatePlugin {

    @Resource
    private IThirdpartyBaiduTranslateService thirdpartyBaiduTranslateService;


    /**
     * 使用百度翻译进行翻译
     * @param sourceLanguageCode 源语言编码
     * @param targetLanguageCode 目标语言编码
     * @param text 待翻译文本
     * @return 翻译后文本
     */
    public String thisTranslateText(String sourceLanguageCode, String targetLanguageCode, String text) {
        return thirdpartyBaiduTranslateService.translateText(sourceLanguageCode, targetLanguageCode, text);
    }

    @Bean
    public FunctionalTranslateSlot functionalTranslateSlot() {
        return new FunctionalTranslateSlot() {
            @Override
            public String translateText(String sourceLanguageCode, String targetLanguage, String text) {
                return thisTranslateText(sourceLanguageCode, targetLanguage, text);
            }
        };
    }

}
