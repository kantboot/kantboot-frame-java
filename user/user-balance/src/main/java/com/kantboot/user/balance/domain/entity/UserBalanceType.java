package com.kantboot.user.balance.domain.entity;


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
 * 用户账号余额种类实体类
 * @author 方某方
 */
@Entity
@Getter
@Setter
@Table(name = "user_balance_type")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class UserBalanceType implements Serializable {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = IdGenerationTypeConsts.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 余额编码
     */
    @Column(name = "t_code", length = 64)
    private String code;

    /**
     * 余额名称
     */
    @Column(name = "t_name", length = 64)
    private String name;

    /**
     * 余额描述
     */
    @Column(name = "t_description", length = 64)
    private String description;

    /**
     * 排序优先级
     */
    @Column(name = "t_priority")
    private Integer priority;

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

}
