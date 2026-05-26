package com.kantboot.functional.pay.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class PayTransferFailDTO
    implements Serializable {

    /**
     * 转账订单Id
     */
    private Long transferOrderId;

    /**
     * 转账方式
     */
    private String transferMethodCode;

    /**
     * 失败原因编码
     */
    private String failReasonCode;

    /**
     * 失败原因描述
     */
    private String failReason;


}
