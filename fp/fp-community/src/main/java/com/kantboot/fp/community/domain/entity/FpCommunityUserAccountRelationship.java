package com.kantboot.fp.community.domain.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
 * 记录用户访问可看的关系
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "fp_community_user_account_relationship")
@DynamicUpdate
@DynamicInsert
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class FpCommunityUserAccountRelationship
    extends BaseEntity
    implements Serializable {


    /**
     * 用户账号ID
     */
    @Column(name = "user_account_id")
    private Long userAccountId;

    /**
     * 访问次数
     */
    @Column(name = "visit_count")
    private Long visitCount;

    /**
     * 点赞次数
     */
    @Column(name = "like_count")
    private Long likeCount;

    /**
     * 帖子数量
     */
    @Column(name = "post_count")
    private Long postCount;

}
