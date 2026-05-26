package com.kantboot.thirdparty.github.setting;

import com.kantboot.util.setting.annotation.Setting;
import com.kantboot.util.setting.annotation.SettingGroup;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

@Data
@Accessors(chain = true)
@Component
@SettingGroup(code = "thirdpartyGithub",name = "阿里云市场的配置",description = "阿里云的市场的配置",sourceLanguageCode = "zh_CN")
public class ThirdpartyGithubSetting {

    @Setting(code="clientId",name="GitHub的clientId",description="GitHub的clientId",sourceLanguageCode = "zh_CN")
    private String clientId;

    @Setting(code="clientSecret",name="GitHub的clientSecret",description="GitHub的clientSecret",sourceLanguageCode = "zh_CN")
    private String clientSecret;

}
