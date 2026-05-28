package com.kantboot.util.email.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmailAccountDTO
    implements Serializable {

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱（大部分情况下，用户名和邮箱是相同的）
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 主机
     */
    private String host;

    /**
     * 是否权限验证
     */
    private Boolean auth = true;

    /**
     * 是否开启ssl
     */
    private Boolean sslEnable = true;

}
