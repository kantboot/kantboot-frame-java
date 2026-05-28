package com.kantboot.user.account.domain.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
 * 用户账号与角色关联表
 * User account and role association table
 * @author 方某方
 */
@Entity
@Getter
@Setter
@Table(name = "user_account_auth_role")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserAccountAuthRole implements Serializable {

    /**
     * 主键
     * Primary key
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = IdGenerationTypeConsts.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 创建时间
     * Create time
     */
    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     * Modified time
     */
    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;

    /**
     * 角色ID
     * Role ID
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 用户账号ID
     * User account ID
     */
    @Column(name = "user_account_id")
    private Long userAccountId;

}
