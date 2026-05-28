package com.kantboot.thirdparty.alicloud.market.param;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

@Data
@Accessors(chain = true)
public class ThirdpartyAlicloudMarketParam implements Serializable {

    /**
     * host
     */
    private String host;

    /**
     * 请求地址
     */
    private String path;

    /**
     * 请求方式
     */
    private String method;

    /**
     * appCode
     */
    private String appCode;

    /**
     * 请求头
     */
    private Map<String, String> headers;

    /**
     * 请求参数（query）
     */
    private Map<String, String> queries;

    /**
     * 请求参数（body）
     */
    private Map<String, String> bodies;

}
