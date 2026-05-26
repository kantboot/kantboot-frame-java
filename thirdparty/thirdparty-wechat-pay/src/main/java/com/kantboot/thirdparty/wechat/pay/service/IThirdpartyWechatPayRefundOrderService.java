package com.kantboot.thirdparty.wechat.pay.service;

import java.math.BigDecimal;

public interface IThirdpartyWechatPayRefundOrderService {

    /**
     * 发起退款
     */
    void refund(Long payOrderId);

}
