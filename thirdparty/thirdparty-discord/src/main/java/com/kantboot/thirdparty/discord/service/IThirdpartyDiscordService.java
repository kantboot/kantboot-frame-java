package com.kantboot.thirdparty.discord.service;

import com.kantboot.thirdparty.discord.setting.ThirdpartyDiscordSetting;
import com.kantboot.user.account.domain.vo.LoginVO;

public interface IThirdpartyDiscordService {

    ThirdpartyDiscordSetting getSetting();

    void setSetting(ThirdpartyDiscordSetting setting);

    String getClientId();

    LoginVO login(String redirectUri, String code);


}
