package com.kantboot.fp.carousel.domain.entity;

import com.alibaba.fastjson2.JSONObject;
import com.kantboot.util.base.control.domian.entity.BaseI18nEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
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
@Table(name = "fp_carousel")
@DynamicUpdate
@DynamicInsert
public class FpCarousel
        extends BaseI18nEntity
        implements Serializable {

    /**
     * 类型编码
     */
    @Column(name = "type_code",length = 32)
    private String typeCode;

    /**
     * 轮播图名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 轮播图描述
     */
    @Column(name = "description",length = 65535)
    private String description;

    /**
     * 图片的文件ID
     */
    @Column(name = "file_id_of_image")
    private Long fileIdOfImage;

    /**
     * 排序
     */
    @Column(name = "t_sort")
    private Long sort;

    /**
     * 链接
     */
    @Column(name = "link", columnDefinition = "TEXT")
    private String link;

    @Type(JsonBinaryType.class)
    @Column(name="kt_format_of_view",columnDefinition = "TEXT")
    private Object ktFormatOfView = new JSONObject();

    /**
     * 跳转方式
     * none: 不跳转
     * link: 跳转到链接
     * view: 跳转到视图
     */
    @Column(name = "target", length = 32)
    private String target;

    /**
     * 发布方式
     * 全网发布 all
     * 籍贯发布 domicile
     * 地域发布（所在地发布）current
     */
    @Column(name="push_method")
    private String pushMethod;

    /**
     * 发布地区编码
     */
    @Column(name = "area_code", length = 64)
    private String areaCode;

    /**
     * 发布的地区全称
     */
    @Column(name = "area_full_name", columnDefinition = "TEXT")
    private String areaFullName;

}
