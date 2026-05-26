package com.kantboot.system.dict.domain.entity;

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
 * 系统字典实体类
 * 用于存储系统中的各种字典数据
 */
@Table(name="sys_dict")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@I18nTopKey
public class SysDict
    extends BaseI18nEntity
    implements Serializable {

    /**
     * 字典分组code
     */
    @Column(name = "group_code", length = 64)
    private String groupCode;

    /**
     * 字典编码
     */
    @Column(name = "code", length = 3000)
    private String code;

    /**
     * 字典全路径编码
     * 例如：groupCode.code
     */
    @Column(name = "full_code", length = 3000)
    private String fullCode;

    /**
     * 字典值
     */
    @Column(name = "value",length = 3000)
    private String value;

    /**
     * 名称
     */
    @Column(name = "name", length = 3000)
    private String name;

    /**
     * 字典描述
     */
    @Column(name = "description")
    private String description;

}
