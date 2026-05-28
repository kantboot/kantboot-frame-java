package com.kantboot.fp.community.domain.entity;

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
 * 文章点赞的用户
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "fp_community_post_like")
@DynamicUpdate
@DynamicInsert
public class FpCommunityPostLike
    extends BaseEntity
    implements Serializable {

    /**
     * 文章ID
     */
    @Column(name = "post_id")
    private Long postId;

    @OneToOne
    @JoinColumn(name = "post_id",insertable = false,updatable = false)
    private FpCommunityPost post;

    /**
     * 用户Id
     */
    @Column(name = "user_account_id")
    private Long userAccountId;

}
