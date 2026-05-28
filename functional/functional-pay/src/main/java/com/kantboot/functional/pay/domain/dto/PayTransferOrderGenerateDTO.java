package com.kantboot.functional.pay.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 生成转账订单的DTO
 */
@Data
@Accessors(chain = true)
public class PayTransferOrderGenerateDTO implements Serializable {

    /**
     * 用户id
     */
    private Long userAccountId;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 业务编码
     */
    private String businessCode;

    /**
     * 描述
     */
    private String description;

    /**
     * 货币
     * 例如：CNY
     * 例如：USD
     */
    private String currency;

}