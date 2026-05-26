package com.kantboot.thirdparty.juhe.domain.entity;

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

/**
 * 第三方聚合数据API实体类
 * @author FangMoFang
 */
@Entity
@Getter
@Setter
@Table(name = "thirdparty_juhe_request")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class ThirdpartyJuheRequest
    extends BaseI18nEntity
    implements Serializable {

    /**
     * 编码
     * 用来标识API的唯一性
     */
    @Column(name = "code", length = 64, unique = true)
    private String code;

    /**
     * api的key
     */
    @Column(name = "t_key")
    private String key;

    /**
     * API的名称
     */
    @Column(name = "name")
    private String name;

    /**
     * API的描述
     */
    @Column(name = "description")
    private String description;

    /**
     * API的URI
     */
    @Column(name = "url",length = 1024)
    private String url;

    /**
     * API的请求方式
     */
    @Column(name = "request_method")
    private String requestMethod;

    /**
     * content-type
     */
    @Column(name = "content_type")
    private String contentType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "request_code", referencedColumnName = "code")
    private List<ThirdpartyJuheRequestParam> params;

    /**
     * 文档地址
     */
    @Column(name = "doc_url",length = 1024)
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