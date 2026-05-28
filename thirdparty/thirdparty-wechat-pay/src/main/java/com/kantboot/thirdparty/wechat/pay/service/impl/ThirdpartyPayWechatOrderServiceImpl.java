package com.kantboot.thirdparty.wechat.pay.service.impl;

import cn.hutool.core.util.StrUtil;
import com.kantboot.functional.pay.domain.dto.PayCompleteDTO;
import com.kantboot.functional.pay.domain.entity.FunctionalPayOrder;
import com.kantboot.thirdparty.wechat.mp.service.IThirdpartyWechatMiniprogramService;
import com.kantboot.thirdparty.wechat.mp.setting.ThirdpartyWechatMiniprogramSetting;
import com.kantboot.thirdparty.wechat.pay.dao.repository.ThirdpartyWechatPayOrderRepository;
import com.kantboot.thirdparty.wechat.pay.domain.entity.ThirdpartyWechatPayOrder;
import com.kantboot.thirdparty.wechat.pay.service.IThirdpartyWechatOrderService;
import com.kantboot.thirdparty.wechat.pay.setting.ThirdpartyWechatPaySetting;
import com.kantboot.functional.pay.service.IFunctionalPayOrderService;
import com.kantboot.thirdparty.wechat.pay.util.PayWechatPayParam;
import com.kantboot.util.cache.CacheUtil;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAPublicKeyConfig;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Cache;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class ThirdpartyPayWechatOrderServiceImpl implements IThirdpartyWechatOrderService {

    public static final String PAY_METHOD_CODE_WECHAT = "wechatPay";

    @Resource
    private ThirdpartyWechatPaySetting wechatPaySetting;

    @Resource
    private ThirdpartyWechatPayOrderRepository repository;

    @Resource
    private IFunctionalPayOrderService payOrderService;

    @Resource
    private IThirdpartyWechatMiniprogramService thirdpartyWechatMiniprogramService;

    @Resource
    private ThirdpartyWechatMiniprogramSetting thirdpartyWechatMiniprogramSetting;

    @Resource
    private CacheUtil cacheUtil;

    @Override
    public Object getPayResultMiniprogram(Long payOrderId, String code) {
        String appId = thirdpartyWechatMiniprogramSetting.getAppId();
        String mchId = wechatPaySetting.getMchId();
        String payNotifyURL = wechatPaySetting.getPayNotifyURL();
        // 获取支付订单
        FunctionalPayOrder unpaidById = payOrderService.getUnpaidById(payOrderId);
        // 根据orderId获取微信的支付订单
        ThirdpartyWechatPayOrder payOrder = repository.findByPayOrderId(payOrderId);

        String outTradeNo = payOrderId+"";
        String openid = thirdpartyWechatMiniprogramService.getOpenIdByCode(code);
        if (payOrder==null){
            payOrder = new ThirdpartyWechatPayOrder();
            payOrder.setPayOrderId(payOrderId);
            payOrder.setAppId(appId);
            payOrder.setMchId(mchId);
            payOrder.setAttach(unpaidById.getDescription());
            payOrder.setDescription(unpaidById.getDescription());
            if(StrUtil.isEmpty(payNotifyURL)){
                payNotifyURL = "https://example.com/notify";
            }
            payOrder.setNotifyUrl(payNotifyURL);
            payOrder.setAmountTotal(unpaidById.getAmount());
            payOrder.setAmountCurrency(unpaidById.getCurrency());
            payOrder.setPayerOpenid(openid);
        }
        payOrder.setOutTradeNo(payOrderId+"");
        repository.save(payOrder);

        PayWechatPayParam wechatPayParamDTO = new PayWechatPayParam();
        // 设置微信小程序的appid
        wechatPayParamDTO.setAppid(appId);
        // 设置商户号
        wechatPayParamDTO.setMchid(mchId);
        // 设置订单号
        wechatPayParamDTO.setOutTradeNo(outTradeNo);
        // 设置金额
        wechatPayParamDTO.setAmountTotal(unpaidById.getAmount());
        // 设置币种
        wechatPayParamDTO.setAmountCurrency(unpaidById.getCurrency());
        // 设置交易说明
        wechatPayParamDTO.setDescription(unpaidById.getDescription());
        // 设置支付回调地址
        wechatPayParamDTO.setNotifyUrl(payNotifyURL);
        wechatPayParamDTO.setPayerOpenid(openid);
        // 更新支付方式
        payOrderService.updatePayMethodCodeById(payOrderId, PAY_METHOD_CODE_WECHAT);
//        return wechatPayParamDTO.createResult(wechatPaySetting.getPayPrivateKey(),payCertSerialNo);
        // 使用微信支付公钥的RSA配置
        Config config =
                new RSAPublicKeyConfig.Builder()
                        .merchantId(mchId)
                        .privateKey(wechatPaySetting.getPayPrivateKey())
                        .publicKey(wechatPaySetting.getPayPublicKey())
                        .publicKeyId(wechatPaySetting.getPayPublicKeyId())
                        .merchantSerialNumber(wechatPaySetting.getPayCertSerialNo())
                        .apiV3Key(wechatPaySetting.getMchKey())
                        .build();
        // 构建service
        JsapiServiceExtension service = new JsapiServiceExtension.Builder().config(config).build();
        // request.setXxx(val)设置所需参数，具体参数可见Request定义
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(unpaidById.getAmount().multiply(new BigDecimal(100)).intValue());
        amount.setCurrency(unpaidById.getCurrency());
        request.setAmount(amount);
        request.setAppid(thirdpartyWechatMiniprogramSetting.getAppId());
        Payer payer = new Payer();
        payer.setOpenid(openid);
        request.setPayer(payer);
        request.setMchid(mchId);
        request.setDescription(unpaidById.getDescription());
        request.setNotifyUrl(wechatPayParamDTO.getNotifyUrl());
        request.setOutTradeNo(wechatPayParamDTO.getOutTradeNo());

        // response包含了调起支付所需的所有参数，可直接用于前端调起支付
        PrepayWithRequestPaymentResponse response = service.prepayWithRequestPayment(request);

        String key = "ThirdpartyWechatPay:getPayResultMiniprogram:"+payOrderId;
        if (!cacheUtil.lock(key)){
            return response;
        }

        // 虚拟线程
        Thread.ofVirtual().name("ThirdpartyPayWechatOrderServiceImpl.getPayResultMiniprogram.checkPaySuccess").start(()->{
            Boolean paySuccess = false;
            int i = 0;
            while (!paySuccess){
                i++;
                if(i>15){
                    break;
                }
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                paySuccess = checkPaySuccess(payOrderId);
            }
        });
        return response;
    }

    @Override
    public Boolean checkPaySuccess(Long payOrderId) {
        FunctionalPayOrder byId = payOrderService.getById(payOrderId);
        if("paid".equals(byId.getStatusCode())){
            return true;
        }
        String appId = thirdpartyWechatMiniprogramSetting.getAppId();
        String mchId = wechatPaySetting.getMchId();
        String payNotifyURL = wechatPaySetting.getPayNotifyURL();
        String payCertSerialNo = wechatPaySetting.getPayCertSerialNo();
        // 获取支付订单
        FunctionalPayOrder unpaidById = payOrderService.getUnpaidById(payOrderId);
        // 根据orderId获取微信的支付订单
        ThirdpartyWechatPayOrder payOrder = repository.findByPayOrderId(payOrderId);
        ThirdpartyWechatPayOrder byPayOrderId = repository.findByPayOrderId(payOrderId);
        String openid = byPayOrderId.getPayerOpenid();
        String outTradeNo = byPayOrderId.getOutTradeNo();
        Config config =
                new RSAPublicKeyConfig.Builder()
                        .merchantId(mchId)
                        .privateKey(wechatPaySetting.getPayPrivateKey())
                        .publicKey(wechatPaySetting.getPayPublicKey())
                        .publicKeyId(wechatPaySetting.getPayPublicKeyId())
                        .merchantSerialNumber(wechatPaySetting.getPayCertSerialNo())
                        .apiV3Key(wechatPaySetting.getMchKey())
                        .build();

        JsapiServiceExtension service = new JsapiServiceExtension.Builder().config(config).build();

        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setOutTradeNo(outTradeNo);
        request.setMchid(mchId);

        try {
            Transaction response = service.queryOrderByOutTradeNo(request);
            if(Transaction.TradeStateEnum.SUCCESS.equals(response.getTradeState())){
                byPayOrderId.setPayStatusCode("paid");
                repository.save(byPayOrderId);
                // 通知订单为支付成功
                payOrderService.payComplete(
                        new PayCompleteDTO().setPayOrderId(payOrderId)
                                .setPayMethodCode(PAY_METHOD_CODE_WECHAT)
                );
                return true;
            }
            return false;
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public ThirdpartyWechatPayOrder getByPayOrderId(Long payOrderId) {
        return repository.findByPayOrderId(payOrderId);
    }
}
