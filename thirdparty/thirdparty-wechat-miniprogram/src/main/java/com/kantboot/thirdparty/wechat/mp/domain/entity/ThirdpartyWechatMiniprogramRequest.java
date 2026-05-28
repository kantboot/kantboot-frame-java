package com.kantboot.thirdparty.wechat.mp.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseI18nEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "thirdparty_wechat_miniprogram_request")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class ThirdpartyWechatMiniprogramRequest
        extends BaseI18nEntity
        implements Serializable {

    /**
     * code
     */
    private String code;

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    /**
     * host
     */
    private String host;

    /**
     * 请求地址
     */
    private String path;

    /**
     * 请求方式
     */
    @Column(name = "request_method", length = 16)
    private String requestMethod;

    /**
     * 描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id")
    private List<ThirdpartyWechatMiniprogramRequestQuery> queries;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id")
    private List<ThirdpartyWechatMiniprogramRequestBody> bodies;

    /**
     * 文档的URL
     */
    @Column(name = "doc_url", length = 1024)
    private String docUrl;

    /**
     * 是否启用缓存
     */
    @Column(name = "t_cache")
    private Boolean cache;

    /**
     * 是否永久缓存
     */
    @Column(name = "permanent_cache")
    private Boolean permanentCache;

    /**
     * 缓存时长（单位：毫秒）
     */
    @Column(name = "cache_duration")
    private Long cacheDuration;

}
