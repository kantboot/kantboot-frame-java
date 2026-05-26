package com.kantboot.user.interrelation.domain.entity;

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

/**
 * 关注关系
 */
@Entity
@Getter
@Setter
@Table(name = "user_account_interrelation_follow")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class UserAccountInterrelationFollow implements Serializable {

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
     * 追随者
     */
    @Column(name = "user_account_id_of_follower")
    private Long userAccountIdOfFollower;

    /**
     * 被追随者
     */
    @Column(name = "user_account_id_of_followed")
    private Long userAccountIdOfFollowed;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_account_id_of_follower", referencedColumnName = "id", insertable = false, updatable = false)
    private UserAccount userAccountOfFollower;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_account_id_of_followed", referencedColumnName = "id", insertable = false, updatable = false)
    private UserAccount userAccountOfFollowed;

}
