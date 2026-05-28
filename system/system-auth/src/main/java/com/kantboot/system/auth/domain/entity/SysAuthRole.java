package com.kantboot.system.auth.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseI18nEntity;
import com.kantboot.util.jpa.consts.IdGenerationTypeConsts;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户角色实体类
 * User Role Entity
 * @author FangMoFang
 */
@Table(name="sys_auth_role")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class SysAuthRole
        extends BaseI18nEntity
        implements Serializable {

    /**
     * 角色名称
     * Role Name
     */
    @Column(name = "name")
    private String name;

    /**
     * 角色编码
     */
    @Column(name = "code")
    private String code;

    /**
     * 角色描述
     * Role Description
     */
    @Column(name = "description")
    private String description;

    /**
     * 排序
     */
    @Column(name = "t_sort")
    private Integer sort;

    /**
     * 权限编码
     */
    @Type(JsonBinaryType.class)
    @Column(name = "permission_codes", columnDefinition = "TEXT")
    private java.util.List<String> permissionCodes;

}
