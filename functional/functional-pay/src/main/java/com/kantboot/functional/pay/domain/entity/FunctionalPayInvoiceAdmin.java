package com.kantboot.functional.pay.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 发票管理
 */
@Entity
@Getter
@Setter
@Table(name = "functional_pay_invoice")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class FunctionalPayInvoiceAdmin
    extends BaseEntity
    implements Serializable {

    /**
     * 抬头
     */
    @Column(name = "title")
    private String title;

    /**
     * 抬头类型
     * enterprise-企业单位
     * personal-个人或非企业单位
     */
    @Column(name = "title_type_code", length = 64)
    private String titleTypeCode;

    /**
     * 纳税人识别号
     */
    @Column(name = "taxpayer_id")
    private String taxpayerId;

    /**
     * 地址
     */
    @Column(name = "address",columnDefinition = "TEXT")
    private String address;

    /**
     * 电话
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 开户行
     */
    @Column(name = "bank_name",columnDefinition = "TEXT")
    private String bankName;

    /**
     * 银行账号
     */
    @Column(name = "bank_account",columnDefinition = "TEXT")
    private String bankAccount;

    /**
     * 发票接收邮箱
     */
    @Column(name = "email",columnDefinition = "TEXT")
    private String email;


    /**
     * 发票号
     */
    @Column(name = "invoice_no")
    private String invoiceNo;

    /**
     * 金额
     */
    @Column(name = "amount")
    private BigDecimal amount;

    /**
     * 用户账号ID
     */
    @Column(name = "user_account_id")
    private Long userAccountId;

    /**
     * 发票文件
     */
    @Column(name = "file_id_of_invoice")
    private Long fileIdOfInvoice;

    /**
     * 是否已开开发票
     */
    @Column(name = "is_issued")
    private Boolean isIssued = false;


    /**
     * 支付订单IDS
     */
    @Type(JsonBinaryType.class)
    @Column(name = "pay_order_ids",columnDefinition = "TEXT")
    private List<Long> payOrderIds = new ArrayList<>();

    /**
     * 开票时间
     */
    @Column(name = "gmt_issue")
    private Date gmtIssue;

    /**
     * 开票时间时间戳
     */
    @Column(name = "gmt_issue_time")
    private Long gmtIssueTime;


    @ManyToMany
    @JoinTable(
        name = "functional_pay_invoice_order",
        joinColumns = @JoinColumn(name = "invoice_id"),
        inverseJoinColumns = @JoinColumn(name = "pay_order_id")
    )
    private List<FunctionalPayOrder> payOrders = new ArrayList<>();



}
