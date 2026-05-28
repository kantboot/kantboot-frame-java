package com.kantboot.functional.pay.web.admin.controller;

import com.kantboot.functional.pay.domain.dto.PayOrderGenerateDTO;
import com.kantboot.functional.pay.domain.entity.FunctionalPayOrder;
import com.kantboot.functional.pay.service.IFunctionalPayOrderService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "支付订单管理",description = "支付订单管理",sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/functional-pay-web/admin/order")
public class FunctionalPayOrderControllerOfAdmin
    extends BaseAdminController<FunctionalPayOrder,Long> {

    @Resource
    private IFunctionalPayOrderService service;

    /**
     * 生成订单
     */
    @AuthInit(name = "生成订单",description = "生成订单",sourceLanguageCode = "zh_CN")
    @RequestMapping("/generate")
    public RestResult<?> generate(@RequestBody PayOrderGenerateDTO dto) {
        return RestResult.success(service.generate(dto), CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

}
