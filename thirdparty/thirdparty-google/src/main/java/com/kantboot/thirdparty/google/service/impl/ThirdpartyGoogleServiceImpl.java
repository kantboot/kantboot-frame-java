package com.kantboot.thirdparty.google.service.impl;

import cn.hutool.core.util.StrUtil;
import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.thirdparty.google.dao.repository.ThirdpartyGoogleUserInfoRepository;
import com.kantboot.thirdparty.google.domain.entity.ThirdpartyGoogleUserInfo;
import com.kantboot.thirdparty.google.service.IThirdpartyGoogleService;
import com.kantboot.thirdparty.google.setting.ThirdpartyGoogleSetting;
import com.kantboot.thirdparty.google.util.ThirdpartyGoogleUtil;
import com.kantboot.thirdparty.google.util.domain.GoogleAccessToken;
import com.kantboot.thirdparty.google.util.domain.GoogleUserInfo;
import com.kantboot.user.account.domain.vo.LoginVO;
import com.kantboot.user.account.service.IUserAccountLoginService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ThirdpartyGoogleServiceImpl
        implements IThirdpartyGoogleService {

    @Resource
    private ISysSettingService settingService;

    @Resource
    private ThirdpartyGoogleUserInfoRepository repository;

    @Resource
    private IUserAccountLoginService userAccountLoginService;

    @Override
    public ThirdpartyGoogleSetting getSetting() {
        ThirdpartyGoogleSetting thirdpartyGoogleSetting = new ThirdpartyGoogleSetting();
        thirdpartyGoogleSetting.setClientId(settingService.getByCode("thirdpartyGoogle.clientId").getValue());
        thirdpartyGoogleSetting.setClientSecret(settingService.getByCode("thirdpartyGoogle.clientSecret").getValue());
        return thirdpartyGoogleSetting;
    }

    @Override
    public void setSetting(ThirdpartyGoogleSetting setting) {
        settingService.setValue("thirdpartyGoogle.clientId", setting.getClientId());
        settingService.setValue("thirdpartyGoogle.clientSecret", setting.getClientSecret());
    }

    @Override
    public String getClientId() {
        ThirdpartyGoogleSetting setting = getSetting();
        return setting.getClientId();
    }

    @Override
    public LoginVO login(String redirectUri,String code) {
        ThirdpartyGoogleSetting setting = getSetting();
        String clientId = setting.getClientId();
        String clientSecret = setting.getClientSecret();
        GoogleAccessToken googleAccessToken
                = ThirdpartyGoogleUtil.createAccessToken(clientId, clientSecret, redirectUri, code);
        GoogleUserInfo googleUserInfo = ThirdpartyGoogleUtil.getUserInfo(googleAccessToken.getAccessToken());

        ThirdpartyGoogleUserInfo userInfo = repository.findByGoogleId(googleUserInfo.getId());
        if (userInfo == null) {
            userInfo = new ThirdpartyGoogleUserInfo();
        }
        userInfo.setGoogleId(googleUserInfo.getId());
        userInfo.setName(googleUserInfo.getName());
        userInfo.setEmail(googleUserInfo.getEmail());
        userInfo.setPicture(googleUserInfo.getPicture());
        userInfo.setGivenName(googleUserInfo.getGivenName());
        userInfo.setVerifiedEmail(googleUserInfo.getVerifiedEmail());
        if(StrUtil.isNotEmpty(userInfo.getEmail())&&userInfo.getVerifiedEmail()){
            LoginVO loginVO = userAccountLoginService.loginByThirdpartyAndEmail("google", "googleId", userInfo.getGoogleId(), userInfo.getEmail());
            userInfo.setUserAccountId(loginVO.getUserAccount().getId());
            repository.save(userInfo);
            return loginVO;
        }

        LoginVO loginVO = userAccountLoginService.loginByThirdparty("google", "googleId", userInfo.getGoogleId());
        userInfo.setUserAccountId(loginVO.getUserAccount().getId());
        repository.save(userInfo);
        return loginVO;
    }
}
