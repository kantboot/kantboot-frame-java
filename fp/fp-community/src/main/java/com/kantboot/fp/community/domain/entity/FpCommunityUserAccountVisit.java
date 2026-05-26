package com.kantboot.fp.community.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

/**
 * 用户访问的数据
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "fp_community_user_account_visit")
@DynamicUpdate
@DynamicInsert
public class FpCommunityUserAccountVisit
    extends BaseEntity
    implements Serializable {

    /**
     * 用户账号ID
     */
    @Column(name = "user_account_id")
    private Long userAccountId;

    /**
     * 访客的用户账号ID
     */
    @Column(name = "user_account_id_of_visitor")
    private Long userAccountIdOfVisitor;

}
