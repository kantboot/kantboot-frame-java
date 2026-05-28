package com.kantboot.thirdparty.wechat.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.kantboot.thirdparty.wechat.util.domain.ThirdpartyWechatExecute;
import com.kantboot.thirdparty.wechat.util.domain.ThirdpartyWechatToken;
import com.kantboot.util.http.HttpSendUtil;
import com.kantboot.util.http.domain.config.HttpSendConfig;
import com.kantboot.util.rest.exception.BaseException;

import java.util.Map;

public class ThirdpartyWechatRequestUtil {

    private final static String BASE_URL = "https://api.weixin.qq.com";

    public static Object execute(ThirdpartyWechatExecute execute) {
        String url = BASE_URL+execute.getPath();
        String method = execute.getMethod();
        String contentType = execute.getContentType();
        String accessToken = execute.getAccessToken();
        Map<String,String> queries = execute.getQueries();
        Map<String,String> bodies = execute.getBodies();

        boolean notEmpty = StrUtil.isNotEmpty(accessToken);
        if (notEmpty) {
            if (url.contains("?")) {
                url += "&access_token=" + accessToken;
            } else {
                url += "?access_token=" + accessToken;
            }
        }
        if(url.contains("?")&&queries!=null&&!queries.isEmpty()){
            url += "&" + StrUtil.join("&", queries);
        }
        String send = HttpSendUtil.send(new HttpSendConfig()
                .setUrl(url)
                .setContentType(contentType)
                .setMethod(method)
                .setBody(bodies)
        );
        JSONObject jsonObject = JSONObject.parseObject(send);
        Integer errcode = jsonObject.getInteger("errcode");
        if (errcode != null && errcode != 0) {
            throw BaseException.of("wechatRequestError:errcode:" + errcode, jsonObject.getString("errmsg"),"zh_CN");
        }
        return jsonObject;
    }

}
