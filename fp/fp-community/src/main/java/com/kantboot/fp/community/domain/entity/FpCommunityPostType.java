package com.kantboot.fp.community.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseI18nEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

/**
 * 帖子类型
 * 比如 社区帖子，广告，商城内帖子
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "fp_community_post_type")
@DynamicUpdate
@DynamicInsert
public class FpCommunityPostType
    extends BaseI18nEntity
    implements Serializable {

    /**
     * 编码
     */
    @Column(name="code", nullable = false,unique = true,length = 64)
    private String code;

    /**
     * 名称
     */
    @Column(name="name")
    private String name;

    /**
     * 描述
     */
    @Column(name="description", columnDefinition = "TEXT")
    private String description;


}