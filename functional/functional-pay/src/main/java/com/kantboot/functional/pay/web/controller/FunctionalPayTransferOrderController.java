package com.kantboot.functional.pay.web.controller;

import com.kantboot.functional.pay.service.IFunctionalPayTransferOrderService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "转账订单", description = "转账订单", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/functional-pay-web/transferOrder")
public class FunctionalPayTransferOrderController {

    @Resource
    private IFunctionalPayTransferOrderService service;

    @RequestMapping("/getSelfByStatusCode")
    @AuthInit(name = "根据状态码获取当前用户的转账订单", description = "根据状态码获取当前用户的转账订单", sourceLanguageCode = "zh_CN", allPass = true)
    public RestResult<?> getSelfByStatusCode(
            @RequestParam("statusCode") String statusCode) {
        return RestResult.success(service.getSelfByStatusCode(statusCode), CommonSuccessStateConsts.GET_SUCCESS);
    }

}
