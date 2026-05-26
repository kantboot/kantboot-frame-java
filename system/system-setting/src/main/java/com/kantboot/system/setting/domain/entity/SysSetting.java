package com.kantboot.system.setting.domain.entity;

import com.kantboot.util.admin.annotation.AdminColumnAnnotation;
import com.kantboot.util.base.control.domian.entity.BaseI18nEntity;
import com.kantboot.util.i18n.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

/**
 * 设置实体类
 * 用于存储系统设置
 * 包含设置的名称、分组编码、编码、值、描述、优先级等信息
 */
@Table(name = "sys_setting")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@I18nTopKey(key = "SysSetting")
public class SysSetting
        extends BaseI18nEntity
        implements Serializable {

    /**
     * 设置编码
     */
    @I18nCenterKey
    @Column(name = "code", length = 64, unique = true)
    private String code;

    /**
     * 设置名称
     */
    @I18nBottomKey
    @Column(name = "name")
    private String name;

    /**
     * 设置分组编码
     * 用于区分不同的设置分组
     * 例如：系统设置、用户设置、角色设置、邮箱设置等
     */
    @Column(name = "group_code", length = 64)
    private String groupCode;

    /**
     * 设置值
     * 要不限制大小，可以使用text类型
     */
    @Column(name = "value", columnDefinition = "TEXT")
    private String value;

    /**
     * 设置描述
     */
    @I18nBottomKey
    @Column(name = "description")
    private String description;

    /**
     * 语言编码
     */
    @Column(name = "source_language_code", length = 16)
    private String sourceLanguageCode;

    /**
     * 优先级
     */
    @Column(name = "priority")
    private Integer priority;



}
