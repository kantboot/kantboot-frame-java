package com.kantboot.thirdparty.github.domain.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kantboot.util.base.control.domian.entity.BaseEntity;
import com.kantboot.util.i18n.annotation.I18nTopKey;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "thirdparty_github_user_info")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@I18nTopKey
public class ThirdpartyGithubUserInfo
    extends BaseEntity
    implements Serializable {

    /**
     * 用户账号ID
     */
    @Column(name = "user_account_id")
    private Long userAccountId;

    /**
     * 登录名
     */
    @Column(name = "login")
    private String login;

    /**
     * 用户ID
     */
    @Column(name = "github_id",unique = true)
    private Long githubId;

    /**
     * 节点ID
     */
    @Column(name = "node_id")
    private String nodeId;

    /**
     * 头像URL
     */
    @Column(name = "avatar_url",columnDefinition = "TEXT")
    private String avatarUrl;

    /**
     * Gravatar ID
     */
    @Column(name = "gravatar_id",columnDefinition = "TEXT")
    private String gravatarId;

    /**
     * API URL
     */
    @Column(name = "url",columnDefinition = "TEXT")
    private String url;

    /**
     * GitHub页面URL
     */
    @Column(name = "html_url",columnDefinition = "TEXT")
    private String htmlUrl;

    /**
     * 粉丝URL
     */
    @Column(name = "followers_url",columnDefinition = "TEXT")
    private String followersUrl;

    /**
     * 关注URL
     */
    @Column(name = "following_url",columnDefinition = "TEXT")
    private String followingUrl;

    /**
     * Gists URL
     */
    @Column(name = "gists_url",columnDefinition = "TEXT")
    private String gistsUrl;

    /**
     * 星标项目URL
     */
    @Column(name = "starred_url",columnDefinition = "TEXT")
    private String starredUrl;

    /**
     * 订阅URL
     */
    @Column(name = "subscriptions_url",columnDefinition = "TEXT")
    private String subscriptionsUrl;

    /**
     * 组织URL
     */
    @Column(name = "organizations_url",columnDefinition = "TEXT")
    private String organizationsUrl;

    /**
     * 仓库URL
     */
    @Column(name = "repos_url",columnDefinition = "TEXT")
    private String reposUrl;

    /**
     * 事件URL
     */
    @Column(name = "events_url",columnDefinition = "TEXT")
    private String eventsUrl;

    /**
     * 接收事件URL
     */
    @Column(name = "received_events_url",columnDefinition = "TEXT")
    private String receivedEventsUrl;

    /**
     * 用户类型
     */
    @Column(name = "type")
    private String type;

    /**
     * 用户视图类型
     */
    @Column(name = "user_view_type")
    private String userViewType;

    /**
     * 是否是站点管理员
     */
    @Column(name = "site_admin")
    private Boolean siteAdmin;

    /**
     * 姓名
     */
    @Column(name = "name")
    private String name;

    /**
     * 公司
     */
    @Column(name = "company")
    private String company;

    /**
     * 博客地址
     */
    @Column(name = "blog",columnDefinition = "TEXT")
    private String blog;

    /**
     * 位置
     */
    @Column(name = "location")
    private String location;

    /**
     * 邮箱
     */
    @Column(name = "email",columnDefinition = "TEXT")
    private String email;

    /**
     * 是否可雇佣
     */
    @Column(name = "hireable")
    private String hireable;

    /**
     * 个人简介
     */
    @Column(name = "bio",columnDefinition = "TEXT")
    private String bio;

    /**
     * Twitter用户名
     */
    @Column(name = "twitter_username")
    private String twitterUsername;

    /**
     * 通知邮箱
     */
    @Column(name = "notification_email",columnDefinition = "TEXT")
    private String notificationEmail;

    /**
     * 公开仓库数
     */
    @Column(name = "public_repos")
    private Integer publicRepos;

    /**
     * 公开Gists数
     */
    @Column(name = "public_gists")
    private Integer publicGists;

    /**
     * 粉丝数
     */
    @Column(name = "followers")
    private Integer followers;

    /**
     * 关注数
     */
    @Column(name = "following")
    private Integer following;

    /**
     * 创建时间
     */
    @Column(name = "gmt_created_at")
    private Date gmtCreatedAt;

    /**
     * 更新时间
     */
    @Column(name = "gmt_updated_at")
    private Date gmtUpdatedAt;


}
