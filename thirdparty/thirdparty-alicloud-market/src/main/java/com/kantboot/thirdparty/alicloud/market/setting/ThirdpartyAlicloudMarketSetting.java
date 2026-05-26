package com.kantboot.thirdparty.alicloud.market.setting;

import com.kantboot.util.setting.annotation.Setting;
import com.kantboot.util.setting.annotation.SettingGroup;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

@Data
@Accessors(chain = true)
@Component
@SettingGroup(code = "thirdpartyAlicloudMarket",name = "阿里云市场的配置",description = "阿里云的市场的配置",sourceLanguageCode = "zh_CN")
public class ThirdpartyAlicloudMarketSetting {

    @Setting(code="appKey",name="阿里云市场的appKey",description="阿里云市场的appKey",sourceLanguageCode = "zh_CN")
    private String appKey;

    @Setting(code="appSecret",name="阿里云市场的appSecret",description="阿里云市场的appSecret",sourceLanguageCode = "zh_CN")
    private String appSecret;

    @Setting(code="appCode",name="阿里云市场的appCode",description="阿里云市场的appCode",sourceLanguageCode = "zh_CN")
    private String appCode;

}
