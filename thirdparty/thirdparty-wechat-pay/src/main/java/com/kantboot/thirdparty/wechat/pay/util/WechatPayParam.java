package com.kantboot.thirdparty.wechat.pay.util;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.kantboot.thirdparty.wechat.pay.exception.WechatPayParamError;
import com.kantboot.thirdparty.wechat.pay.exception.WechatPaySignError;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.Serializable;

/**
 * 微信支付的参数
 *
 * @author 方某方
 */
@Data
@Accessors(chain = true)
public class WechatPayParam implements Serializable {

    /**
     * 服务商应用ID
     */
    @JSONField(name = "appid")
    private String appid;

    /**
     * 服务商户号
     */
    @JSONField(name = "mchid")
    private String mchid;

    private String attach;


    /**
     * 商户订单号
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;


    /**
     * 交易说明
     */
    @JSONField(name = "description")
    private String description;

    /**
     * 通知地址
     */
    @JSONField(name = "notify_url")
    private String notifyUrl;

    /**
     * 金额
     */
    @JSONField(name = "amount")
    private Amount amount;

    /**
     * 支付者
     */
    @JSONField(name = "payer")
    private Payer payer;

    /**
     * 金额
     */
    @Data
    public static class Amount {
        /**
         * 总金额
         */
        @JSONField(name = "total")
        private Integer total;

        /**
         * 币种
         */
        @JSONField(name = "currency")
        private String currency = "CNY";
    }

    /**
     * 支付者
     */
    @Data
    public static class Payer {
        /**
         * 用户标识
         */
        @JSONField(name = "openid")
        private String openid;
    }

    @SneakyThrows
    public WechatPayResult createResult(String certStr,String serialNo) {
        String timeStamp = String.valueOf(System.currentTimeMillis()/1000);
        String nonceStr = IdUtil.simpleUUID().toUpperCase();

        // 生成签名
        String data="POST\n" +
                "/v3/pay/transactions/jsapi\n" +
                timeStamp+"\n" +
                nonceStr+"\n" +
                JSON.toJSONString(this)+"\n";
        System.err.println(data+"::::");
        String base64Signature = PemUtil.getBase64Signature(data, certStr);

        String authorization="WECHATPAY2-SHA256-RSA2048 "
                +"mchid=\""+this.getMchid()+"\","
                +"nonce_str=\""+nonceStr+"\","
                +"signature=\""+base64Signature+"\","
                +"timestamp=\""+timeStamp+"\","
                +"serial_no=\""+serialNo+"\"";

        String url = "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi";

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody
                .create(MediaType.parse("application/json;charset=utf-8"),
                        JSON.toJSONString(this));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", authorization)
                .post(body)
                .build();
        // 发送请求
        String response = client.newCall(request).execute().body().string();
        JSONObject jsonObject = JSON.parseObject(response);
        String responseCode =  jsonObject.get("code")+"";
        if(responseCode.equals("SIGN_ERROR")){
            throw new WechatPaySignError(jsonObject.getString("message"));
        }
        if(responseCode.equals("PARAM_ERROR")){
            throw new WechatPayParamError(jsonObject.getString("message"));
        }
        if(responseCode.equals("NO_AUTH")){
            throw new WechatPayParamError(jsonObject.getString("message"));
        }

        String prepayId = jsonObject.getString("prepay_id");


        String paySignAfter= this.getAppid()+"\n"
                +timeStamp+"\n"
                +nonceStr+"\n"
                +"prepay_id="+prepayId+"\n";

        // 获取支付签名
        String paySign=PemUtil.getBase64Signature(paySignAfter, certStr);
        WechatPayResult result = new WechatPayResult();
        result.setNonceStr(nonceStr);
        result.setTimeStamp(timeStamp);
        result.setPackageStr("prepay_id=" + prepayId);
        result.setSignType("RSA");
        result.setPaySign(paySign);

        return result;
    }


}
