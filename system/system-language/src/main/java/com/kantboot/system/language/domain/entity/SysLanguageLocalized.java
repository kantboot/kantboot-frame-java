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
 * 语言本地化实体类
 * 因为在识别语言时，可能会有多个语言对应一个语言编码，但是在不同的地方，可能会有不同的描述
 * 例如：zh_CN，可能在某个地方是zh，某个地方是zh_CN，所以这里需要一个再对应
 *
 * @author 方某方
 */
@Table(name = "sys_language_localized")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@I18nTopKey
public class SysLanguageLocalized
        extends BaseI18nEntity
        implements Serializable {

    /**
     * 语言编码
     */
    @Column(name = "code", unique = true, length = 64)
    private String code;

    /**
     * 对应的语言编码
     */
    @Column(name = "language_code", length = 10)
    private String languageCode;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

}
