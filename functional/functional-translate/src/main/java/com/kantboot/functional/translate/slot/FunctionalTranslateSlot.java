package com.kantboot.functional.translate.slot;

import com.kantboot.util.rest.exception.BaseException;
import org.springframework.stereotype.Service;

@Service
public class FunctionalTranslateSlot {

    public String translateText(String sourceLanguageCode, String targetLanguage, String text) {
        // 提示没有插件实现全局翻译
        // No plugin implements global translation
        throw BaseException.of("NoPluginImplementsTranslation","No plugin implements translation");
    }


}
