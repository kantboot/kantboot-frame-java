package com.kantboot.thirdparty.wechat.mp.service.impl;

import cn.hutool.core.util.StrUtil;
import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.thirdparty.wechat.mp.service.IThirdpartyWechatMiniprogramService;
import com.kantboot.thirdparty.wechat.mp.setting.ThirdpartyWechatMiniprogramSetting;
import com.kantboot.thirdparty.wechat.mp.util.ThirdpartyWechatMiniprogramUtil;
import com.kantboot.thirdparty.wechat.mp.util.domain.ThirdpartyWechatMiniprogramPhone;
import com.kantboot.thirdparty.wechat.util.ThirdpartyWechatUtil;
import com.kantboot.thirdparty.wechat.util.domain.ThirdpartyWechatSession;
import com.kantboot.thirdparty.wechat.util.domain.ThirdpartyWechatToken;
import com.kantboot.user.account.domain.vo.LoginVO;
import com.kantboot.user.account.service.IUserAccountBindService;
import com.kantboot.user.account.service.IUserAccountLoginService;
import com.kantboot.util.cache.CacheUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ThirdpartyWechatMiniprogramServiceImpl
        implements IThirdpartyWechatMiniprogramService {

    @Resource
    private ISysSettingService settingService;

    @Resource
    private IUserAccountLoginService userAccountLoginService;

    @Resource
    private IUserAccountBindService userAccountBindService;

    @Resource
    private CacheUtil cacheUtil;

    @Override
    public String getAccessToken() {
        String accessToken = cacheUtil.get("thirdpartyWechatMiniprogramAccessToken");
        if (StrUtil.isNotEmpty(accessToken)) {
            return accessToken;
        }
        ThirdpartyWechatMiniprogramSetting setting = getSetting();
        String appId = setting.getAppId();
        String appSecret = setting.getAppSecret();
        ThirdpartyWechatToken token = ThirdpartyWechatUtil.createToken(appId, appSecret);
        Integer expiresIn = token.getExpiresIn();
        cacheUtil.setEx("thirdpartyWechatMiniprogramAccessToken", token.getAccessToken(), 3, TimeUnit.MINUTES);
        return token.getAccessToken();
    }

    public ThirdpartyWechatMiniprogramSetting getSetting() {
        ThirdpartyWechatMiniprogramSetting s = new ThirdpartyWechatMiniprogramSetting();
        s.setAppId(settingService.getByCode("thirdpartyWechatMiniprogram.appId").getValue());
        s.setAppSecret(settingService.getByCode("thirdpartyWechatMiniprogram.appSecret").getValue());
        return s;
    }

    @Override
    public void setSetting(ThirdpartyWechatMiniprogramSetting s) {
        settingService.setValue("thirdpartyWechatMiniprogram.appId", s.getAppId());
        settingService.setValue("thirdpartyWechatMiniprogram.appSecret", s.getAppSecret());
    }

    @Override
    public ThirdpartyWechatSession getByUserInfoCode(String code) {
        ThirdpartyWechatMiniprogramSetting setting = getSetting();
        String appId = setting.getAppId();
        String appSecret = setting.getAppSecret();
        return ThirdpartyWechatMiniprogramUtil.jscode2session(appId, appSecret, code);
    }

    @Override
    public String getOpenIdByCode(String code) {
        ThirdpartyWechatSession byUserInfoCode = getByUserInfoCode(code);
        return byUserInfoCode.getOpenId();
    }

    public LoginVO loginByUserInfo(String code) {

        ThirdpartyWechatMiniprogramSetting setting = getSetting();
        String appId = setting.getAppId();
        String appSecret = setting.getAppSecret();
        ThirdpartyWechatSession thirdpartyWechatSession = ThirdpartyWechatMiniprogramUtil.jscode2session(appId, appSecret, code);
        String unionId = thirdpartyWechatSession.getUnionId();
        String openId = thirdpartyWechatSession.getOpenId();
        if (StrUtil.isNotEmpty(unionId)) {
            return userAccountLoginService.loginByThirdparty("wechatMiniprogram", "unionId", unionId);
        }
        return userAccountLoginService.loginByThirdparty("wechatMiniprogram", "openId", openId);
    }

    @Override
    public LoginVO loginByPhone(String code) {
        String accessToken = getAccessToken();
        ThirdpartyWechatMiniprogramPhone phoneNumber = ThirdpartyWechatMiniprogramUtil.getPhoneNumber(accessToken, code);
        String countryCode = phoneNumber.getCountryCode();
        String phone = phoneNumber.getPhoneNumber();
        return userAccountLoginService.loginByPhone(countryCode, phone);
    }

    @Override
    public LoginVO loginByPhoneAndUserInfo(String phoneCode, String userInfoCode) {
        LoginVO loginVO = loginByPhone(phoneCode);
        ThirdpartyWechatMiniprogramSetting setting = getSetting();
        String appId = setting.getAppId();
        String appSecret = setting.getAppSecret();
        ThirdpartyWechatSession thirdpartyWechatSession = ThirdpartyWechatMiniprogramUtil.jscode2session(appId, appSecret, userInfoCode);
        String unionId = thirdpartyWechatSession.getUnionId();
        String openId = thirdpartyWechatSession.getOpenId();
        System.err.println(openId + "----ID:" + loginVO.getUserAccount().getRealName());
        try {
            if (StrUtil.isNotEmpty(unionId)) {
                userAccountBindService.bindByThirdparty(loginVO.getUserAccount().getId(), "wechatMiniprogram", "unionId", unionId);
            }
            userAccountBindService.bindByThirdparty(loginVO.getUserAccount().getId(), "wechatMiniprogram", "openId", openId);
        } catch (Exception e) {
            // 绑定失败不影响登录，所以不处理
        }
        return loginVO;
    }

    @Override
    public void bindByUserInfo(String code) {
        ThirdpartyWechatMiniprogramSetting setting = getSetting();
        String appId = setting.getAppId();
        String appSecret = setting.getAppSecret();
        ThirdpartyWechatSession thirdpartyWechatSession = ThirdpartyWechatMiniprogramUtil.jscode2session(appId, appSecret, code);
        String unionId = thirdpartyWechatSession.getUnionId();
        String openId = thirdpartyWechatSession.getOpenId();
        if (StrUtil.isNotEmpty(unionId)) {
            userAccountBindService.bindByThirdparty("wechatMiniprogram", "unionId", unionId);
        }
        userAccountBindService.bindByThirdparty("wechatMiniprogram", "openId", openId);
    }
}
