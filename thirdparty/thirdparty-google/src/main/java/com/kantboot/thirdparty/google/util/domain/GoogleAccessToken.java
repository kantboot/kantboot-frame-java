package com.kantboot.thirdparty.google.util.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoogleAccessToken
    implements Serializable {

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 访问令牌过期时间，单位秒
     */
    private Long expiresIn;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 刷新令牌过期时间，单位秒
     */
    private String scope;

    /**
     * 令牌类型
     */
    private String tokenType;

    /**
     * ID令牌
     */
    private String idToken;

}
