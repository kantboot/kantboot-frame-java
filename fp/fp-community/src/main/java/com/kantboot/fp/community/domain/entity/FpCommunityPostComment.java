package com.kantboot.fp.community.domain.entity;

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

/**
 * 文章的用户评论
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "fp_community_post_comment")
@DynamicUpdate
@DynamicInsert
public class FpCommunityPostComment
        extends BaseEntity
        implements Serializable {

    /**
     * 文章ID
     */
    @Column(name = "post_id")
    private Long postId;

    /**
     * 上传者的用户账号ID
     */
    @Column(name = "user_account_id_of_uploader")
    private Long userAccountIdOfUploader;

    /**
     * 审核状态
     */
    @Column(name = "audit_status", length = 64)
    private String auditStatus;

    /**
     * 审核不通过的原因
     */
    @Column(name = "audit_file_reason", columnDefinition = "TEXT")
    private String auditFileReason;

    /**
     * 回复的评论
     */
    @Column(name = "comment_id_of_reply")
    private Long commentIdOfReply;

    /**
     * 格式
     */
    @Type(JsonBinaryType.class)
    @Column(name="kt_format_of_view",columnDefinition = "TEXT")
    private Object ktFormatOfView;


    @Column(name="search_str",columnDefinition = "TEXT")
    private String searchStr;

    /**
     * 用来过滤的编码集，用逗号分隔
     */
    @Column(name = "filter_code_collection", columnDefinition = "TEXT")
    private String filterCodeCollection;

}
