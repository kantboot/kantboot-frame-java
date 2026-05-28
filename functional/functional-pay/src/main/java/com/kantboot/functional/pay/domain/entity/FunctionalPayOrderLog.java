package com.kantboot.functional.pay.order.domain.entity;

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

/**
 * 订单实体日志
 * @author 方某方
 */
@Entity
@Getter
@Setter
@Table(name = "functional_pay_order_log")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class FunctionalPayOrderLog
        extends BaseEntity
        implements Serializable {

    /**
     * 订单id
     */
    @Column(name="pay_order_id")
    private Long payOrderId;

    /**
     * 发起人的用户id
     */
    @Column(name="user_account_id")
    private Long userAccountId;

    /**
     * 订单金额
     */
    @Column(name="amount")
    private BigDecimal amount;

    /**
     * 订单状态，驼峰式
     * 未支付 unpaid
     * 已支付 paid
     * 已退款 refunded
     * 已取消 canceled
     * 异常 error
     */
    @Column(name="status_code")
    private String statusCode;

    /**
     * 校验失败原因编码
     * 出于国际化与查找原因的考虑，使用编码
     */
    @Column(name="paid_check_fail_reason_code")
    private String paidAfterCheckFailReasonCode;

    /**
     * 校验失败原因描述
     * 如果非国际化项目，可以直接使用文字
     */
    @Column(name="paid_check_fail_reason_description")
    private String paidAfterCheckFailReasonDescription;

    /**
     * 产品编码
     * 用于区分不同的产品
     * 例如：oMoney
     * 例如：oVip
     */
    @Column(name="product_code")
    private String productCode;

    /**
     * 描述
     */
    @Column(name="description")
    private String description;

    /**
     * 货币
     * 例如：CNY
     * 例如：USD
     */
    @Column(name="currency")
    private String currency;

    /**
     * 支付方式编码
     * 例如：wechatPay
     * 例如：alipay
     */
    @Column(name="pay_method_code")
    private String payMethodCode;

    /**
     * 手续费
     */
    @Column(name="fee")
    private BigDecimal fee;

    /**
     * 实付金额
     */
    @Column(name="paid_amount")
    private BigDecimal paidAmount;

}
