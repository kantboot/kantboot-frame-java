package com.kantboot.thirdparty.google.setting;

import com.kantboot.util.setting.annotation.Setting;
import com.kantboot.util.setting.annotation.SettingGroup;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@SettingGroup(code="thirdpartyGoogle",name="Google的配置",description="Google的配置",sourceLanguageCode = "zh_CN")
public class ThirdpartyGoogleSetting {

    @Setting(code = "clientId",name = "Google的clientId",description = "Google的clientId",sourceLanguageCode = "zh_CN")
    private String clientId;

    @Setting(code="clientSecret",name = "Google的clientSecret",description = "Google的clientSecret",sourceLanguageCode = "zh_CN")
    private String clientSecret;

}
