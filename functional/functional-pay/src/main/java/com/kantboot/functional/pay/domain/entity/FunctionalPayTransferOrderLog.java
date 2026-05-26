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
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "functional_pay_transfer_order_log")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class FunctionalPayTransferOrderLog
    extends BaseEntity
    implements Serializable {

    /**
     * 转账订单ID
     */
    @Column(name = "transfer_order_id")
    private Long transferOrderId;

    /**
     * 转账给的用户账号ID
     */
    @Column(name = "user_account_id")
    private Long userAccountId;

    /**
     * 订单金额
     */
    @Column(name="amount", precision = 19, scale = 4)
    private BigDecimal amount;

    /**
     * 订单状态，驼峰式
     * 未完成 pending
     * 已完成 completed
     * 已取消 canceled
     * 异常 error
     */
    @Column(name="status_code")
    private String statusCode;

    /**
     * 描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 转账方式编码
     */
    @Column(name="transfer_method_code", length = 64)
    private String transferMethodCode;

    /**
     * 转账业务编码
     */
    @Column(name="transfer_business_code")
    private String transferBusinessCode;

    /**
     * 转账失败原因编码
     */
    @Column(name="fail_reason_code",length = 128)
    private String failReasonCode;

    /**
     * 转账失败原因
     */
    @Column(name="fail_reason", columnDefinition = "TEXT")
    private String failReason;

    /**
     * 货币
     */
    @Column(name="currency", length = 10)
    private String currency;


}
