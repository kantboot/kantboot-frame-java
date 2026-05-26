package com.kantboot.thirdparty.discord.service.impl;

import cn.hutool.core.util.StrUtil;
import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.thirdparty.discord.dao.repository.ThirdpartyDiscordUserInfoRepository;
import com.kantboot.thirdparty.discord.domain.entity.ThirdpartyDiscordUserInfo;
import com.kantboot.thirdparty.discord.service.IThirdpartyDiscordService;
import com.kantboot.thirdparty.discord.setting.ThirdpartyDiscordSetting;
import com.kantboot.thirdparty.discord.util.ThirdpartyDiscordUtil;
import com.kantboot.thirdparty.discord.util.param.DiscordAccessToken;
import com.kantboot.thirdparty.discord.util.param.DiscordUserInfo;
import com.kantboot.user.account.domain.vo.LoginVO;
import com.kantboot.user.account.service.IUserAccountLoginService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ThirdpartyDiscordServiceImpl
        implements IThirdpartyDiscordService {

    @Resource
    private ISysSettingService settingService;

    @Resource
    private ThirdpartyDiscordUserInfoRepository repository;

    @Resource
    private IUserAccountLoginService userAccountLoginService;

    @Override
    public ThirdpartyDiscordSetting getSetting() {
        ThirdpartyDiscordSetting thirdpartyDiscordSetting = new ThirdpartyDiscordSetting();
        thirdpartyDiscordSetting.setClientId(settingService.getByCode("thirdpartyDiscord.clientId").getValue());
        thirdpartyDiscordSetting.setClientSecret(settingService.getByCode("thirdpartyDiscord.clientSecret").getValue());
        return thirdpartyDiscordSetting;
    }

    @Override
    public void setSetting(ThirdpartyDiscordSetting setting) {
        settingService.setValue("thirdpartyDiscord.clientId", setting.getClientId());
        settingService.setValue("thirdpartyDiscord.clientSecret", setting.getClientSecret());
    }

    @Override
    public String getClientId() {
        return getSetting().getClientId();
    }

    @Override
    public LoginVO login(String redirectUri, String code) {
        ThirdpartyDiscordSetting discordSetting = getSetting();
        String clientId = discordSetting.getClientId();
        String clientSecret = discordSetting.getClientSecret();
        DiscordAccessToken accessToken = ThirdpartyDiscordUtil.createAccessToken(clientId,
                clientSecret,
                redirectUri,
                code);
        DiscordUserInfo userInfo = ThirdpartyDiscordUtil.getUserInfo(accessToken.getAccessToken());
        ThirdpartyDiscordUserInfo discordUserInfo = repository.findByDiscordId(userInfo.getId());
        if (discordUserInfo == null) {
            discordUserInfo = new ThirdpartyDiscordUserInfo();
        }
        discordUserInfo.setDiscordId(userInfo.getId());
        discordUserInfo.setUsername(userInfo.getUsername());
        discordUserInfo.setAvatar(userInfo.getAvatar());
        discordUserInfo.setDiscriminator(userInfo.getDiscriminator());
        discordUserInfo.setPublicFlags(userInfo.getPublicFlags());
        discordUserInfo.setFlags(userInfo.getFlags());
        discordUserInfo.setBanner(userInfo.getBanner());
        discordUserInfo.setAccentColor(userInfo.getAccentColor());
        discordUserInfo.setGlobalName(userInfo.getGlobalName());
        discordUserInfo.setCollectibles(userInfo.getCollectibles());
        discordUserInfo.setBannerColor(userInfo.getBannerColor());
        discordUserInfo.setClan(userInfo.getClan());
        discordUserInfo.setPrimaryGuild(userInfo.getPrimaryGuild());
        discordUserInfo.setMfaEnabled(userInfo.getMfaEnabled());
        discordUserInfo.setLocale(userInfo.getLocale());
        discordUserInfo.setPremiumType(userInfo.getPremiumType());
        discordUserInfo.setEmail(userInfo.getEmail());
        discordUserInfo.setVerified(userInfo.getVerified());

        if (StrUtil.isNotEmpty(discordUserInfo.getEmail())) {
            LoginVO loginVO = userAccountLoginService.loginByThirdpartyAndEmail("discord", "discordId", discordUserInfo.getDiscordId(), discordUserInfo.getEmail());
            discordUserInfo.setUserAccountId(loginVO.getUserAccount().getId());
            repository.save(discordUserInfo);
            return loginVO;
        }

        LoginVO loginVO = userAccountLoginService.loginByThirdparty("discord", "discordId", discordUserInfo.getDiscordId());
        discordUserInfo.setUserAccountId(loginVO.getUserAccount().getId());
        repository.save(discordUserInfo);
        return loginVO;
    }
}
