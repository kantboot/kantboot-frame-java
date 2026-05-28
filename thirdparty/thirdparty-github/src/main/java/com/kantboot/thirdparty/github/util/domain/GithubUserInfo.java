package com.kantboot.thirdparty.github.util.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class GithubUserInfo implements Serializable {

    /**
     * 登录名
     */
    private String login;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 节点ID
     */
    private String nodeId;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * Gravatar ID
     */
    private String gravatarId;

    /**
     * API URL
     */
    private String url;

    /**
     * GitHub页面URL
     */
    private String htmlUrl;

    /**
     * 粉丝URL
     */
    private String followersUrl;

    /**
     * 关注URL
     */
    private String followingUrl;

    /**
     * Gists URL
     */
    private String gistsUrl;

    /**
     * 星标项目URL
     */
    private String starredUrl;

    /**
     * 订阅URL
     */
    private String subscriptionsUrl;

    /**
     * 组织URL
     */
    private String organizationsUrl;

    /**
     * 仓库URL
     */
    private String reposUrl;

    /**
     * 事件URL
     */
    private String eventsUrl;

    /**
     * 接收事件URL
     */
    private String receivedEventsUrl;

    /**
     * 用户类型
     */
    private String type;

    /**
     * 用户视图类型
     */
    private String userViewType;

    /**
     * 是否是站点管理员
     */
    private Boolean siteAdmin;

    /**
     * 姓名
     */
    private String name;

    /**
     * 公司
     */
    private String company;

    /**
     * 博客地址
     */
    private String blog;

    /**
     * 位置
     */
    private String location;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 是否可雇佣
     */
    private String hireable;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * Twitter用户名
     */
    private String twitterUsername;

    /**
     * 通知邮箱
     */
    private String notificationEmail;

    /**
     * 公开仓库数
     */
    private Integer publicRepos;

    /**
     * 公开Gists数
     */
    private Integer publicGists;

    /**
     * 粉丝数
     */
    private Integer followers;

    /**
     * 关注数
     */
    private Integer following;

    /**
     * 创建时间
     */
    private String createdAt;

    /**
     * 更新时间
     */
    private String updatedAt;
}