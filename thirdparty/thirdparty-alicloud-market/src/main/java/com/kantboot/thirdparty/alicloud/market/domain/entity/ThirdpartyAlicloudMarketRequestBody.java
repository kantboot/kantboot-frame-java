package com.kantboot.thirdparty.alicloud.market.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseI18nEntity;
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
 * 阿里云市场API请求参数（body）
 * @author FangMoFang
 */
@Entity
@Getter
@Setter
@Table(name = "thirdparty_alicloud_market_request_body")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class ThirdpartyAlicloudMarketRequestBody
    extends BaseI18nEntity
    implements Serializable {

    /**
     * request的id
     */
    @Column(name = "request_id")
    private Long requestId;

    /**
     * 字段
     */
    @Column(name = "field", length = 64)
    private String field;

    /**
     * 参数类型
     * int/string/float/boolean等
     */
    @Column(name = "type")
    private String type;

    /**
     * 参数名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 参数描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 是否必填
     */
    @Column(name = "t_required")
    private Boolean required = false;

    /**
     * 默认值
     */
    @Column(name = "default_value",columnDefinition = "TEXT")
    private String defaultValue;

}