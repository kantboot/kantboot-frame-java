package com.kantboot.thirdparty.wechat.pay.service.impl;

import cn.hutool.core.util.StrUtil;
import com.kantboot.functional.pay.domain.dto.PayRefundDTO;
import com.kantboot.functional.pay.domain.entity.FunctionalPayOrder;
import com.kantboot.functional.pay.service.IFunctionalPayOrderService;
import com.kantboot.thirdparty.wechat.pay.domain.entity.ThirdpartyWechatPayOrder;
import com.kantboot.thirdparty.wechat.pay.service.IThirdpartyWechatOrderService;
import com.kantboot.thirdparty.wechat.pay.service.IThirdpartyWechatPayRefundOrderService;
import com.kantboot.thirdparty.wechat.pay.setting.ThirdpartyWechatPaySetting;
import com.kantboot.thirdparty.wechat.pay.util.WechatRefundsParam;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ThirdpartyWechatPayRefundOrderServiceImpl
    implements IThirdpartyWechatPayRefundOrderService {

    @Resource
    private IFunctionalPayOrderService payOrderService;

    @Resource
    private IThirdpartyWechatOrderService wechatOrderService;

    @Resource
    private ThirdpartyWechatPaySetting wechatPaySetting;

    @Override
    public void refund(Long payOrderId) {

        FunctionalPayOrder byId = payOrderService.getById(payOrderId);
        String currency = byId.getCurrency();
        if(StrUtil.isEmpty(currency)){
            currency = "CNY";
        }
        WechatRefundsParam wechatRefundsParam = new WechatRefundsParam();
        wechatRefundsParam.setOutRefundNo("refund-"+byId.getId());
        wechatRefundsParam.setAmount(new WechatRefundsParam.Amount()
                .setTotal(byId.getPaidAmount().multiply(new BigDecimal("100")).longValue())
                .setRefund(byId.getActualRefundAmount().multiply(new BigDecimal("100")).longValue())
                .setCurrency(currency)
        );

        wechatRefundsParam.setOutTradeNo(byId.getId()+"");
        wechatRefundsParam.createResult(wechatPaySetting.getPayPrivateKey(),
                wechatPaySetting.getPayCertSerialNo(),
                wechatPaySetting.getMchId());


        payOrderService.refundComplete(new PayRefundDTO()
                .setPayOrderId(payOrderId));
    }

}
