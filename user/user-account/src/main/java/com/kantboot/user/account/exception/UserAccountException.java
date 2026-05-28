package com.kantboot.user.account.exception;

import com.kantboot.util.rest.exception.BaseException;

public class UserAccountException {

    /**
     * 未登录
     */
    public static final BaseException NOT_LOGIN = BaseException.of("notLogin", "请重新登录","zh_CN");

    /**
     * 账号不存在
     */
    public static final BaseException NOT_EXIST = BaseException.of("notExist", "Account does not exist","en");

    /**
     * 用户名已存在
     */
    public static final BaseException USERNAME_EXIST = BaseException.of("usernameExist", "Username already exists","en");

    /**
     * 邮箱已存在
     */
    public static final BaseException EMAIL_EXIST = BaseException.of("emailExist", "Email already exists","en");

    /**
     * 手机号已存在
     */
    public static final BaseException PHONE_EXIST = BaseException.of("phoneExist", "Phone already exists","en");

    /**
     * 原密码错误
     */
    public static final BaseException OLD_PASSWORD_ERROR = BaseException.of("oldPasswordError", "Old password error","en");

    /**
     * 用户名或密码错误
     * usernameOrPasswordError
     */
    public static final BaseException USERNAME_OR_PASSWORD_ERROR = BaseException.of("usernameOrPasswordError", "Username or password error","en");

    /**
     * 该账号未设置密码
     * passwordNotSet
     */
    public static final BaseException PASSWORD_NOT_SET = BaseException.of("passwordNotSet", "The account has not set a password","en");

    /**
     * 未使用短信验证码插件
     */
    public static final BaseException SMS_PLUGIN_NOT_USED = BaseException.of("smsPluginNotUsed", "The SMS verification code plugin is not used","en");

    /**
     * 未使用邮箱验证码插件
     */
    public static final BaseException EMAIL_PLUGIN_NOT_USED = BaseException.of("emailPluginNotUsed", "The email verification code plugin is not used","en");

    /**
     * 账号不存在
     */
    public static final BaseException ACCOUNT_NOT_EXIST = BaseException.of("accountNotExist", "Account does not exist","en");

    /**
     * 邮箱或密码错误
     */
    public static final BaseException EMAIL_OR_PASSWORD_ERROR = BaseException.of("emailOrPasswordError", "Email or password error","en");

    /**
     * 验证码错误
     */
    public static final BaseException VERIFICATION_CODE_ERROR = BaseException.of("verifyCodeError", "Verification code error","en");

    /**
     * 参数错误
     */
    public static final BaseException PARAMETER_ERROR = BaseException.of("parameterError", "Parameter error","en");

    /**
     * 已存在用户，不符合初始化要求
     */
    public static final BaseException USER_ACCOUNT_EXIST_IN_INIT = BaseException.of("userAccountExistInInit", "User account already exists, and does not meet the initialization requirements","en");

    /**
     * 密码不为空
     */
    public static final BaseException PASSWORD_NOT_NULL = BaseException.of("passwordNotNull", "Password cannot be empty","en");

    /**
     * 已绑定
     */
    public static final BaseException THIRD_PARTY_BIND_EXIST = BaseException.of("thirdPartyBindExist", "Already bound","en");

    /**
     * HAS_INVITER
     * 已有邀请人
     */
    public static final BaseException HAS_INVITER = BaseException.of("hasInviter", "Already has an inviter","en");

    /**
     * INVITER_IS_SELF
     */
    public static final BaseException INVITER_IS_SELF = BaseException.of("inviterIsSelf", "Inviter is self","en");

    /**
     * INVITER_JOIN_TIME_ERROR
     */
    public static final BaseException INVITER_JOIN_TIME_ERROR = BaseException.of("inviterJoinTimeError", "Inviter join time error","en");

    /**
     * PHONE_OR_PASSWORD_ERROR
     */
    public static final BaseException PHONE_OR_PASSWORD_ERROR = BaseException.of("phoneOrPasswordError", "Phone or password error","en");

    /**
     * 登录已过期
     */
    public static final BaseException LOGIN_EXPIRED = BaseException.of("loginExpired", "Login expired","en");
}
