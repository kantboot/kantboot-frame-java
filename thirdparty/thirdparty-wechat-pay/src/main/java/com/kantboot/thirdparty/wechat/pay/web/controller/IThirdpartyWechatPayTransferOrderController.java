package com.kantboot.thirdparty.wechat.pay.web.controller;

import com.kantboot.thirdparty.wechat.pay.service.IThirdpartyWechatPayTransferOrderService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/thirdparty-wechat-pay/transferOrder")
public class IThirdpartyWechatPayTransferOrderController {

    @Resource
    private IThirdpartyWechatPayTransferOrderService service;

    @AuthInit(name = "获取转账返回",description = "获取转账返回",sourceLanguageCode = "zh_CN",allPass = true)
    @RequestMapping("/getTransferResultMiniprogram")
    public RestResult<?> getTransferResultMiniprogram(
            @RequestParam("orderId") Long orderId,
            @RequestParam("code") String code) {
        return RestResult.success(service.getTransferResultMiniprogram(orderId, code), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "检查转账是否成功",description = "检查转账是否成功",sourceLanguageCode = "zh_CN",allPass = true)
    @RequestMapping("/checkTransferSuccess")
    public RestResult<?> checkTransferSuccess(
            @RequestParam("orderId") Long orderId) {
        return RestResult.success(service.checkTransferSuccess(orderId), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "撤销转账",description = "撤销转账",sourceLanguageCode = "zh_CN",allPass = true)
    @RequestMapping("/cancelTransfer")
    public RestResult<?> cancelTransfer(
            @RequestParam("orderId") Long orderId) {
        return RestResult.success(service.cancelTransfer(orderId), CommonSuccessStateConsts.GET_SUCCESS);
    }


}
