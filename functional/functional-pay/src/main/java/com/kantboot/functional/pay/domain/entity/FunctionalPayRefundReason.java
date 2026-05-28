package com.kantboot.functional.pay.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseEntity;
import com.kantboot.util.jpa.consts.IdGenerationTypeConsts;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

/**
 * 退款原因
 * @author 方某方
 */
@Entity
@Getter
@Setter
@Table(name = "functional_pay_refund_reason")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class FunctionalPayRefundReason
        extends BaseEntity
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
