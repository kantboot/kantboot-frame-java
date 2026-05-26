package com.kantboot.fp.community.domain.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kantboot.util.base.control.domian.entity.BaseEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "fp_community_post_report")
@DynamicUpdate
@DynamicInsert
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class FpCommunityPostReport
    extends BaseEntity
    implements Serializable {

    /**
     * 帖子id
     */
    @Column(name = "post_id")
    private Long postId;

    /**
     * 举报者的用户账号ID
     */
    @Column(name = "user_account_id")
    private Long userAccountId;

    /**
     * 举报选项的id
     */
    @Column(name = "option_id")
    private Long optionId;

    /**
     * 举报内容
     */
    @Type(JsonBinaryType.class)
    @Column(name = "kt_format_of_content",columnDefinition = "TEXT")
    private String ktFormatOfContent;

    @OneToOne
    @JoinColumn(name = "post_id",insertable = false,updatable = false)
    private FpCommunityPost post;

}
