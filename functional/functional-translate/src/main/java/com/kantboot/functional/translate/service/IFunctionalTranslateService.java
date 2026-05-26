package com.kantboot.functional.translate.service;

public interface IFunctionalTranslateService {

    /**
     * 翻译文本
     * @param sourceLanguageCode 源语言代码
     * @param targetLanguage 目标语言代码
     * @param text 待翻译的文本
     * @return 翻译后的文本
     */
    String translateText(String sourceLanguageCode, String targetLanguage, String text);

}
