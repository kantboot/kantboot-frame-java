package com.kantboot.thirdparty.wechat.pay.util;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.kantboot.thirdparty.wechat.pay.exception.WechatPaySignError;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

@Data
@Accessors(chain = true)
public class WechatRefundsParam {

    /**
     * out_refund_no：商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     */
    @JSONField(name = "out_refund_no")
    private String outRefundNo;

    /**
     * out_trade_no：原支付交易对应的商户订单号。
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;

    /**
     * reason：退款原因说明
     */
    private String reason;

    @JSONField(name = "notify_url")
    private String  notifyUrl;

    @Data
    @Accessors(chain = true)
    public static class Amount {

        /**
         * 原支付金额
         * total：订单总金额，单位为分，只能为整数，详见支付金额
         */
        private Long total;

        /**
         * refund：退款金额，必须小于等于订单金额，单位为分，只能为整数，详见支付金额
         */
        private Long refund;

        /**
         * currency：货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY
         */
        private String currency;
        


     }

    /**
     * 退款金额
     */
    private Amount amount;


    /**
     * 退款并返回第一次的退款结果
     * @param certStr 证书
     * @param serialNo 序列号
     * @param mchId 商户号
     * @return 退款结果
     */
    @SneakyThrows
    public WechatRefundsResult createResult(String certStr,String serialNo,String mchId) {
        String timeStamp = String.valueOf(System.currentTimeMillis()/1000);
        String nonceStr = IdUtil.simpleUUID().toUpperCase();

        // 生成签名
        String data="POST\n" +
                "/v3/refund/domestic/refunds\n" +
                timeStamp+"\n" +
                nonceStr+"\n" +
                JSON.toJSONString(this)+"\n";
        String base64Signature = PemUtil.getBase64Signature(data, certStr);

        String authorization="WECHATPAY2-SHA256-RSA2048 "
                +"mchid=\""+ mchId +"\","
                +"nonce_str=\""+nonceStr+"\","
                +"signature=\""+base64Signature+"\","
                +"timestamp=\""+timeStamp+"\","
                +"serial_no=\""+serialNo+"\"";

        String url = "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds";

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
            throw new WechatPaySignError(jsonObject.getString("message"));
        }

        WechatRefundsResult result = JSON.parseObject(response, WechatRefundsResult.class);
        return result;
    }


    /**
     * 查询退款结果
     * 【GET】/v3/refund/domestic/refunds/{out_refund_no}
     */
    @SneakyThrows
    public WechatRefundsResult queryResult(String certStr,String serialNo,String mchId) {
        String timeStamp = String.valueOf(System.currentTimeMillis()/1000);
        String nonceStr = IdUtil.simpleUUID().toUpperCase();

        // 生成签名
        String data="GET\n" +
                "/v3/refund/domestic/refunds/"+outRefundNo+"\n" +
                timeStamp+"\n" +
                nonceStr+"\n" +
                "\n";
        String base64Signature = PemUtil.getBase64Signature(data, certStr);

        String authorization="WECHATPAY2-SHA256-RSA2048 "
                +"mchid=\""+ mchId +"\","
                +"nonce_str=\""+nonceStr+"\","
                +"signature=\""+base64Signature+"\","
                +"timestamp=\""+timeStamp+"\","
                +"serial_no=\""+serialNo+"\"";

        String url = "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds/"+outRefundNo;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", authorization)
                .get()
                .build();
        // 发送请求
        String response = client.newCall(request).execute().body().string();
        JSONObject jsonObject = JSON.parseObject(response);
        String responseCode =  jsonObject.get("code")+"";
        if(responseCode.equals("SIGN_ERROR")){
            throw new WechatPaySignError(jsonObject.getString("message"));
        }
        if(responseCode.equals("PARAM_ERROR")){
            throw new WechatPaySignError(jsonObject.getString("message"));
        }

        WechatRefundsResult result = JSON.parseObject(response, WechatRefundsResult.class);
        return result;
    }


}
