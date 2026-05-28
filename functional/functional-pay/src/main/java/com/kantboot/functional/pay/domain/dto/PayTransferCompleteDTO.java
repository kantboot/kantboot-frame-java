package com.kantboot.functional.pay.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class PayTransferCompleteDTO
    implements Serializable {

    /**
     * 转账订单ID
     */
    private Long payTransferOrderId;

    /**
     * 转账方式
     */
    private String transferMethodCode;

}
