package com.kantboot.thirdparty.wechat.pay.event.handler;

import com.kantboot.thirdparty.wechat.pay.service.IThirdpartyWechatPayRefundOrderService;
import com.kantboot.util.event.annotation.EventOn;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ThirdpartyWechatPayRefundHandler{

    @Resource
    private IThirdpartyWechatPayRefundOrderService service;

    @EventOn(code="FunctionalPayOrder:refundStart:wechatPay")
    public void handle(Long payOrderId){
        service.refund(payOrderId);
    }

}
