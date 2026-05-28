package com.kantboot.thirdparty.wechat.pay.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.thirdparty.wechat.pay.setting.ThirdpartyWechatPaySetting;
import com.wechat.pay.java.core.RSAPublicKeyConfig;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.http.*;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Slf4j
public class WechatTransferQuery implements Serializable {

    private ThirdpartyWechatPaySetting wechatPaySetting;

    @Data
    @Accessors(chain = true)
    public static class WechatTransferQueryResult
            implements Serializable {

        /**
         * 小程序appid
         */
        private String appid;

        /**
         * 创建时间
         */
        private String createTime;

        private String mchId;

        private String openid;

        private String outBillNo;

        /**
         * 状态
         * SUCCESS
         */
        private String state;

        private BigDecimal transferAmount;

        private String transferBillNo;

        private String transferRemark;

        private String updateTime;

        private String code;

        private String message;
    }

    public WechatTransferQueryResult getWechatTransferQueryResult(String outBillNo){

        HttpClient httpClient = new DefaultHttpClientBuilder()
                .config(new RSAPublicKeyConfig.Builder()
                        .merchantId(wechatPaySetting.getMchId())
                        .privateKey(wechatPaySetting.getPayPrivateKey())
                        .publicKey(wechatPaySetting.getPayPublicKey())
                        .publicKeyId(wechatPaySetting.getPayPublicKeyId())
                        .merchantSerialNumber(wechatPaySetting.getPayCertSerialNo())
                        .apiV3Key(wechatPaySetting.getMchKey())
                        .build())
                .okHttpClient(new OkHttpClient())
                .build();


        HttpHeaders headers = new HttpHeaders();
        headers.addHeader("Accept", MediaType.APPLICATION_JSON.getValue());
        headers.addHeader("Content-Type", MediaType.APPLICATION_JSON.getValue());
        headers.addHeader("Wechatpay-Serial", wechatPaySetting.getPayCertSerialNo());

        HttpRequest httpRequest = new HttpRequest.Builder()
                .httpMethod(HttpMethod.GET)
                .url("https://api.mch.weixin.qq.com/v3/fund-app/mch-transfer/transfer-bills/out-bill-no/" + outBillNo)
                .headers(headers)
                .build();

        try {
            HttpResponse<JSONObject> response = httpClient.execute(httpRequest, JSONObject.class);
            JSONObject body = response.getServiceResponse();

            return JSONObject.parseObject(JSON.toJSONString(body), WechatTransferQueryResult.class);
        } catch (ServiceException e) {
            String string = e.toString();
            if(string.contains("NOT_FOUND")){
                WechatTransferQueryResult wechatTransferQueryResult = new WechatTransferQueryResult();
                wechatTransferQueryResult.setCode("NOT_FOUND");
                wechatTransferQueryResult.setMessage("转账单不存在");
                return wechatTransferQueryResult;
            }
            throw e;
        }
    }






}
