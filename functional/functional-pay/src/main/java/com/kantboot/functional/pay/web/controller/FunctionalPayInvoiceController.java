package com.kantboot.functional.pay.web.controller;

import com.kantboot.functional.pay.domain.entity.FunctionalPayInvoice;
import com.kantboot.functional.pay.service.IFunctionalPayInvoiceService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "发票", description = "发票", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/functional-pay-web/invoice")
public class FunctionalPayInvoiceController {

    @Resource
    private IFunctionalPayInvoiceService service;

    @RequestMapping("/applyInvoice")
    @AuthInit(name = "申请发票",description = "申请发票",sourceLanguageCode = "zh_CN",allPass = true)
    public RestResult<?> applyInvoice(@RequestBody FunctionalPayInvoice invoice) {
        service.applyInvoice(invoice);
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    @RequestMapping("/getInvoicedOPayOrderIdsBySelf")
    @AuthInit(name = "获取当前用户已开具发票的支付订单ID",description = "获取当前用户已开具发票的支付订单ID",sourceLanguageCode = "zh_CN",allPass = true)
    public RestResult<?> getInvoicedOPayOrderIdsBySelf() {
        return RestResult.success(service.getInvoicedOPayOrderIdsBySelf(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/getBySelf")
    @AuthInit(name = "获取当前用户发票",description = "获取当前用户发票",sourceLanguageCode = "zh_CN",allPass = true)
    public RestResult<?> getBySelf() {
        return RestResult.success(service.getBySelf(), CommonSuccessStateConsts.GET_SUCCESS);
    }


}
