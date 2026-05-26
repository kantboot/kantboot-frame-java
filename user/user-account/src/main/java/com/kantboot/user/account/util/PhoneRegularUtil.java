package com.kantboot.user.account.util;

import java.util.Map;

/**
 * 手机号码正则工具类
 * 临时
 */
public class PhoneRegularUtil {

    public static Map<String,String> phoneRegularMap = Map.of(
            // 美国
            "1","^1[3-9]\\d{9}$",
            // 加拿大
            "7","^7[0-9]\\d{8}$",
            // 中国内地
            "86","^1[3-9]\\d{9}$",
            // 中国香港
            "852","^5[0-9]\\d{7}$",
            // 中国澳门
            "853","^6\\d{7}$",
            // 中国台湾
            "886","^09\\d{8}$"
    );

    /**
     * 校验手机号码格式是否合法
     */
    public static boolean check(String phoneAreaCode, String phone) {
        // 手机号码正则表达式
        String phoneRegular = phoneRegularMap.get(phoneAreaCode);
        if (phoneRegular == null) {
            return false;
        }
        return phone.matches(phoneRegular);
    }
}
