package com.kantboot.thirdparty.wechat.mp.service;

import com.kantboot.thirdparty.wechat.mp.setting.ThirdpartyWechatMiniprogramSetting;
import com.kantboot.thirdparty.wechat.mp.util.ThirdpartyWechatMiniprogramUtil;
import com.kantboot.thirdparty.wechat.util.domain.ThirdpartyWechatSession;
import com.kantboot.user.account.domain.vo.LoginVO;

public interface IThirdpartyWechatMiniprogramService {

    String getAccessToken();

    ThirdpartyWechatMiniprogramSetting getSetting();

    void setSetting(ThirdpartyWechatMiniprogramSetting setting);

    ThirdpartyWechatSession getByUserInfoCode(String code);

    String getOpenIdByCode(String code);

    LoginVO loginByUserInfo(String code);

    LoginVO loginByPhone(String code);

    LoginVO loginByPhoneAndUserInfo(String phoneCode, String userInfoCode);

    void bindByUserInfo(String code);

}
