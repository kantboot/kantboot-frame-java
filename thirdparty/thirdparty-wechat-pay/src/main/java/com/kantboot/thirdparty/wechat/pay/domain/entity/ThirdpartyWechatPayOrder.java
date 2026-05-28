package com.kantboot.thirdparty.wechat.pay.domain.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.kantboot.util.base.control.domian.entity.BaseEntity;
import jakarta.persistence.*;
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
@Table(name = "thirdparty_wechat_pay_order")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class ThirdpartyWechatPayOrder
    extends BaseEntity
    implements Serializable {

    /**
     * 支付订单ID
     */
    @Column(name = "pay_order_id")
    private Long payOrderId;

    /**
     * 服务商应用ID
     */
    @Column(name = "app_id")
    private String appId;

    /**
     * 服务商户号
     */
    @Column(name = "mch_id")
    private String mchId;

    @Column(name = "attach")
    private String attach;


    /**
     * 商户订单号
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;

    /**
     * 交易说明
     */
    @Column(name = "description")
    private String description;

    /**
     * 通知地址
     */
    @Column(name = "notify_url")
    private String notifyUrl;

    /**
     * 总金额
     */
    @Column(name = "amount_total")
    private BigDecimal amountTotal;

    /**
     * 币种
     */
    @Column(name = "amount_currency")
    private String amountCurrency;

    /**
     * 用户标识
     */
    @Column(name = "payer_openid")
    private String payerOpenid;

    /**
     * 支付状态编码
     */
    @Column(name = "pay_status_code")
    private String payStatusCode;

}
