package com.kantboot.functional.pay.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.AccessType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 退款DTO
 */
@Data
@Accessors(chain = true)
public class PayRefundDTO implements Serializable {

    /**
     * 支付订单ID
     */
    private Long payOrderId;

    /**
     * 退款原因编码
     */
    private String reasonCode;

    /**
     * 退款原因描述
     */
    private String reasonDescription;

    /**
     * 退款金额
     */
    private BigDecimal amount;

    /**
     * 第三方回执
     * AdditionalInfo
     */
    private String additionalInfo;


}
