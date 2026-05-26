package com.kantboot.fp.comment.domain.entity;

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
 * 轮播图实体类
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "fp_comment")
@DynamicUpdate
@DynamicInsert
public class FpComment
    extends BaseEntity
    implements Serializable {

    /**
     * 审核状态
     */
    @Column(name = "audit_status", length = 64)
    private String auditStatus;

    /**
     * 审核不通过的原因
     */
    @Column(name = "audit_fail_reason", columnDefinition = "TEXT")
    private String auditFailReason;

    /**
     * 编码
     * 用来区分不同的评论项
     */
    @Column(name = "code",length = 128)
    private String code;

    /**
     * 用户账号ID
     */
    @Column(name = "user_account_id_of_pusher")
    private Long userAccountIdOfPusher;

    /**
     * 用户账号
     */
    @OneToOne
    @JoinColumn(name = "user_account_id_of_pusher", insertable = false, updatable = false)
    private com.kantboot.user.account.domain.entity.UserAccount userAccountOfPusher;

    /**
     * 类型编码
     */
    @Column(name = "type_code",length = 64)
    private String typeCode;

    /**
     * 格式
     */
    @Type(JsonBinaryType.class)
    @Column(name="kt_format_of_view",columnDefinition = "TEXT")
    private Object ktFormatOfView;

    /**
     * 文字内容
     */
    @Column(name = "text_content",columnDefinition = "TEXT")
    private String textContent;

    /**
     * 关联内容
     */
    @Column(name = "related_content", columnDefinition = "TEXT")
    private String relatedContent;

    /**
     * 是否删除
     */
    @Column(name = "is_delete")
    private Boolean isDelete = false;

    /**
     * 最大ID
     * 不存放在数据库中，只用来搜索
     */
    @Transient
    private Long maxId;

    /**
     * 最小ID
     * 不存放在数据库中，只用来搜索
     */
    @Transient
    private Long minId;


}
