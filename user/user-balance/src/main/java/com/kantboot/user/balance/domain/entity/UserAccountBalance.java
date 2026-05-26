package com.kantboot.user.balance.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseEntity;
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
import java.math.BigDecimal;

/**
 * 用户账号余额实体类
 * @author FangMoFang
 */
@Entity
@Getter
@Setter
@Table(name = "user_account_balance")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class UserAccountBalance
        extends BaseEntity
        implements Serializable {

    /**
     * 用户账号ID
     */
    @Column(name = "user_account_id")
    private Long userAccountId;

    /**
     * 余额类型编码
     */
    @Column(name = "balance_code", length = 64)
    private String balanceCode;

    /**
     * 数量
     */
    @Column(name = "t_number")
    private BigDecimal number;

}
