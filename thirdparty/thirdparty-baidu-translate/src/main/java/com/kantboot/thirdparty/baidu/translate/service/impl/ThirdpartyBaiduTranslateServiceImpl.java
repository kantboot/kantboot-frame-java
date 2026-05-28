package com.kantboot.thirdparty.baidu.translate.service.impl;

import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.thirdparty.baidu.translate.exception.ThirdpartyBaiduTranslateException;
import com.kantboot.thirdparty.baidu.translate.service.IThirdpartyBaiduTranslateService;
import com.kantboot.thirdparty.baidu.translate.setting.ThirdpartyBaiduTranslateSetting;
import com.kantboot.thirdparty.baidu.translate.util.BaiduTranslateUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ThirdpartyBaiduTranslateServiceImpl implements IThirdpartyBaiduTranslateService {

    @Resource
    private ISysSettingService settingService;

    @Override
    public ThirdpartyBaiduTranslateSetting getSetting() {
        ThirdpartyBaiduTranslateSetting thirdpartyBaiduTranslateSetting = new ThirdpartyBaiduTranslateSetting();
        thirdpartyBaiduTranslateSetting.setAppid(settingService.getValue("thirdpartyBaiduTranslate.appid"));
        thirdpartyBaiduTranslateSetting.setSecret(settingService.getValue("thirdpartyBaiduTranslate.secret"));
        return thirdpartyBaiduTranslateSetting;
    }

    @Override
    public void setSetting(ThirdpartyBaiduTranslateSetting setting) {
        settingService.setValue("thirdpartyBaiduTranslate.appid", setting.getAppid());
        settingService.setValue("thirdpartyBaiduTranslate.secret", setting.getSecret());
    }

    @Override
    public String translateText(String sourceLanguageCode, String targetLanguageCode, String text) {
        ThirdpartyBaiduTranslateSetting setting = getSetting();
        log.info("Baidu translateText sourceLanguageCode:{} targetLanguageCode:{} text:{}", sourceLanguageCode, targetLanguageCode, text);
        return BaiduTranslateUtil.translate(text,
                BaiduTranslateUtil.toBaiduCode(sourceLanguageCode),
                BaiduTranslateUtil.toBaiduCode(targetLanguageCode),
                setting.getAppid(),
                setting.getSecret());
    }

    @Override
    public String detectLanguage(String text) {
        ThirdpartyBaiduTranslateSetting setting = getSetting();
        log.info("Baidu detectLanguage text:{}", text);
        String languageCode = BaiduTranslateUtil.detectLanguage(text, setting.getAppid(), setting.getSecret());
        if (languageCode == null) {
            throw ThirdpartyBaiduTranslateException.CODE_54009;
        }
        return BaiduTranslateUtil.detectLanguage(languageCode, setting.getAppid(), setting.getSecret());
    }
}
