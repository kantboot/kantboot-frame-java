package com.kantboot.user.account.slot;

import com.kantboot.user.account.exception.UserAccountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用户账户模块的插槽
 * 用于提供用户账户模块的扩展点
 * User Account Slot
 * Use for provide extension point for user account module
 */
@Slf4j
@Component
public class UserAccountSlot {

    /**
     * 发送手机登录验证码的插槽
     * Send login verification code by phone slot
     */
    public void sendLoginVerifyCodeByPhone(String phoneAreaCode, String phone) {
        log.info("sendLoginVerifyCodeByPhone phoneAreaCode: {}, phone: {}", phoneAreaCode, phone);
        // 提示用户账户模块未使用短信插件
        throw UserAccountException.SMS_PLUGIN_NOT_USED;
    }

    /**
     * 发送邮箱登录验证码的插槽
     * Send login verification code by email slot
     */
    public void sendLoginVerifyCodeByEmail(String email) {
        log.info("sendLoginVerifyCodeByEmail email: {}", email);
        // 提示用户账户模块未使用邮件插件
        throw UserAccountException.EMAIL_PLUGIN_NOT_USED;
    }

    /**
     * 验证手机登录验证码的是否成功的插槽
     */
    public Boolean matchLoginVerifyCodeByPhone(String phoneAreaCode, String phone, String verifyCode) {
        log.info("matchLoginVerifyCodeByPhone phoneAreaCode: {}, phone: {}, verificationCode: {}", phoneAreaCode, phone, verifyCode);
        // 提示用户账户模块未使用短信插件
        throw UserAccountException.SMS_PLUGIN_NOT_USED;
    }

    /**
     * 验证码邮箱登录验证码的是否成功的插槽
     */
    public Boolean matchLoginVerifyCodeByEmail(String email, String verifyCode) {
        log.info("matchLoginVerifyCodeByEmail email: {}, verificationCode: {}", email, verifyCode);
        // 提示用户账户模块未使用邮件插件
        throw UserAccountException.EMAIL_PLUGIN_NOT_USED;
    }

    /**
     * 发送手机绑定验证码的插槽
     * Send bind verification code by phone slot
     */
    public void sendBindVerifyCodeByPhone(String phoneAreaCode, String phone) {
        log.info("sendBindVerifyCodeByPhone phoneAreaCode: {}, phone: {}", phoneAreaCode, phone);
        // 提示用户账户模块未使用短信插件
        throw UserAccountException.SMS_PLUGIN_NOT_USED;
    }

    /**
     * 发送邮箱绑定验证码的插槽
     * Send bind verification code by email slot
     */
    public void sendBindVerifyCodeByEmail(String email) {
        log.info("sendBindVerifyCodeByEmail email: {}", email);
        // 提示用户账户模块未使用邮件插件
        throw UserAccountException.EMAIL_PLUGIN_NOT_USED;
    }

    /**
     * 验证手机绑定验证码的是否成功的插槽
     */
    public Boolean matchBindVerifyCodeByPhone(String phoneAreaCode, String phone, String verifyCode) {
        log.info("matchBindVerifyCodeByPhone phoneAreaCode: {}, phone: {}, verificationCode: {}", phoneAreaCode, phone, verifyCode);
        // 提示用户账户模块未使用短信插件
        throw UserAccountException.SMS_PLUGIN_NOT_USED;
    }

    /**
     * 验证码邮箱绑定验证码的是否成功的插槽
     */
    public Boolean matchBindVerifyCodeByEmail(String email, String verifyCode) {
        log.info("matchBindVerifyCodeByEmail email: {}, verificationCode: {}", email, verifyCode);
        // 提示用户账户模块未使用邮件插件
        throw UserAccountException.EMAIL_PLUGIN_NOT_USED;
    }


}
