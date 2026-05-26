package com.kantboot.system.setting.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseI18nEntity;
import com.kantboot.util.i18n.annotation.I18nQuery;
import com.kantboot.util.i18n.annotation.I18nSave;
import com.kantboot.util.i18n.annotation.I18nTopKey;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 配置实体分组实体类
 * 用于存储系统设置分组信息
 * 包含分组的编码、名称、描述、优先级等信息
 */
@Table(name = "sys_setting_group")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@I18nTopKey(key = "SysSettingGroup")
public class SysSettingGroup
        extends BaseI18nEntity
        implements Serializable {

    /**
     * 设置编码
     */
    @Column(name = "code", length = 64, unique = true)
    private String code;

    /**
     * 设置名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 设置描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 源语言编码
     */
    @Column(name = "source_language_code", length = 16)
    private String sourceLanguageCode;

    /**
     * 优先级
     */
    @Column(name = "priority")
    private Integer priority;

}
