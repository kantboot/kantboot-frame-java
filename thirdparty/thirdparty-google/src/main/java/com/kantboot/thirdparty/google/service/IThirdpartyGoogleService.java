package com.kantboot.thirdparty.google.service;

import com.kantboot.thirdparty.google.setting.ThirdpartyGoogleSetting;
import com.kantboot.user.account.domain.vo.LoginVO;

public interface IThirdpartyGoogleService {

    ThirdpartyGoogleSetting getSetting();

    void setSetting(ThirdpartyGoogleSetting setting);

    String getClientId();

    LoginVO login(String redirectUri,String code);

}
