package com.kantboot.thirdparty.github.util.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class GithubAccessToken
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
    private Long refreshTokenExpiresIn;

    /**
     * 令牌类型
     */
    private String tokenType;

    /**
     * 作用域
     */
    private String scope;

}
