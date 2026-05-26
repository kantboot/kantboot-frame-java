package com.kantboot.util.http.domain.config;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

@Data
@Accessors(chain=true)
public class HttpSendConfig implements Serializable {

    private String url;

    private String method;

    private String contentType;

    private Map<String,Object> headers;

    private Object body;

    /**
     * 添加一个header
     * @param key 键
     * @param value 值
     * @return this
     */
    public HttpSendConfig addHeader(String key, Object value) {
        if (this.headers == null) {
            this.headers = new java.util.HashMap<>();
        }
        headers.put(key, value);
        return this;
    }

}
