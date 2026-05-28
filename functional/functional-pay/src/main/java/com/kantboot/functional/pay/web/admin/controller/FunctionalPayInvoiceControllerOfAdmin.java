package com.kantboot.functional.pay.web.admin.controller;

import com.kantboot.functional.pay.domain.entity.FunctionalPayInvoice;
import com.kantboot.functional.pay.domain.entity.FunctionalPayInvoiceAdmin;
import com.kantboot.functional.pay.service.IFunctionalPayInvoiceService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "发票管理", description = "发票管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/functional-pay-web/admin/invoice")
public class FunctionalPayInvoiceControllerOfAdmin
    extends BaseAdminController<FunctionalPayInvoiceAdmin,Long> {

    @Resource
    private IFunctionalPayInvoiceService service;

    /**
     * 开具发票
     */
    @AuthInit(name = "开具发票", description = "开具发票", sourceLanguageCode = "zh_CN")
    @RequestMapping("/issueInvoice")
    public RestResult<?> issueInvoice(@RequestBody FunctionalPayInvoice invoice) {
        service.issueInvoice(invoice);
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

}
