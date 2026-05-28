package com.kantboot.util.http;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.kantboot.util.http.callback.HttpResponseStreamCallback;
import com.kantboot.util.http.domain.config.HttpSendConfig;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * HTTP请求发送工具类
 */
@Slf4j
public class HttpSendUtil {

    private final static OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.MINUTES) // 设置连接超时时间
            .writeTimeout(30, TimeUnit.MINUTES)   // 设置写入超时时间
            .readTimeout(60, TimeUnit.MINUTES).build();

    public static String send(HttpSendConfig dto, HttpResponseStreamCallback method){
        if (StrUtil.isBlank(dto.getUrl())) {
            throw new RuntimeException("url为空");
        }
        if (StrUtil.isBlank(dto.getMethod())) {
            dto.setMethod("POST");
        }
        if (dto.getContentType() == null) {
            dto.setContentType("application/json; charset=utf-8");
        }

        Response response = null;

        try {
            InputStreamReader reader = getInputReader(dto, response);
            // 读取每次流的内容
            String streamRead = "";
            StringBuilder content = new StringBuilder();
            while ((streamRead = readStream(reader)) != null) {
                method.run(streamRead);
                content.append(streamRead);
            }
            method.finish(content.toString());
            return content.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public static String send(HttpSendConfig dto) {
        if (StrUtil.isBlank(dto.getUrl())) {
            throw new RuntimeException("url为空");
        }
        if (StrUtil.isBlank(dto.getMethod())) {
            dto.setMethod("POST");
        }
        if (dto.getContentType() == null) {
            dto.setContentType("application/json;charset=utf-8");
        }

        Response response = null;

        try {
            InputStreamReader reader = getInputReader(dto, response);
            // 读取每次流的内容
            String streamRead = "";
            StringBuilder content = new StringBuilder();
            while ((streamRead = readStream(reader)) != null) {
                content.append(streamRead);
            }
            return content.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if (response != null) {
                response.close();
            }
        }

    }

    @SuppressWarnings({"deprecation"})
    private static InputStreamReader getInputReader(HttpSendConfig dto, Response response) {
        String method = dto.getMethod();
        String contentType = dto.getContentType();
        MediaType mediaType = MediaType.get(contentType);
        RequestBody reqBody = RequestBody.create(mediaType, connectParamsByContentType(dto));
        Request.Builder url = new Request.Builder().url(dto.getUrl());

        if("GET".equalsIgnoreCase(method)){
            url.get();
        }
        if ("POST".equalsIgnoreCase(method)) {
            url.post(reqBody);
        }
        if ("PUT".equalsIgnoreCase(method)) {
            url.put(reqBody);
        }
        if ("DELETE".equalsIgnoreCase(method)) {
            url.delete(reqBody);
        }
        if ("PATCH".equalsIgnoreCase(method)) {
            url.patch(reqBody);
        }
        if ("HEAD".equalsIgnoreCase(method)) {
            url.head();
        }
        if ("OPTIONS".equalsIgnoreCase(method)) {
            url.method(method, reqBody);
        }

        if (dto.getHeaders() != null) {
            for (Map.Entry<String, Object> entry : dto.getHeaders().entrySet()) {
                url.addHeader(entry.getKey(), entry.getValue().toString());
            }
        }

        if(StrUtil.isNotBlank(dto.getContentType())){
            url.addHeader("Content-Type", dto.getContentType());
        }

        Request request = url.build();
        Call call = client.newCall(request);
        response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!response.isSuccessful()) {
            throw new RuntimeException("Unexpected code " + response);
        }
        // 获得响应体
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            throw new RuntimeException("Response body is null");
        }
        InputStream inputStream = responseBody.byteStream();
        return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    }

    /**
     * 根据contentType连接参数
     */
    @SuppressWarnings("unchecked")
    private static String connectParamsByContentType(HttpSendConfig dto) {
        String contentType = dto.getContentType();
        Object params = dto.getBody();
        if (StrUtil.isBlank(contentType)) {
            throw new RuntimeException("contentType不能为空");
        }
        if (params == null) {
            return "";
        }
        // 如果是字符串直接返回
        if (params instanceof String) {
            return (String) params;
        }
        if (contentType.contains("application/json")) {
            return JSONUtil.toJsonStr(params);
        } else if (contentType.contains("application/x-www-form-urlencoded")) {
            return URLUtil.buildQuery(JSONUtil.toBean(JSONUtil.toJsonStr(params), Map.class), StandardCharsets.UTF_8);
        } else {
            throw new RuntimeException("暂不支持的contentType");
        }
    }


    /**
     * 获取每次流的数据
     *
     * @param reader reader
     * @return 每次流的数据
     */
    private static String readStream(InputStreamReader reader) {
        int read = 0;
        try {
            read = reader.read();
        } catch (IOException e) {
            log.warn("获取字节流异常: {}", e.getMessage());
        }
        if (read == -1) {
            return null;
        }
        return String.valueOf((char) read);
    }


}
