package com.kantboot.user.account.service;

import com.kantboot.user.account.domain.vo.LoginVO;

public interface IUserAccountLoginService {


    /**
     * 根据用户名和密码登录
     * @param username 用户名
     * @param password 密码
     * @return 登录信息
     */
    LoginVO loginByUsernameAndPassword(String username, String password);

    /**
     * 根据用户名登录
     */
    LoginVO loginByUsername(String username);

    /**
     * 根据邮箱和密码登录
     * @param email 邮箱
     *              Email
     * @param password 密码
     *                 Password
     */
    LoginVO loginByEmailAndPassword(String email, String password);

    /**
     * 根据手机号加密码
     * @param phoneAreaCode 手机区号
     * @param phone 手机号
     * @param password 密码
     */
    LoginVO loginByPhoneAndPassword(String phoneAreaCode, String phone, String password);


    /**
     * 发送手机验证码
     * @param phoneAreaCode 手机区号
     * @param phone 手机号
     */
    void sendLoginVerificationCodeByPhone(String phoneAreaCode, String phone);

    /**
     * 发送邮箱验证码
     * @param email 邮箱
     */
    void sendLoginVerificationCodeByEmail(String email);

    /**
     * 验证手机验证码是否登录成功
     * @param phoneAreaCode 手机区号
     *                      Phone area code
     * @param phone 手机号
     *              Phone
     *
     * @param verifyCode 验证码
     *                   Verify code
     */
    LoginVO loginByPhoneVerificationCode(String phoneAreaCode, String phone, String verifyCode);

    /**
     * 验证邮箱验证码是否登录成功
     * @param email 邮箱
     *              Email
     *
     * @param verificationCode 验证码
     *                         Verification code
     */
    LoginVO loginByEmailAndVerificationCode(String email, String verificationCode);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 检查登录状态
     */
    boolean checkLogin();

    /**
     * 是否登录
     */
    boolean isLogin();

    /**
     * 应用内授权登录，不透露给客户端接口
     * @param userAccountId 用户账号ID
     *                      User account ID
     */
    LoginVO loginByUserAccountId(Long userAccountId);

    /**
     * 应用内登录，不透露给客户端接口
     *
     */
    LoginVO loginByPhone(String phoneAreaCode, String phone);

    /**
     * 应用内登录，不透露给客户端接口
     *
     */
    LoginVO loginByEmail(String email);

    /**
     * 应用内授权登录，不透露给客户端接口
     * 第三方登录
     */
    LoginVO loginByThirdparty(String thirdpartyCode, String key, String value);

    /**
     * 应用内授权登录，不透露给客户端接口
     * 第三方登录加自动根据邮箱登录或注册
     */
    LoginVO loginByThirdpartyAndEmail(String thirdPartyCode, String key, String value, String email);
}