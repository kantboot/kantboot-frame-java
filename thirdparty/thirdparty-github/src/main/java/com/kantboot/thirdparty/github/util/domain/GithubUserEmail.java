package com.kantboot.thirdparty.github.util.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class GithubUserEmail implements Serializable {

    /**
     * 邮箱
     */
    private String email;

    /**
     * 是否为主要邮箱
     */
    private Boolean primary;

    /**
     * 是否已验证
     */
    private Boolean verified;

    /**
     * 可见性
     */
    private String visibility;

}