package com.kantboot.user.account.web.controller;

import com.kantboot.user.account.domain.vo.LoginVO;
import com.kantboot.user.account.service.IUserAccountLoginService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户账号登录控制器
 */
@AuthInit(name = "用户账号登录", description = "用户账号登录", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/user-account-web/userAccountLogin")
public class UserAccountLoginController {

    @Resource
    private IUserAccountLoginService service;

    /**
     * 根据用户名和密码登录
     * 通常不开放
     *
     * @param username 用户名
     * @param password 密码
     */
    @AuthInit(name = "根据用户名和密码登录", description = "根据用户名和密码登录", sourceLanguageCode = "zh_CN", noNeedLogin = true)
    @RequestMapping("/loginByUsernameAndPassword")
    public RestResult<LoginVO> loginByUsernameAndPassword(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        return RestResult.success(service.loginByUsernameAndPassword(username, password),
                CommonSuccessStateConsts.LOGIN_SUCCESS);
    }

    /**
     * 根据邮箱和密码登录
     *
     * @param email    邮箱
     *                 Email
     * @param password 密码
     *                 Password
     */
    @AuthInit(name = "根据邮箱和密码登录", description = "根据邮箱和密码登录", sourceLanguageCode = "zh_CN", noNeedLogin = true)
    @RequestMapping("/loginByEmailAndPassword")
    public RestResult<LoginVO> loginByEmailAndPassword(
            @RequestParam("email") String email,
            @RequestParam("password") String password) {
        return RestResult.success(service.loginByEmailAndPassword(email, password),
                CommonSuccessStateConsts.LOGIN_SUCCESS);
    }

    /**
     * 根据手机号登录
     */
    @AuthInit(name = "根据手机号和密码登录", description = "根据手机号和密码登录", sourceLanguageCode = "zh_CN", noNeedLogin = true)
    @RequestMapping("/loginByPhoneAndPassword")
    public RestResult<LoginVO> loginByPhoneAndPassword(
            @RequestParam("phoneAreaCode") String phoneAreaCode,
            @RequestParam("phone") String phone,
            @RequestParam("password") String password) {
        return RestResult.success(service.loginByPhoneAndPassword(phoneAreaCode, phone, password),
                CommonSuccessStateConsts.LOGIN_SUCCESS);
    }

    /**
     * 退出登录
     */
    @AuthInit(name = "退出登录", description = "退出登录", sourceLanguageCode = "zh_CN", allPass = true)
    @RequestMapping("/logout")
    public RestResult<Void> logout() {
        service.logout();
        return RestResult.success(null, CommonSuccessStateConsts.LOGOUT_SUCCESS);
    }


    /**
     * 根据手机号和验证码登录
     *
     * @param phoneAreaCode    手机号前缀
     *                         Phone number prefix
     * @param phone            手机号
     *                         Phone number
     * @param verificationCode 验证码
     *                         Verification code
     */
    @AuthInit(name = "根据手机号和验证码登录", description = "根据手机号和验证码登录", sourceLanguageCode = "zh_CN", noNeedLogin = true)
    @RequestMapping("/loginByPhoneAndVerificationCode")
    public RestResult<LoginVO> loginByPhoneAndVerificationCode(
            @RequestParam("phoneAreaCode") String phoneAreaCode,
            @RequestParam("phone") String phone,
            @RequestParam("verificationCode") String verificationCode) {
        return RestResult.success(service.loginByPhoneVerificationCode(phoneAreaCode, phone, verificationCode),
                CommonSuccessStateConsts.LOGIN_SUCCESS);
    }

    /**
     * 根据邮箱和验证码登录
     *
     * @param email            邮箱
     *                         Email
     * @param verificationCode 验证码
     *                         Verification code
     */
    @AuthInit(name = "根据邮箱和验证码登录", description = "根据邮箱和验证码登录", sourceLanguageCode = "zh_CN", noNeedLogin = true)
    @RequestMapping("/loginByEmailAndVerificationCode")
    public RestResult<LoginVO> loginByEmailAndVerificationCode(
            @RequestParam("email") String email,
            @RequestParam("verificationCode") String verificationCode) {
        return RestResult.success(service.loginByEmailAndVerificationCode(email, verificationCode),
                CommonSuccessStateConsts.LOGIN_SUCCESS);
    }

    /**
     * 发送登录手机登录验证码
     *
     * @param phoneAreaCode 手机号前缀
     *                      Phone number prefix
     * @param phone         手机号
     *                      Phone number
     */
    @AuthInit(name = "发送登录手机登录验证码", description = "发送登录手机登录验证码", sourceLanguageCode = "zh_CN", noNeedLogin = true)
    @RequestMapping("/sendLoginVerificationCodeByPhone")
    public RestResult<Void> sendLoginVerificationCodeByPhone(
            @RequestParam("phoneAreaCode") String phoneAreaCode,
            @RequestParam("phone") String phone) {
        service.sendLoginVerificationCodeByPhone(phoneAreaCode, phone);
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    /**
     * 发送登录邮箱登录验证码
     *
     * @param email 邮箱
     *              Email
     */
    @AuthInit(name = "发送登录邮箱登录验证码", description = "发送登录邮箱登录验证码", sourceLanguageCode = "zh_CN", noNeedLogin = true)
    @RequestMapping("/sendLoginVerificationCodeByEmail")
    public RestResult<Void> sendLoginVerificationCodeByEmail(
            @RequestParam("email") String email) {
        service.sendLoginVerificationCodeByEmail(email);
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    /**
     * 检查登录状态
     * checkLogin
     */
    @AuthInit(name = "检查登录状态", description = "检查登录状态", sourceLanguageCode = "zh_CN", allPass = true)
    @RequestMapping("/checkLogin")
    public RestResult<Boolean> checkLogin() {
        boolean login = service.checkLogin();
        return RestResult.success(login, CommonSuccessStateConsts.GET_SUCCESS);
    }



}
