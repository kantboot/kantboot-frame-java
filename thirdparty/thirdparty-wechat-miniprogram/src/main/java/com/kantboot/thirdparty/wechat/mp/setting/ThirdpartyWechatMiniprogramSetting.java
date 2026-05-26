package com.kantboot.thirdparty.wechat.mp.setting;

import com.kantboot.util.setting.annotation.Setting;
import com.kantboot.util.setting.annotation.SettingGroup;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@SettingGroup(code="thirdpartyWechatMiniprogram", name="微信小程序配置")
public class ThirdpartyWechatMiniprogramSetting {

    /**
     * 小程序AppID
     */
    @Setting(code = "appId", name = "小程序AppID")
    private String appId;

    @Setting(code = "appSecret", name = "小程序AppSecret")
    private String appSecret;

}