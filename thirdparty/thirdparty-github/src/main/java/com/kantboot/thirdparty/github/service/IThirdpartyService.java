package com.kantboot.thirdparty.github.service;

import com.kantboot.thirdparty.github.domain.entity.ThirdpartyGithubUserInfo;
import com.kantboot.thirdparty.github.setting.ThirdpartyGithubSetting;
import com.kantboot.thirdparty.github.util.domain.GithubAccessToken;
import com.kantboot.user.account.domain.vo.LoginVO;

public interface IThirdpartyService {

    ThirdpartyGithubSetting getSetting();

    void setSetting(ThirdpartyGithubSetting setting);

    String getClientId();

    GithubAccessToken createAccessToken(String code);

    LoginVO login(String code);

    ThirdpartyGithubUserInfo bind(String code);

}