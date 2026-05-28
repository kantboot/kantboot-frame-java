package com.kantboot.thirdparty.github.service.impl;

import com.kantboot.system.setting.domain.entity.SysSetting;
import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.thirdparty.github.dao.repository.ThirdpartyGithubUserInfoRepository;
import com.kantboot.thirdparty.github.domain.entity.ThirdpartyGithubUserInfo;
import com.kantboot.thirdparty.github.service.IThirdpartyService;
import com.kantboot.thirdparty.github.setting.ThirdpartyGithubSetting;
import com.kantboot.thirdparty.github.util.ThirdpartyGithubUtil;
import com.kantboot.thirdparty.github.util.domain.GithubAccessToken;
import com.kantboot.thirdparty.github.util.domain.GithubUserEmail;
import com.kantboot.thirdparty.github.util.domain.GithubUserInfo;
import com.kantboot.user.account.domain.vo.LoginVO;
import com.kantboot.user.account.service.IUserAccountLoginService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ThirdpartyServiceImpl
    implements IThirdpartyService {

    @Resource
    private ISysSettingService settingService;

    @Resource
    private IUserAccountLoginService userAccountLoginService;

    @Resource
    private ThirdpartyGithubUserInfoRepository repository;

    @Override
    public ThirdpartyGithubSetting getSetting() {
        SysSetting clientId = settingService.getByCode("thirdpartyGithub.clientId");
        SysSetting clientSecret = settingService.getByCode("thirdpartyGithub.clientSecret");
        ThirdpartyGithubSetting setting = new ThirdpartyGithubSetting();
        setting.setClientId(clientId.getValue());
        setting.setClientSecret(clientSecret.getValue());
        return setting;
    }

    @Override
    public void setSetting(ThirdpartyGithubSetting setting) {
        settingService.setValue("thirdpartyGithub.clientId", setting.getClientId());
        settingService.setValue("thirdpartyGithub.clientSecret", setting.getClientSecret());
    }

    @Override
    public String getClientId() {
        ThirdpartyGithubSetting setting = getSetting();
        return setting.getClientId();
    }

    @Override
    public GithubAccessToken createAccessToken(String code) {
        ThirdpartyGithubSetting setting = getSetting();
        return ThirdpartyGithubUtil.createAccessToken(setting.getClientId(), setting.getClientSecret(), code);
    }

    @Override
    public LoginVO login(String code) {
        GithubAccessToken githubAccessToken = createAccessToken(code);
        String accessToken = githubAccessToken.getAccessToken();
        GithubUserInfo userInfo = ThirdpartyGithubUtil.getUserInfo(accessToken);
        List<GithubUserEmail> userEmails = ThirdpartyGithubUtil.getUserEmails(accessToken);
        ThirdpartyGithubUserInfo thirdpartyGithubUserInfo = repository.getByGithubId(userInfo.getId());
        if(thirdpartyGithubUserInfo == null){
            thirdpartyGithubUserInfo = new ThirdpartyGithubUserInfo();
        }
        thirdpartyGithubUserInfo.setGithubId(userInfo.getId());
        thirdpartyGithubUserInfo.setLogin(userInfo.getLogin());
        thirdpartyGithubUserInfo.setName(userInfo.getName());
        thirdpartyGithubUserInfo.setAvatarUrl(userInfo.getAvatarUrl());
        thirdpartyGithubUserInfo.setNodeId(userInfo.getNodeId());
        thirdpartyGithubUserInfo.setUrl(userInfo.getUrl());
        thirdpartyGithubUserInfo.setHtmlUrl(userInfo.getHtmlUrl());
        thirdpartyGithubUserInfo.setType(userInfo.getType());
        thirdpartyGithubUserInfo.setSiteAdmin(userInfo.getSiteAdmin());
        thirdpartyGithubUserInfo.setCompany(userInfo.getCompany());
        thirdpartyGithubUserInfo.setBlog(userInfo.getBlog());
        thirdpartyGithubUserInfo.setLocation(userInfo.getLocation());
        thirdpartyGithubUserInfo.setEmail(userInfo.getEmail());
        thirdpartyGithubUserInfo.setBio(userInfo.getBio());
        thirdpartyGithubUserInfo.setTwitterUsername(userInfo.getTwitterUsername());
        thirdpartyGithubUserInfo.setPublicRepos(userInfo.getPublicRepos());
        thirdpartyGithubUserInfo.setPublicGists(userInfo.getPublicGists());
        thirdpartyGithubUserInfo.setFollowers(userInfo.getFollowers());
        thirdpartyGithubUserInfo.setFollowing(userInfo.getFollowing());

        if(userInfo.getCreatedAt()!=null){
            thirdpartyGithubUserInfo.setGmtCreatedAt(Date.from(java.time.Instant.parse(userInfo.getCreatedAt())));
        }

        if(userInfo.getUpdatedAt()!=null){
            thirdpartyGithubUserInfo.setGmtUpdatedAt(Date.from(java.time.Instant.parse(userInfo.getUpdatedAt())));
        }

        if(!userEmails.isEmpty()){
            GithubUserEmail githubUserEmail = userEmails.getFirst();
            // 优先使用主邮箱登录
            if(githubUserEmail.getPrimary()!=null && githubUserEmail.getPrimary()){
                String email = githubUserEmail.getEmail();
                thirdpartyGithubUserInfo.setEmail(email);
                LoginVO loginVO = userAccountLoginService.loginByThirdpartyAndEmail("github", "githubId", String.valueOf(userInfo.getId()), email);
                thirdpartyGithubUserInfo.setUserAccountId(loginVO.getUserAccount().getId());
                repository.save(thirdpartyGithubUserInfo);
                return loginVO;
            }
        }
        LoginVO loginVO = userAccountLoginService.loginByThirdparty("github", "githubId", String.valueOf(userInfo.getId()));
        thirdpartyGithubUserInfo.setUserAccountId(loginVO.getUserAccount().getId());
        return loginVO;
    }

    @Override
    public ThirdpartyGithubUserInfo bind(String code) {
        return null;
    }
}
