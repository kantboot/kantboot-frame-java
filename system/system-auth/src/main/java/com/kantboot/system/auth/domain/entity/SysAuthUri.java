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
 * 系统权限URI
 */
@Table(name="sys_auth_uri")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class SysAuthUri
        extends BaseI18nEntity
        implements Serializable {

    /**
     * URI
     */
    @Column(name = "uri",columnDefinition = "text")
    private String uri;

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

    /**
     * 无需登录
     * No need login
     */
    @Column(name = "no_need_login")
    private Boolean noNeedLogin;

    /**
     * 是否全员放行（需要登录）
     * Is all pass
     */
    @Column(name = "all_pass")
    private Boolean allPass;

}
