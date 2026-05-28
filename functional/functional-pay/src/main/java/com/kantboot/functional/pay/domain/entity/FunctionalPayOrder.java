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
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单实体
 * @author FangMoFang
 */
@Entity
@Getter
@Setter
@Table(name = "functional_pay_order")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class FunctionalPayOrder
        extends FunctionalPayOrderAttrExt
        implements Serializable {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = IdGenerationTypeConsts.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;

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
     * 等待退款确认 refund_checking
     * 退款中 refunding
     * 已退款 refunded
     * 已取消 canceled
     * 异常 error
     */
    @Column(name="status_code")
    private String statusCode;

    /**
     * 支付后校验失败原因编码
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
     * 业务编码
     * 用于区分不同的业务类型
     * 例如：ovoClion
     */
    @Column(name="business_code")
    private String businessCode;

    /**
     * 支付方式编码
     * 例如：wechatPay
     * 例如：alipay
     */
    @Column(name="pay_method_code")
    private String payMethodCode;

    /**
     * 支付方式的附加信息
     */
    @Column(name="pay_method_additional_info",length = 30000)
    private String payMethodAdditionalInfo;

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

    /**
     * 退款原因编码
     */
    @Column(name="refund_reason_code")
    private String refundReasonCode;

    /**
     * 退款原因描述
     */
    @Column(name="refund_reason_description")
    private String refundReasonDescription;

    /**
     * 退款金额
     */
    @Column(name="refund_amount")
    private BigDecimal refundAmount;

    /**
     * 是否全额退款
     */
    @Column(name="is_all_refund")
    private Boolean isAllRefund;

    /**
     * 退款是是否扣除手续费
     */
    @Column(name="is_subtract_fee_when_refund")
    private Boolean isSubtractFeeWhenRefund;

    /**
     * 实际退款金额
     */
    @Column(name="actual_refund_amount")
    private BigDecimal actualRefundAmount;

    /**
     * 退款时的附加信息
     */
    @Column(name="refund_additional_info",columnDefinition = "TEXT")
    private String refundAdditionalInfo;

    /**
     * 退款失败原因编码
     */
    @Column(name="refund_fail_reason_code")
    private String refundFailReasonCode;

    /**
     * 退款失败原因描述
     */
    @Column(name="refund_fail_reason_description",columnDefinition = "TEXT")
    private String refundFailReasonDescription;
}
