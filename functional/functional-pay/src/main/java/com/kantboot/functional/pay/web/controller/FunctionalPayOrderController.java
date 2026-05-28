package com.kantboot.functional.pay.web.controller;

import com.kantboot.functional.pay.domain.dto.PayOrderGenerateDTO;
import com.kantboot.functional.pay.domain.entity.FunctionalPayOrder;
import com.kantboot.functional.pay.service.IFunctionalPayOrderService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "支付订单管理",description = "支付订单管理",sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/functional-pay-web/order")
public class FunctionalPayOrderController {

    @Resource
    private IFunctionalPayOrderService service;

    /**
     * 获取已支付的订单详情
     */
    @AuthInit(name = "获取已支付的订单详情",description = "获取已支付的订单详情",sourceLanguageCode = "zh_CN",allPass = true)
    @RequestMapping("/getSelfOfPaid")
    public RestResult<?> getAllOfPaid() {
        return RestResult.success(service.getSelfOfPaid(), CommonSuccessStateConsts.GET_SUCCESS);
    }


}
