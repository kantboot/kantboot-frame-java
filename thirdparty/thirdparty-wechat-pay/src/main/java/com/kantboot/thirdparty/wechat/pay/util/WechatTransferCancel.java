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

@Data
@Slf4j
public class WechatTransferCancel implements Serializable {

    private ThirdpartyWechatPaySetting wechatPaySetting;

    @Data
    @Accessors(chain = true)
    public static class WechatTransferCancelResult implements Serializable {

        /**
         * 商户转账单号
         */
        private String outBillNo;

        /**
         * 微信转账单号
         */
        private String transferBillNo;

        /**
         * 状态
         * CANCELING / CANCELLED
         */
        private String state;

        /**
         * 更新时间
         */
        private String updateTime;

        /**
         * 错误码（本地封装）
         */
        private String code;

        /**
         * 错误信息（本地封装）
         */
        private String message;

        /**
         * 原始返回体，调试用
         */
        private String rawBody;
    }

    /**
     * 撤销转账
     * 只能在【用户确认收款之前】调用
     */
    public WechatTransferCancelResult cancelTransfer(String outBillNo) {

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
                .httpMethod(HttpMethod.POST)
                .url("https://api.mch.weixin.qq.com/v3/fund-app/mch-transfer/transfer-bills/out-bill-no/"
                        + outBillNo + "/cancel")
                .headers(headers)
                .build();

        try {
            HttpResponse<JSONObject> response =
                    httpClient.execute(httpRequest, JSONObject.class);

            JSONObject body = response.getServiceResponse();
            log.info("cancel transfer response: {}", body.toJSONString());

            return new WechatTransferCancelResult()
                    .setOutBillNo(body.getString("out_bill_no"))
                    .setTransferBillNo(body.getString("transfer_bill_no"))
                    .setState(body.getString("state"))
                    .setUpdateTime(body.getString("update_time"))
                    .setRawBody(body.toJSONString());

        } catch (ServiceException e) {
            String err = e.toString();
            log.error("cancel transfer error: {}", err);

            // 常见业务错误兜底
            if (err.contains("NOT_FOUND")) {
                return new WechatTransferCancelResult()
                        .setCode("NOT_FOUND")
                        .setMessage("转账单不存在或已失效")
                        .setRawBody(err);
            }

            if (err.contains("INVALID_REQUEST")) {
                return new WechatTransferCancelResult()
                        .setCode("INVALID_REQUEST")
                        .setMessage("转账状态不允许撤销（可能已被用户确认）")
                        .setRawBody(err);
            }

            if (err.contains("FREQUENCY_LIMIT_EXCEED")
                    || err.contains("RATELIMIT_EXCEEDED")) {
                return new WechatTransferCancelResult()
                        .setCode("RATE_LIMIT")
                        .setMessage("撤销请求过于频繁")
                        .setRawBody(err);
            }

            return new WechatTransferCancelResult()
                    .setCode("UNKNOWN_ERROR")
                    .setMessage("未知错误：" + err)
                    .setRawBody(err);
        }
    }
}
