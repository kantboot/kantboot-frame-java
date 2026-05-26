package com.kantboot.system.auth.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseI18nEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.List;

/**
 * 用户权限实体类
 * User Permission Entity
 * @author FangMoFang
 */
@Table(name="sys_auth_permission")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class SysAuthPermission
        extends BaseI18nEntity
        implements Serializable {

    /**
     * 权限编码
     */
    @Column(name = "code")
    private String code;

    /**
     * 权限名称
     * Permission Name
     */
    @Column(name = "name")
    private String name;

    /**
     * 权限描述
     * Permission Description
     */
    @Column(name = "description")
    private String description;

    /**
     * 权限组ID
     */
    @Column(name = "group_code")
    private String groupCode;

    @Type(JsonBinaryType.class)
    @Column(name = "uri_ids", columnDefinition = "TEXT")
    private List<Long> uriIds;

}