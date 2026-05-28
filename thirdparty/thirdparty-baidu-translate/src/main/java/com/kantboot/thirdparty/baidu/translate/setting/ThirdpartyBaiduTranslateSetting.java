package com.kantboot.thirdparty.baidu.translate.setting;

import com.kantboot.util.setting.annotation.Setting;
import com.kantboot.util.setting.annotation.SettingGroup;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@SettingGroup(code = "thirdpartyBaiduTranslate", name = "百度翻译", description = "百度翻译的相关设置", sourceLanguageCode = "zh_CN")
public class ThirdpartyBaiduTranslateSetting {

    /**
     * 获取百度翻译的appid
     * Get the appid of Baidu translation
     */
    @Setting(code = "appid", name = "百度翻译的appid", description = "百度翻译的appid", sourceLanguageCode = "zh_CN")
    private String appid;

    /**
     * 获取百度翻译的secret
     * Get the secret of Baidu translation
     */
    @Setting(code = "secret", name = "百度翻译的secret", description = "百度翻译的secret", sourceLanguageCode = "zh_CN")
    private String secret;

}