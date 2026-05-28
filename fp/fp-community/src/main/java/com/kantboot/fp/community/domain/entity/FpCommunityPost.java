package com.kantboot.fp.community.domain.entity;

import com.kantboot.user.account.domain.entity.UserAccount;
import com.kantboot.util.base.control.domian.entity.BaseEntity;
import com.kantboot.util.i18n.annotation.I18nQuery;
import com.kantboot.util.i18n.annotation.I18nSave;
import com.kantboot.util.jpa.consts.IdGenerationTypeConsts;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "fp_community_post")
@DynamicUpdate
@DynamicInsert
public class FpCommunityPost
    extends FpCommunityPostAttrExt
    implements Serializable {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = IdGenerationTypeConsts.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 最后一次修改时间
     */
    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;

    @I18nSave
    @Type(JsonBinaryType.class)
    @Column(name = "i18n_save",columnDefinition = "TEXT")
    private Map<String, Map<String,Object>> i18nSave;

    @I18nQuery
    @Column(name = "i18n_query",columnDefinition = "TEXT")
    private String i18nQuery;


    /**
     * 上传者的用户账号ID
     */
    @Column(name = "user_account_id_of_pusher", length = 64)
    private Long userAccountIdOfPusher;

    @OneToOne
    @JoinColumn(name = "user_account_id_of_pusher", referencedColumnName = "id", insertable = false, updatable = false)
    private UserAccount userAccountOfPusher;

    /**
     * 文章标题
     */
    @Column(name = "title", length = 1000)
    private String title;

    /**
     * 封面图片的文件ID
     */
    @Column(name = "file_id_of_cover_image")
    private Long fileIdOfCoverImage;

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
     * 是否删除
     */
    @Column(name = "is_delete")
    private Boolean isDelete = false;

    /**
     * 格式
     */
    @Type(JsonBinaryType.class)
    @Column(name="kt_format_of_view",columnDefinition = "TEXT")
    private Object ktFormatOfView;

    /**
     * 对应的帖子关系（点赞、收藏、评论等）
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "relationship_id", referencedColumnName = "id")
    private FpCommunityPostRelationship relationship;

    /**
     * 用来过滤的编码集，用逗号分隔
     */
    @Column(name = "filter_code_collection", columnDefinition = "TEXT")
    private String filterCodeCollection;

    /**
     * 信息编码
     */
    @Column(name = "code", length = 128, unique = true)
    private String code;

    @Column(name="search_str",columnDefinition = "TEXT")
    private String searchStr;

    @Column(name = "text_content", columnDefinition = "TEXT")
    private String textContent;

    /**
     * 关联内容
     */
    @Column(name = "related_content", columnDefinition = "TEXT")
    private String relatedContent;

    @Column(name = "type_code", length = 64)
    private String typeCode;

    /**
     * 优先级
     */
    @Column(name = "t_priority")
    private Long priority = 0L;


    /**
     * 编辑次数
     */
    @Column(name = "edit_count")
    private Integer editCount = 0;


    /**
     * 是否被自己收藏
     */
    @Transient
    private Boolean isCollectedBySelf;

    /**
     * 是否已退款
     */
    @Column(name = "is_refund")
    private Boolean isRefund = false;

}
