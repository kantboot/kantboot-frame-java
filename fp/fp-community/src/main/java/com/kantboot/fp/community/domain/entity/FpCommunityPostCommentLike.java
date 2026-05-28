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
 * 文章帖子点赞的用户
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "fp_community_post_comment_like")
@DynamicUpdate
@DynamicInsert
public class FpCommunityPostCommentLike
    extends BaseEntity
    implements Serializable {

    /**
     * 帖子ID
     */
    @Column(name = "comment_id")
    private Long commentId;

    /**
     * 用户Id
     */
    @Column(name = "user_account_id")
    private Long userAccountId;


}
