package com.kantboot.user.account.service;

/**
 * 用户账户绑定服务
 */
public interface IUserAccountBindService {

    /**
     * 跳过绑定
     */
    void skipBind();

    void sendVerificationCodeByEmail(String email);

    void sendVerificationCodeByPhone(String phoneAreaCode, String phone);

    /**
     * 根据邮箱加验证码绑定
     */
    void bindByEmailAndVerificationCode(String email, String verificationCode);

    /**
     * 根据手机号加验证码绑定
     */
    void bindByPhoneAndVerificationCode(String phoneAreaCode, String phone, String verificationCode);

    /**
     * 应用内授权绑定，不透露给客户端接口
     * 第三方绑定
     */
    void bindByThirdparty(String thirdPartyCode, String key, String value);

    void bindByThirdparty(Long userAccountId,String thirdPartyCode, String key, String value);


    /**
     * 应用内授权绑定，不透露给客户端接口
     * 第三方绑定邮箱
     */
    void bindByThirdpartyAndEmail(String thirdPartyCode, String key, String value, String email);

    /**
     * 应用内授权绑定，不透露给客户端接口
     * 第三方绑定手机号
     */
    void bindByPhone(String phoneAreaCode, String phone);


}
