package com.kantboot.tool.area.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseI18nEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "tool_area")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class ToolArea
        extends BaseI18nEntity
        implements Serializable {

    /**
     * 国家或地区名称 或 行政区划名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 国家或地区编码（三位代码） 或者 行政区划编码
     * ISO 3166-1
     */
    @Column(name = "code",length = 64,unique = true)
    private String code;

    /**
     * 当前仅有中国内地有效
     */
    @Column(length = 64)
    private String minCode;

    /**
     * 手机区号
     */
    @Column(name = "phone_area_code", length = 16)
    private String phoneAreaCode;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    /**
     * 国家或地区编码（二位代码）
     * ISO 3166-1
     * level 为 0 时有效
     */
    @Column(name = "alpha_2_code",length = 64)
    private String alpha2Code;

    /**
     * 国家或地区编码（三位代码）
     * ISO 3166-1
     * level 为 0 时有效
     */
    @Column(name = "alpha_3_code",length = 64)
    private String alpha3Code;

    /**
     * 拥有的行政区划级别
     * level 为 0 时有效
     */
    @Column(name = "level_count")
    private Integer levelCount;

    /**
     * 1级行政区称呼
     */
    @Column(name = "level_1_name")
    private String level1Name;

    /**
     * 2级行政区称呼
     */
    @Column(name = "level_2_name")
    private String level2Name;

    /**
     * 身份证前缀，用逗号隔开
     */
    @Column(name = "id_card_prefix")
    private String idCardPrefix;

    /**
     * 3级行政区称呼
     */
    @Column(name = "level_3_name")
    private String level3Name;

    /**
     * 4级行政区称呼
     */
    @Column(name = "level_4_name")
    private String level4Name;

    /**
     * 5级行政区称呼
     */
    @Column(name = "level_5_name")
    private String level5Name;

    /**
     * 行政区划级别
     */
    @Column(name = "level")
    private Integer level;

    /**
     * 1级行政区划编码
     */
    @Column(name = "level_1_code", length = 32)
    private String level1Code;

    /**
     * 2级行政区划编码
     */
    @Column(name = "level_2_code", length = 32)
    private String level2Code;

    /**
     * 3级行政区划编码
     */
    @Column(name = "level_3_code", length = 32)
    private String level3Code;

    @Column(name = "full_code", length = 32)
    private String fullCode;

    @Column(name = "parent_code", length = 64)
    private String parentCode;

}
