package com.kantboot.functional.pay.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 退款失败DTO
 */
@Data
public class PayRefundFailDTO implements Serializable {

    /**
     * 支付订单ID
     */
    private Long payOrderId;


    /**
     * 退款失败原因编码
     */
    private String reasonCode;

    /**
     * 退款失败原因描述
     */
    private String reasonDescription;

    /**
     * 第三方回执
     */
    private String additionalInfo;

}
