package com.kantboot.user.account.domain.vo;

import com.kantboot.user.account.domain.entity.UserAccount;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户账户登录VO
 */
@Data
@Accessors(chain = true)
public class LoginVO implements Serializable {

    /**
     * 令牌
     */
    private String token;

    /**
     * 用户账户
     */
    private UserAccount userAccount;

}
