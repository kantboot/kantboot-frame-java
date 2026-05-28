package com.kantboot.user.account.web.controller;

import com.kantboot.user.account.service.IUserAccountBindService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户账号绑定控制器
 */
@AuthInit(name = "用户账号绑定", description = "用户账号绑定", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/user-account-web/userAccountBind")
public class UserAccountBindController {

    @Resource
    private IUserAccountBindService service;

    /**
     * 跳过绑定
     */
    @AuthInit(name = "跳过绑定", description = "跳过绑定", sourceLanguageCode = "zh_CN", noNeedLogin = true)
    @RequestMapping("/skipBind")
    public RestResult<Void> skipBind() {
        service.skipBind();
        return RestResult.success(null, CommonSuccessStateConsts.SKIP_BIND_SUCCESS);
    }

    /**
     * 发送邮箱验证码
     * @param email 邮箱
     */
    @AuthInit(name = "发送邮箱验证码", description = "发送邮箱验证码", sourceLanguageCode = "zh_CN", noNeedLogin = true)
    @RequestMapping("/sendVerificationCodeByEmail")
    public RestResult<Void> sendVerificationCodeByEmail(@RequestParam("email") String email) {
        service.sendVerificationCodeByEmail(email);
        return RestResult.success(null, CommonSuccessStateConsts.SEND_VERIFICATION_CODE_SUCCESS);
    }

    /**
     * 发送手机验证码
     * @param phoneAreaCode 手机区号
     * @param phone 手机号
     */
    @AuthInit(name = "发送手机验证码", description = "发送手机验证码", sourceLanguageCode = "zh_CN", noNeedLogin = true)
    @RequestMapping("/sendVerificationCodeByPhone")
    public RestResult<Void> sendVerificationCodeByPhone(
            @RequestParam("phoneAreaCode") String phoneAreaCode,
            @RequestParam("phone") String phone) {
        service.sendVerificationCodeByPhone(phoneAreaCode, phone);
        return RestResult.success(null, CommonSuccessStateConsts.SEND_VERIFICATION_CODE_SUCCESS);
    }

    /**
     * 通过邮箱和验证码绑定
     * @param email 邮箱
     * @param verificationCode 验证码
     */
    @AuthInit(name = "通过邮箱和验证码绑定", description = "通过邮箱和验证码绑定", sourceLanguageCode = "zh_CN", noNeedLogin = true)
    @RequestMapping("/bindByEmailAndVerificationCode")
    public RestResult<Void> bindByEmailAndVerificationCode(
            @RequestParam("email") String email,
            @RequestParam("verificationCode") String verificationCode) {
        service.bindByEmailAndVerificationCode(email, verificationCode);
        return RestResult.success(null, CommonSuccessStateConsts.BIND_SUCCESS);
    }

    /**
     * 通过手机和验证码绑定
     * @param phoneAreaCode 手机区号
     * @param phone 手机号
     * @param verificationCode 验证码
     */
    @AuthInit(name = "通过手机和验证码绑定", description = "通过手机和验证码绑定", sourceLanguageCode = "zh_CN", noNeedLogin = true)
    @RequestMapping("/bindByPhoneAndVerificationCode")
    public RestResult<Void> bindByPhoneAndVerificationCode(
            @RequestParam("phoneAreaCode") String phoneAreaCode,
            @RequestParam("phone") String phone,
            @RequestParam("verificationCode") String verificationCode) {
        service.bindByPhoneAndVerificationCode(phoneAreaCode, phone, verificationCode);
        return RestResult.success(null, CommonSuccessStateConsts.BIND_SUCCESS);
    }
}
