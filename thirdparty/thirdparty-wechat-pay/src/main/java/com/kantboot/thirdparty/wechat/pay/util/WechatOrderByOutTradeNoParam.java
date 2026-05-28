package com.kantboot.thirdparty.wechat.pay.util;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.thirdparty.wechat.pay.exception.WechatPayParamError;
import com.kantboot.thirdparty.wechat.pay.exception.WechatPaySignError;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.Serializable;

/**
 * 商户订单号查询订单参数
 */
@Data
@Slf4j
@Accessors(chain = true)
public class WechatOrderByOutTradeNoParam implements Serializable {

    /**
     * 【商户订单号】 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一。
     * 特殊规则：最小字符长度为6
     * 在请求时为路径参数
     */
    private String outTradeNO;

    /**
     * 商户号
     */
    private String mchid;

    @SneakyThrows
    public WechatOrderByOutTradeNoResult toResult(String certStr, String serialNo) {
        String uri = "/v3/pay/transactions/out-trade-no/";
        uri += this.getOutTradeNO();
        uri += "?mchid=" + this.getMchid();

        String url = "https://api.mch.weixin.qq.com" + uri;

        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = IdUtil.simpleUUID().toUpperCase();

        // 生成签名
        String data = "GET\n" +
                uri + "\n" +
                timeStamp + "\n" +
                nonceStr + "\n\n";
        String base64Signature = PemUtil.getBase64Signature(data, certStr);

        String authorization = "WECHATPAY2-SHA256-RSA2048 "
                + "mchid=\"" + this.getMchid() + "\","
                + "nonce_str=\"" + nonceStr + "\","
                + "signature=\"" + base64Signature + "\","
                + "timestamp=\"" + timeStamp + "\","
                + "serial_no=\"" + serialNo + "\"";

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
        String responseCode = jsonObject.getString("code") + "";
        if ("SIGN_ERROR".equals(responseCode)) {
            log.info("微信支付订单查询签名错误:{}", jsonObject.getString("message"));
            throw new WechatPaySignError(jsonObject.getString("message"));
        }
        if ("PARAM_ERROR".equals(responseCode)) {
            log.info("微信支付订单查询参数错误:{}", jsonObject.getString("message"));
            throw new WechatPayParamError(jsonObject.getString("message"));
        }
        return JSON.parseObject(response, WechatOrderByOutTradeNoResult.class);
    }


}
