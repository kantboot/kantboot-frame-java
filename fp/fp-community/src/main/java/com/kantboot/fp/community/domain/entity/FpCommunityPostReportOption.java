package com.kantboot.fp.community.domain.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
 * 帖子举报选项
 * 比如 广告，色情等
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "fp_community_post_report_option")
@DynamicUpdate
@DynamicInsert
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class FpCommunityPostReportOption
    extends BaseI18nEntity
        implements Serializable {

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 源语言编码
     */
    @Column(name = "source_language_code",length = 64)
    private String sourceLanguageCode;

}
