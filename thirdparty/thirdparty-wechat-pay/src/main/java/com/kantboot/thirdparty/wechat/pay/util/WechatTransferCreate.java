package com.kantboot.thirdparty.wechat.pay.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.thirdparty.wechat.mp.service.IThirdpartyWechatMiniprogramService;
import com.kantboot.thirdparty.wechat.mp.setting.ThirdpartyWechatMiniprogramSetting;
import com.kantboot.thirdparty.wechat.pay.setting.ThirdpartyWechatPaySetting;
import com.wechat.pay.java.core.RSAPublicKeyConfig;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.http.*;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class WechatTransferCreate {

    private IThirdpartyWechatMiniprogramService thirdpartyWechatMiniprogramService;

    private ThirdpartyWechatMiniprogramSetting thirdpartyWechatMiniprogramSetting;

    private ThirdpartyWechatPaySetting wechatPaySetting;

    /**
     * 商户转账单号
     */
    private String outBillNo;

    /**
     * 场景ID
     */
    private String transferSceneId;

    /**
     * 用户的openid
     */
    private String openid;

    /**
     * 转账金额，单位：分
     */
    private BigDecimal transferAmount;

    /**
     * 转账备注
     * transfer_remark
     */
    private String transferRemark;

    /**
     * 回调URL
     * notify_url
     */
    private String notifyUrl;


    @Data
    @Accessors(chain = true)
    public static class TransferCreateResult {
        private boolean success;

        private String outBillNo;

        /**
         * 文档里返回的 package_info，前端 wx.requestMerchantTransfer 用
         */
        private String packageInfo;

        /**
         * 转账单状态：WAIT_USER_CONFIRM 等
         */
        private String state;

        private String errorCode;

        private String errorMessage;

        /**
         * 原始返回体，调试用
         */
        private String rawBody;
    }

    public TransferCreateResult createTransferBill(String outBillNo, String code, BigDecimal amount) {

        /**
         * 商户ID
         */
        String mchId = wechatPaySetting.getMchId();

        String openId = thirdpartyWechatMiniprogramService.getOpenIdByCode(code);

        String payPrivateKey = wechatPaySetting.getPayPrivateKey();

        String payPublicKey = wechatPaySetting.getPayPublicKey();

        String payPublicKeyId = wechatPaySetting.getPayPublicKeyId();

        String payCertSerialNo = wechatPaySetting.getPayCertSerialNo();

        String mchKey = wechatPaySetting.getMchKey();

        String appId = thirdpartyWechatMiniprogramSetting.getAppId();


        // 1) 构建微信 HttpClient（用 RSAAutoCertificateConfig）
        HttpClient httpClient = new DefaultHttpClientBuilder()
                .config(new RSAPublicKeyConfig.Builder()
                        .merchantId(mchId)
                        .privateKey(payPrivateKey)
                        .publicKey(payPublicKey)
                        .publicKeyId(payPublicKeyId)
                        .merchantSerialNumber(payCertSerialNo)
                        .apiV3Key(mchKey)
                        .build())
                .okHttpClient(new OkHttpClient())
                .build();

        // 2) 请求头
        HttpHeaders headers = new HttpHeaders();
        headers.addHeader("Accept", MediaType.APPLICATION_JSON.getValue());
        headers.addHeader("Content-Type", MediaType.APPLICATION_JSON.getValue());
        // 商户 API 证书序列号（不是平台证书），和商户后台看到的一致
        headers.addHeader("Wechatpay-Serial", payCertSerialNo);

        // 3) 请求体 map，字段严格按文档来
        Map<String, Object> body = new HashMap<>();
        // 小程序 appid
        body.put("appid", appId);
        // 商户转账单号
        body.put("out_bill_no", outBillNo);
        // 场景ID：佣金报酬/现金奖励等，按你实际申请的填
        body.put("transfer_scene_id", "1005");
        // 收款人 openid
        body.put("openid", openId);

        // 金额（分）
        body.put("transfer_amount", amount.multiply(new BigDecimal(100)).longValue());
        // 备注
        body.put("transfer_remark", this.transferRemark);
        System.err.println(this.notifyUrl);
        // 回调地址（用户确认收款后的回调）
        body.put("notify_url", this.notifyUrl);
        List<Map<String, String>> reportInfos = new ArrayList<>();
        Map<String, String> info1 = new HashMap<>();
        info1.put("info_type", "岗位类型");
        info1.put("info_content", "转账或提现");
        reportInfos.add(info1);

        Map<String, String> info2 = new HashMap<>();
        info2.put("info_type", "报酬说明");
        info2.put("info_content", "转账或提现");
        reportInfos.add(info2);

        body.put("transfer_scene_report_infos", reportInfos);

        String json = JSON.toJSONString(body);
        log.info("create transfer bill request body: {}", json);

        JsonRequestBody requestBody = new JsonRequestBody.Builder()
                .body(json)
                .build();

        HttpRequest httpRequest = new HttpRequest.Builder()
                .httpMethod(HttpMethod.POST)
                .url("https://api.mch.weixin.qq.com/v3/fund-app/mch-transfer/transfer-bills")
                .headers(headers)
                .body(requestBody)
                .build();

        try {
            HttpResponse<JSONObject> response = httpClient.execute(httpRequest, JSONObject.class);
            JSONObject respJson = response.getServiceResponse();
            // 正常返回：要的核心字段是 package_info 和 state（一般是 WAIT_USER_CONFIRM）
            String packageInfo = respJson.getString("package_info");
            String state = respJson.getString("state");

            return new TransferCreateResult()
                    .setSuccess(true)
                    .setOutBillNo(outBillNo)
                    .setPackageInfo(packageInfo)
                    .setState(state)
                    .setRawBody(respJson.toJSONString());
        } catch (ServiceException e) {
            String string = e.toString();
            if(string.contains("\"code\":\"NOT_ENOUGH\"")){
                return new TransferCreateResult()
                        .setSuccess(false)
                        .setOutBillNo(outBillNo)
                        .setErrorCode("NOT_ENOUGH")
                        .setErrorMessage("账户余额不足")
                        .setRawBody(string);
            } else {
                return new TransferCreateResult()
                        .setSuccess(false)
                        .setOutBillNo(outBillNo)
                        .setErrorCode("UNKNOWN_ERROR")
                        .setErrorMessage("未知错误：" + string)
                        .setRawBody(string);
            }
        }

    }


}
