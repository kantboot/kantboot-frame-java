package com.kantboot.system.dict.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseI18nEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

/**
 * 系统字典分组
 */
@Table(name="sys_dict_group")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class SysDictGroup
        extends BaseI18nEntity
        implements Serializable {

    /**
     * 字典编码
     */
    @Column(name = "code", length = 64, unique = true)
    private String code;

    /**
     * 字典名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 字典描述
     */
    @Column(name = "description")
    private String description;

}
