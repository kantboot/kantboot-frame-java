package com.kantboot.user.account.service;

import com.kantboot.user.account.domain.vo.LoginVO;

public interface IUserAccountRegisterService {

    /**
     * 通过用户名和密码注册新用户
     * 通常不适用于生产环境
     * @param username 用户名
     * @param password 密码
     * @return 登录信息
     */
    LoginVO registerByUsernameAndPassword(
            String username, String password);

}
