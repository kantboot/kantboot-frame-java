package com.kantboot.user.account.domain.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kantboot.util.base.control.controller.BaseAdminController;
import com.kantboot.util.base.control.domian.entity.BaseEntity;
import com.kantboot.util.jpa.consts.IdGenerationTypeConsts;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户账号令牌实体类
 * @author 方某方
 */
@Entity
@Getter
@Setter
@Table(name = "user_account_token")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserAccountToken
        extends BaseEntity
        implements Serializable {

    /**
     * 用户账号ID
     */
    @Column(name = "user_account_id")
    private Long userAccountId;

    /**
     * 令牌
     */
    @Column(name = "t_token", length = 1024)
    private String token;

    /**
     * 令牌过期时间
     */
    @Column(name = "gmt_expire")
    private Date gmtExpire;

    /**
     * ip
     */
    @Column(name = "ip")
    private String ip;


    /**
     * 浏览器
     */
    @Column(name = "browser", length = 255)
    private String browser;

    /**
     * 浏览器版本
     */
    @Column(name = "browser_version", length = 255)
    private String browserVersion;

    /**
     * 操作系统
     */
    @Column(name = "os", length = 255)
    private String os;

    /**
     * 操作系统版本
     */
    @Column(name = "os_version", length = 255)
    private String osVersion;

    /**
     * User-Agent
     */
    @Column(name = "user_agent", length = 1024)
    private String userAgent;


}
