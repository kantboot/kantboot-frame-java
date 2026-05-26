package com.kantboot.functional.pay.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class PayCompleteDTO
    implements Serializable {

    /**
     * 支付订单ID
     */
    private Long payOrderId;

    /**
     * 支付方式编码
     * 例如：wechatPay
     * 例如：alipay
     */
    private String payMethodCode;

    /**
     * 支付方式的附加信息
     */
    private String payMethodAdditionalInfo;

    /**
     * 手续费，由支付方式决定
     */
    private BigDecimal fee = BigDecimal.ZERO;


}
