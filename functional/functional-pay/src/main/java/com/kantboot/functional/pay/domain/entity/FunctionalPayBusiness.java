package com.kantboot.functional.pay.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseI18nEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

/**
 * 支付对应的业务
 */
@Entity
@Getter
@Setter
@Table(name = "functional_pay_business")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class FunctionalPayBusiness
        extends BaseI18nEntity
        implements Serializable {

    /**
     * 编码
     */
    @Column(name = "code")
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
