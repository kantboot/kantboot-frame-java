package com.kantboot.user.account.util;

import com.kantboot.user.account.dao.repository.UserAccountRepository;
import com.kantboot.user.account.domain.entity.UserAccount;
import com.kantboot.util.rest.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 校验保存用户账号的工具类
 */
@Component
public class UserAccountSaveCheckUtil {

    @Resource
    private UserAccountRepository repository;

    /**
     * 校验
     */
    public void check(UserAccount userAccount) {
        // 校验手机号码
        checkPhone(userAccount);
        // 校验用户名
        checkUsername(userAccount);
        // 校验邮箱
        checkEmail(userAccount);
//        if (userAccount.getPassword() !=null) {
            // 校验密码
//            checkPassword(userAccount.getPassword());
//        }
    }

    /**
     * 校验手机号码是否合法
     */
    public void checkPhone(UserAccount userAccount) {
        // 手机区域编码
        String phoneAreaCode = userAccount.getPhoneAreaCode();
        // 手机号码
        String phone = userAccount.getPhone();
        // 手机区域编码为空 或者 手机号码为空，直接跳过
        if (phoneAreaCode == null || phone == null) {
            return;
        }
        // 手机号码正则表达式
        if (!PhoneRegularUtil.check(phoneAreaCode, phone)) {
            // 抛出手机号格式错误异常
            throw BaseException.of("phoneFormatError", "手机号格式错误");
        }
        // 手机号码已注册
        boolean existsByPhoneAreaCodeAndPhone
                = repository.existsByPhoneAreaCodeAndPhone(
                userAccount.getPhoneAreaCode(),
                userAccount.getPhone());
        // 手机号码已注册
        if (existsByPhoneAreaCodeAndPhone) {
            // 抛出手机号已注册异常
            throw BaseException.of("phoneRegistered", "手机号已注册");
        }
    }

    /**
     * 校验用户名是否合法
     */
    public void checkUsername(UserAccount userAccount) {
        // 用户名
        String username = userAccount.getUsername();
        // 用户名为空，直接跳过
        if (username == null) {
            return;
        }
        // 用户名正则表达式
        String usernameRegular = "^[a-zA-Z0-9_-]{4,16}$";
        if (!username.matches(usernameRegular)) {
            // 抛出用户名格式错误异常
            throw BaseException.of("usernameFormatError", "用户名格式错误，长度4-16位，只能包含字母、数字、下划线、减号");
        }
        // 用户名已注册
        boolean existsByUsername = repository.existsByUsername(username);
        // 用户名已注册
        if (existsByUsername) {
            // 抛出用户名已注册异常
            throw BaseException.of("usernameRegistered", "用户名已注册");
        }
    }

    /**
     * 校验邮箱是否合法
     */
    public void checkEmail(UserAccount userAccount) {
        // 邮箱
        String email = userAccount.getEmail();
        // 邮箱为空，直接跳过
        if (email == null) {
            return;
        }
        // 邮箱格式
        int atIndex = email.indexOf("@");
        int dotIndex = email.lastIndexOf(".");
        if (atIndex < 1 || dotIndex < atIndex + 2 || dotIndex + 2 >= email.length()) {
            // 抛出邮箱格式错误异常
            throw BaseException.of("emailFormatError", "邮箱格式错误");
        }

        // 邮箱已注册
        boolean existsByEmail = repository.existsByEmail(email);
        // 邮箱已注册
        if (existsByEmail) {
            // 抛出邮箱已注册异常
            throw BaseException.of("emailRegistered", "邮箱已注册");
        }
    }

    /**
     * 校验密码是否合法
     */
    public void checkPassword(String password) {
//        // 密码格式，必须包含数字、字母，长度8-32位，不能包含除!@#$%^&*()_+-={}[]|\:;"'<>,.?/~`空格外的特殊字符
//        int length = password.length();
//        // 特殊字符
//        String specialCharacters = "!@#$%^&*()_+-={}[]|\\:;\"'<>,.?/~`";
//        // 数字
//        String numberCharacters = "1234567890";
//        // 字母
//        String letterCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
//        String characters = specialCharacters + numberCharacters + letterCharacters;
//        if (length < 8 || length > 32) {
//            // 抛出密码格式错误异常
//            throw BaseException.of("passwordFormatError", "密码格式错误，必须包含数字、字母，长度8-32位，不能包含除"+specialCharacters+"外的特殊字符","zh_CN");
//        }
//        // 数字数量
//        int numberCount = 0;
//        // 字母数量
//        int letterCount = 0;
//        for (int i = 0; i < length; i++) {
//            String c = String.valueOf(password.charAt(i));
//            // 是否是指定特殊字符
//            boolean isHasChars = characters.contains(c);
//            // 如果不是指定特殊字符
//            if (!isHasChars) {
//                // 抛出密码格式错误异常
//                throw BaseException.of("passwordFormatError", "密码格式错误，必须包含数字、字母，长度8-32位，不能包含除"+specialCharacters+"外的特殊字符","zh_CN");
//            }
//            // 是否包含数字
//            if (numberCharacters.contains(c)) {
//                numberCount++;
//            }
//            // 是否包含字母
//            if (letterCharacters.contains(c)) {
//                letterCount++;
//            }
//
//        }
//        // 如果数字数量为0 或 字母数量为0
//        if (numberCount == 0 || letterCount == 0) {
//            // 抛出密码格式错误异常
//            throw BaseException.of("passwordFormatError",
//                    "密码格式错误，必须包含数字、字母，长度8-32位，不能包含除"+specialCharacters+"外的特殊字符",
//                    "zh_CN");
//        }

    }




}
