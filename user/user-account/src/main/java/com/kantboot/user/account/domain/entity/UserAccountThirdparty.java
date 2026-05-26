package com.kantboot.user.account.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

/**
 * 用户第三方信息
 * 用于存储用户的第三方信息
 * 如：github、wechat、qq、google等
 */
@Entity
@Getter
@Setter
@Table(name = "user_account_thirdparty")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class UserAccountThirdparty
    extends BaseEntity
    implements Serializable
{

    /**
     * 用户id
     */
    @Column(name = "user_account_id")
    private Long userAccountId;

    /**
     * 第三方平台编码
     * 如：github、wechat、qq、google等
     */
    @Column(name = "thirdparty_code")
    private String thirdpartyCode;

    /**
     * 识别标识的key
     */
    @Column(name = "t_key")
    private String key;

    /**
     * 采用的字段value
     */
    @Column(name = "t_value")
    private String value;

}
