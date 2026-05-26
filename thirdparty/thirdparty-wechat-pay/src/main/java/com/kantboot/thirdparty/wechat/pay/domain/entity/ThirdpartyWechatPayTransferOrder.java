package com.kantboot.thirdparty.wechat.pay.domain.entity;

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
@Table(name = "thirdparty_wechat_pay_transfer_order")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class ThirdpartyWechatPayTransferOrder
    extends BaseEntity
    implements Serializable {

    /**
     * 商户ID
     */
    @Column(name = "mch_id")
    private String mchId;

    /**
     * 转账订单ID
     */
    @Column(name = "transfer_order_id")
    private Long transferOrderId;

    @Column(name = "app_id")
    private String outBillNo;

    /**
     * 文档里返回的 package_info，前端 wx.requestMerchantTransfer 用
     */
    @Column(name = "package_info", columnDefinition = "TEXT")
    private String packageInfo;

    /**
     * 转账单状态：WAIT_USER_CONFIRM 等
     */
    @Column(name = "state")
    private String state;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    /**
     * 金额
     */
    @Column(name="amount", precision = 19, scale = 4)
    private BigDecimal amount;

    /**
     * 原始返回体，调试用
     */
    @Column(name = "raw_body", columnDefinition = "TEXT")
    private String rawBody;



}
