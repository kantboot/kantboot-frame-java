package com.kantboot.thirdparty.wechat.util.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class ThirdpartyWechatExecute {

    private String path;

    private String method;

    private String contentType;

    private String accessToken;

    private Map<String, String> queries;

    private Map<String, String> bodies;

}
