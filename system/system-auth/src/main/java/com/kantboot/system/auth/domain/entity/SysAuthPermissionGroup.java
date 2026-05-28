package com.kantboot.system.auth.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseI18nEntity;
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
 * 权限组实体类
 * User Permission Entity
 * @author FangMoFang
 */
@Table(name="sys_auth_permission_group")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class SysAuthPermissionGroup
    extends BaseI18nEntity
    implements Serializable {

    /**
     * 编码
     */
    @Column(name = "code",columnDefinition = "TEXT")
    private String code;

    /**
     * 名称
     */
    @Column(name = "name",columnDefinition = "TEXT")
    private String name;

    /**
     * 描述
     */
    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

}