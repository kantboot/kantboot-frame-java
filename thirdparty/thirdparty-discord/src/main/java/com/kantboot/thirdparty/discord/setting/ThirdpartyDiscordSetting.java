package com.kantboot.thirdparty.discord.setting;

import com.kantboot.util.setting.annotation.Setting;
import com.kantboot.util.setting.annotation.SettingGroup;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@SettingGroup(code = "thirdpartyDiscord", name = "Discord的配置", description = "Discord的配置", sourceLanguageCode = "zh_CN")
public class ThirdpartyDiscordSetting {

    @Setting(code = "clientId", name = "Discord的clientId", description = "Discord的clientId", sourceLanguageCode = "zh_CN")
    private String clientId;

    @Setting(code = "clientSecret", name = "Discord的clientSecret", description = "Discord的clientSecret", sourceLanguageCode = "zh_CN")
    private String clientSecret;
}
