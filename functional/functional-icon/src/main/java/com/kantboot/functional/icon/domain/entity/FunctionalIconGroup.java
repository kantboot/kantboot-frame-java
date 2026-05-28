package com.kantboot.functional.icon.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseI18nEntity;
import com.kantboot.util.i18n.annotation.I18nTopKey;
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

/**
 * 图标组
 */
@Entity
@Getter
@Setter
@Table(name = "functional_icon_group")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@I18nTopKey
public class FunctionalIconGroup
    extends BaseI18nEntity
    implements Serializable {

    /**
     * 编码
     */
    @Column(name = "code", unique = true)
    private String code;

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

}
