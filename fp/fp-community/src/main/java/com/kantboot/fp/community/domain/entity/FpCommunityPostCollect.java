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
 * 帖子收藏
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "fp_community_post_collect")
@DynamicUpdate
@DynamicInsert
public class FpCommunityPostCollect
    extends BaseEntity
    implements Serializable {


    /**
     * 文章ID
     */
    @Column(name = "post_id")
    private Long postId;

    /**
     * 用户Id
     */
    @Column(name = "user_account_id")
    private Long userAccountId;

    @OneToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id", insertable = false, updatable = false)
    private FpCommunityPost post;

}
