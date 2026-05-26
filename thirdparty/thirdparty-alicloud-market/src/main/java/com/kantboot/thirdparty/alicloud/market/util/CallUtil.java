package com.kantboot.thirdparty.alicloud.market.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.thirdparty.alicloud.market.param.ThirdpartyAlicloudMarketParam;
import com.kantboot.util.rest.exception.BaseException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

public class CallUtil {

    public static Object call(ThirdpartyAlicloudMarketParam param) {
        String host = param.getHost();
        String path = param.getPath();
        String method = param.getMethod();
        if(StrUtil.isEmpty(method)){
            method = "POST";
        }
        method = method.toUpperCase();
        String appcode = param.getAppCode();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        try {
            HttpResponse response = null;
            if("POST".equals(method)){
                response = HttpUtil.doPost(host, path, method, headers, param.getQueries(), param.getBodies());
            }else{
                response = HttpUtil.doGet(host, path, method, headers, param.getQueries());
            }
            String string = response.toString();
            String[] stringSplit = string.split(" ");
            // 获取第二个字符串
            String s = stringSplit[1];
            if(!("200".equals(s))){
                throw BaseException.of("alicloud.callErrorRequest:"+s, "请求阿里云市场失败:"+s,"zh_CN");
            }
            //获取response的body
            String string1 = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSONObject.parseObject(string1);
            return jsonObject;
        } catch (BaseException e) {
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void main(String[] args) {
//        Object call = call(new ThirdpartyAlicloudmarketParam()
//                .setHost("https://kzipquery.market.alicloudapi.com")
//                .setPath("/api/ip/queryv2")
//                .setAppCode("055272a7524a4a5baec910d494451a19")
//                .setMethod("POST")
//                .setBodies(Map.of("ip", "117.42.155.205"))
//        );
//        System.err.println(JSONObject.toJSONString(call));
//    }


}
