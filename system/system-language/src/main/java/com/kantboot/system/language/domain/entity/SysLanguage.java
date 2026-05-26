package com.kantboot.system.language.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseI18nEntity;
import com.kantboot.util.i18n.annotation.I18nTopKey;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

/**
 * 语言实体类
 * 用于多语言，用来存储语言的编码和名称
 * 例如：zh_CN、en_US
 *
 * @author 方某方
 */
@Table(name = "sys_language")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@I18nTopKey
public class SysLanguage
        extends BaseI18nEntity
        implements Serializable {

    /**
     * 语言编码
     */
    @Column(name = "code", unique = true, length = 64)
    private String code;

    /**
     * 语言名称，例如：中文、英文
     * 这里为了方便管理，用各自的语言存储各自的语言翻译
     * 例：中文(简体)，存储为：中文(简体)
     * 英语(美国)，存储为：English (United States)
     * 在勾选时，会根据当前语言显示对应的语言名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 描述
     * 这个字段，是为了开发阶段和管理阶段方便查看
     * 基本上用中文存储
     * 例如：中文(简体)、中文(繁体)
     */
    @Column(name = "description")
    private String description;

    /**
     * 是否支持
     * 用于勾选时，是否支持该语言
     * 如果不支持该语言，那么就不会显示在勾选框中
     */
    @Column(name = "is_support")
    private Boolean support;

}
