package com.kantboot.user.interrelation.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Table(name = "user_account_interrelation_black")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class UserAccountInterrelationBlack
    extends BaseEntity {

    /**
     * 用户账号ID
     */
    @Column(name="user_account_id")
    private Long userAccountId;

    /**
     * 被拉黑的用户账号ID
     */
    @Column(name="black_user_account_id")
    private Long blackUserAccountId;

    @OneToOne
    @JoinColumn(name = "user_account_id",insertable = false,updatable = false)
    private com.kantboot.user.account.domain.entity.UserAccount userAccount;

    @OneToOne
    @JoinColumn(name = "black_user_account_id",insertable = false,updatable = false)
    private com.kantboot.user.account.domain.entity.UserAccount blackUserAccount;

}
