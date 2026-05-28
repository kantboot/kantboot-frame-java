package com.kantboot.user.account.domain.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kantboot.util.i18n.annotation.I18nBottomKey;
import com.kantboot.util.i18n.annotation.I18nQuery;
import com.kantboot.util.i18n.annotation.I18nSave;
import com.kantboot.util.i18n.annotation.I18nTopKey;
import com.kantboot.util.jpa.consts.IdGenerationTypeConsts;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户账号实体类
 * @author 方某方
 */
@Entity
@Getter
@Setter
@Table(name = "user_account")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@I18nTopKey
public class UserAccount extends UserAccountAttrExtInUserModule
        implements Serializable {

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
     * 最后一次修改时间
     */
    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;

    @I18nSave
    @Type(JsonBinaryType.class)
    @Column(name = "i18n_save",columnDefinition = "TEXT")
    private Map<String, Map<String,Object>> i18nSave;

    @I18nQuery
    @Column(name = "i18n_query",columnDefinition = "TEXT")
    private String i18nQuery;

    /**
     * 用户名
     */
    @Column(name = "username")
    private String username;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 头像的文件id
     */
    @Column(name = "file_id_of_avatar")
    private Long fileIdOfAvatar;

    /**
     * 昵称
     */
    @I18nBottomKey
    @Column(name = "nickname")
    private String nickname;

    /**
     * 真实姓名
     */
    @I18nBottomKey
    @Column(name = "real_name")
    private String realName;

    /**
     * 身份证号
     */
    @Column(name = "id_card")
    private String idCard;

    /**
     * 是否实名认证通过
     */
    @Column(name = "is_real_name_auth")
    private Boolean isRealNameAuth = false;


    /**
     * 出生日期
     */
    @Column(name = "gmt_birthday")
    private Date gmtBirthday;

    /**
     * 手机号的国际区号
     */
    @Column(name = "phone_area_code")
    private String phoneAreaCode;

    /**
     * 手机号
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 个人介绍
     */
    @I18nBottomKey
    @Column(name = "introduction", length = 4096)
    private String introduction;

    /**
     * 性别编码
     * male:男
     * female:女
     */
    @Column(name = "gender_code")
    private String genderCode;


    /**
     * 直属码
     */
    @Column(name = "direct_code")
    private String directCode;

    /**
     * 邀请码
     */
    @Column(name = "invite_code")
    private String inviteCode;
    
    /**
     * 邀请人ID
     */
    @Column(name = "user_account_id_of_inviter")
    private Long userAccountIdOfInviter;

    /**
     * 是否为临时账号
     */
    @Column(name = "is_temporary")
    private Boolean isTemporary;

    /**
     * 是否为管理员
     */
    @Column(name = "is_admin")
    private Boolean isAdmin;

    /**
     * 是否为系统管理员
     */
    @Column(name = "is_system_admin")
    private Boolean isSystemAdmin;

    /**
     * 账号是否被禁止
     */
    @Column(name = "is_banned")
    private Boolean isBanned = false;

    @Type(JsonBinaryType.class)
    @Column(name = "role_ids", columnDefinition = "TEXT")
    private List<Long> roleIds;

}
