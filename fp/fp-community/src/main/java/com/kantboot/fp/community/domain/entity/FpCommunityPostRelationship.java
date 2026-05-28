package com.kantboot.fp.community.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kantboot.util.base.control.domian.entity.BaseEntity;
import com.kantboot.util.base.control.domian.entity.BaseI18nEntity;
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
 * 文章关系
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "fp_community_post_relationship")
@DynamicUpdate
@DynamicInsert
public class FpCommunityPostRelationship
    extends BaseEntity
    implements Serializable {

    /**
     * 文章ID
     */
    @Column(name = "post_id")
    private Long postId;

    /**
     * 点赞数量
     */
    @Column(name = "like_count")
    private Long likeCount;

    /**
     * 收藏数量
     */
    @Column(name = "collect_count")
    private Long collectCount;

    /**
     * 评论数量
     */
    @Column(name = "comment_count")
    private Long commentCount;

}
