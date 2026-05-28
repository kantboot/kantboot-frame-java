package com.kantboot.functional.pay.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseEntity;
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
 * 发票对应的支付订单
 */
@Entity
@Getter
@Setter
@Table(name = "functional_pay_invoice_order")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class FunctionalPayInvoiceOrder
    extends BaseEntity
    implements Serializable {

    /**
     * 支付订单ID
     */
    @Column(name = "pay_order_id")
    private Long payOrderId;

    /**
     * 发票ID
     */
    @Column(name = "invoice_id")
    private Long invoiceId;

    /**
     * 用户账号ID
     */
    @Column(name = "user_account_id")
    private Long userAccountId;

}
