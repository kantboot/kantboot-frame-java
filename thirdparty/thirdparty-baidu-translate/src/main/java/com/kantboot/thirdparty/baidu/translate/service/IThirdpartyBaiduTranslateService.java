package com.kantboot.thirdparty.baidu.translate.service;

import com.kantboot.thirdparty.baidu.translate.setting.ThirdpartyBaiduTranslateSetting;

public interface IThirdpartyBaiduTranslateService {

    /**
     * 获取设置
     * Get settings
     */
    ThirdpartyBaiduTranslateSetting getSetting();

    /**
     * 配置设置
     */
    void setSetting(ThirdpartyBaiduTranslateSetting setting);

    /**
     * 翻译文本
     * Translate text
     * @param sourceLanguageCode 源语言编码
     *                           Source language code
     * @param targetLanguageCode 目标语言
     *                       Target language code
     * @param text 文本
     *             Text
     */
    String translateText(String sourceLanguageCode, String targetLanguageCode, String text);

    /**
     * 识别语言
     */
    String detectLanguage(String text);

}
