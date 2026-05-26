package com.kantboot.thirdparty.wechat.pay.domain.entity;

import com.alibaba.fastjson2.annotation.JSONField;
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
 * 微信支付退款订单实体
 */
@Entity
@Getter
@Setter
@Table(name = "thirdparty_wechat_pay_refund_order")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class ThirdpartyWechatPayRefundOrder
    extends BaseEntity
    implements Serializable {

    /**
     * 支付订单ID
     */
    @Column(name = "pay_order_id")
    private Long payOrderId;

    /**
     * 退款单号
     */
    @Column(name = "refund_order_id")
    private String outRefundNo;

    /**
     * 商户订单号
     */
    @Column(name = "out_trade_no")
    private String outTradeNo;

    /**
     * 货币
     */
    @Column(name = "currency")
    private String currency;

    /**
     * 金额
     */
    @Column(name = "amount")
    private Long amount;

    /**
     * 回调地址
     */
    @Column(name = "notify_url")
    private String notifyUrl;

    /**
     * 【微信订单号】 微信订单号
     */
    @Column(name = "transaction_id")
    private String transactionId;

    /**
     * 【退款渠道】 退款渠道
     *     ORIGINAL: 原路退款
     *     BALANCE: 退回到余额
     *     OTHER_BALANCE: 原账户异常退到其他余额账户
     *     OTHER_BANKCARD: 原银行卡异常退到其他银行卡
     */
    @JSONField(name = "channel")
    private String channel;

}
