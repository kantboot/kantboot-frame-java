package com.kantboot.thirdparty.github.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.thirdparty.github.util.domain.GithubAccessToken;
import com.kantboot.thirdparty.github.util.domain.GithubUserEmail;
import com.kantboot.thirdparty.github.util.domain.GithubUserInfo;
import com.kantboot.util.http.HttpSendUtil;
import com.kantboot.util.http.domain.config.HttpSendConfig;
import com.kantboot.util.rest.exception.BaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ThirdpartyGithubUtil {

    public static GithubAccessToken createAccessToken(String clientId,
                                                      String clientSecret,
                                                      String code) {
        String send = HttpSendUtil.send(new HttpSendConfig()
                .setMethod("POST")
                .setUrl("https://github.com/login/oauth/access_token")
                .setBody(Map.of("client_id", clientId, "client_secret", clientSecret, "code", code))
                .addHeader("Accept", "application/json")
        );

        System.err.println(send);
        JSONObject json = JSONObject.parseObject(send);
        if (StrUtil.isNotEmpty(json.getString("error"))) {
            throw BaseException.of("ThirdpartyGithubRequestError:createAccessToken",
                    json.getString("error_description"), "en");
        }

        GithubAccessToken accessToken = new GithubAccessToken();
        accessToken.setAccessToken(json.getString("access_token"));
        accessToken.setScope(json.getString("scope"));
        accessToken.setExpiresIn(json.getLong("expires_in"));
        accessToken.setTokenType(json.getString("token_type"));
        accessToken.setRefreshToken(json.getString("refresh_token"));
        return accessToken;
    }

    public static GithubUserInfo getUserInfo(String accessToken) {
        String send = HttpSendUtil.send(new HttpSendConfig()
                .setMethod("GET")
                .setUrl("https://api.github.com/user")
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Accept", "application/vnd.github+json")
        );

        JSONObject json = JSONObject.parseObject(send);
        if (json.containsKey("message")) {
            throw BaseException.of("ThirdpartyGithubRequestError:getUserInfo",
                    json.getString("message"), "en");
        }

        GithubUserInfo userInfo = new GithubUserInfo();
        userInfo.setLogin(json.getString("login"));
        userInfo.setId(json.getLong("id"));
        userInfo.setNodeId(json.getString("node_id"));
        userInfo.setAvatarUrl(json.getString("avatar_url"));
        userInfo.setGravatarId(json.getString("gravatar_id"));
        userInfo.setUrl(json.getString("url"));
        userInfo.setHtmlUrl(json.getString("html_url"));
        userInfo.setFollowersUrl(json.getString("followers_url"));
        userInfo.setFollowingUrl(json.getString("following_url"));
        userInfo.setGistsUrl(json.getString("gists_url"));
        userInfo.setStarredUrl(json.getString("starred_url"));
        userInfo.setSubscriptionsUrl(json.getString("subscriptions_url"));
        userInfo.setOrganizationsUrl(json.getString("organizations_url"));
        userInfo.setReposUrl(json.getString("repos_url"));
        userInfo.setEventsUrl(json.getString("events_url"));
        userInfo.setReceivedEventsUrl(json.getString("received_events_url"));
        userInfo.setType(json.getString("type"));
        userInfo.setUserViewType(json.getString("user_view_type"));
        userInfo.setSiteAdmin(json.getBoolean("site_admin"));
        userInfo.setName(json.getString("name"));
        userInfo.setCompany(json.getString("company"));
        userInfo.setBlog(json.getString("blog"));
        userInfo.setLocation(json.getString("location"));
        userInfo.setEmail(json.getString("email"));
        userInfo.setHireable(json.getString("hireable"));
        userInfo.setBio(json.getString("bio"));
        userInfo.setTwitterUsername(json.getString("twitter_username"));
        userInfo.setNotificationEmail(json.getString("notification_email"));
        userInfo.setPublicRepos(json.getInteger("public_repos"));
        userInfo.setPublicGists(json.getInteger("public_gists"));
        userInfo.setFollowers(json.getInteger("followers"));
        userInfo.setFollowing(json.getInteger("following"));
        userInfo.setCreatedAt(json.getString("created_at"));
        userInfo.setUpdatedAt(json.getString("updated_at"));

        return userInfo;
    }

    /**
     * 获取用户邮箱信息（需要申请额外的权限）
     */
    public static List<GithubUserEmail> getUserEmails(String accessToken) {
        String send = HttpSendUtil.send(new HttpSendConfig()
                .setMethod("GET")
                .setUrl("https://api.github.com/user/emails")
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Accept", "application/vnd.github+json")
        );

        JSONArray jsonArray = JSONArray.parseArray(send);

        List<GithubUserEmail> emails = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            GithubUserEmail email = new GithubUserEmail();
            email.setEmail(json.getString("email"));
            email.setPrimary(json.getBoolean("primary"));
            email.setVerified(json.getBoolean("verified"));
            email.setVisibility(json.getString("visibility"));
            emails.add(email);
        }
        return emails;
    }

}
