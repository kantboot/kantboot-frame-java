package com.kantboot.thirdparty.wechat.pay.web.controller;

import com.kantboot.thirdparty.wechat.pay.service.IThirdpartyWechatOrderService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 关于微信支付的服务
 */
@AuthInit(name = "微信支付订单",description = "微信支付订单",sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/thirdparty-wechat-pay/order")
@Slf4j
public class ThirdpartyWechatPayOrderController {

    @Resource
    private IThirdpartyWechatOrderService service;

    /**
     * 获取支付结果
     */
    @RequestMapping("/getPayResultMiniprogram")
    @AuthInit(name = "获取支付结果",description = "获取支付结果",sourceLanguageCode = "zh_CN",allPass = true)
    public RestResult<?> getPayResultMiniprogram(
            @RequestParam("payOrderId") Long payOrderId,
            @RequestParam("wechatCode") String wechatCode) {
        return RestResult.success(service.getPayResultMiniprogram(payOrderId, wechatCode), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 检查支付是否成功
     * @param payOrderId 订单号
     * @return
     */
    @RequestMapping("/checkPaySuccess")
    @AuthInit(name = "获取支付结果",description = "获取支付结果",sourceLanguageCode = "zh_CN",allPass = true)
    public RestResult<?> checkPaySuccess(
            @RequestParam("payOrderId") Long payOrderId) {
        return RestResult.success(service.checkPaySuccess(payOrderId), CommonSuccessStateConsts.GET_SUCCESS);
    }

}
