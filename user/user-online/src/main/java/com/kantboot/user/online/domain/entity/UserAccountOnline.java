package com.kantboot.user.online.domain.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kantboot.user.account.domain.entity.UserAccount;
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

@Entity
@Getter
@Setter
@Table(name = "user_account_online")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserAccountOnline implements Serializable {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = IdGenerationTypeConsts.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;

    /**
     * 用户id
     */
    @Column(name = "user_account_id",unique = true)
    private Long userAccountId;

    /**
     * 是否在线
     */
    @Column(name = "is_online")
    private Boolean isOnline;

    /**
     * 是否隐身
     */
    @Column(name = "is_invisible")
    private Boolean isInvisible;

    /**
     * 最后一次心跳时间
     */
    @Column(name = "gmt_last_heartbeat")
    private Date gmtLastHeartbeat;

    /**
     * 最后一次在线时间
     */
    @Column(name = "gmt_last_online")
    private Date gmtLastOnline;

    /**
     * 最后一次离线时间
     */
    @Column(name = "gmt_last_offline")
    private Date gmtLastOffline;

    /**
     * 显示的是否在线
     */
    @Column(name = "is_online_of_show")
    private Boolean isOnlineOfShow;

    /**
     * 显示的最后一次在线时间
     */
    @Column(name = "gmt_last_online_of_show")
    private Date gmtLastOnlineOfShow;

    /**
     * ip
     */
    @Column(name = "ip")
    private String ip;

    /**
     * 设备
     */
    @Column(name = "user_agent")
    private String userAgent;

    /**
     * 浏览器
     */
    @Column(name = "browser")
    private String browser;

    /**
     * 场景
     */
    @Column(name = "scene")
    private String scene;

    /**
     * 关联用户账号
     */
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_account_id", insertable = false, updatable = false)
    private UserAccount userAccount;

}
