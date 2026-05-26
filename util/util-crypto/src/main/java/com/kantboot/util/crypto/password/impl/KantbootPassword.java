package com.kantboot.util.crypto.password.impl;


import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.kantboot.util.crypto.password.IBasePassword;
import org.springframework.stereotype.Component;

/**
 * 一种密码处理方式
 * 方式名称：kantboot
 *
 * @author 方某方
 */
@Component
public class KantbootPassword implements IBasePassword {

    /**
     * 用来给密码加密
     * 无法进行逆向解密
     * @param inputPassword 输入的密码
     * @param salt          盐
     * @return 加密后的密码
     */
    private String encryptFront(String inputPassword, String salt) {

        // 用来存储密码
        String passwordStorage = salt + "/" + inputPassword;

        // 用来存储加密后的密码
        String digestHex = new Digester(DigestAlgorithm.MD5).digestHex(passwordStorage);

        // start:用来做多次加密
        int num = 3;
        for (int i = 0; i < num; i++) {
            // 用来做多次加密
            digestHex = new Digester(DigestAlgorithm.MD5).digestHex(digestHex + passwordStorage);
            // 用来做多次加密
            digestHex = new Digester(DigestAlgorithm.MD5).digestHex(inputPassword + digestHex + passwordStorage);
        }
        // end:用来做多次加密

        // 用来存储盐
        digestHex = salt + "." + digestHex;


        return digestHex;
    }

    /**
     * 用来给密码加密
     * 无法进行逆向解密
     * @param inputPassword 输入的密码，一般是用户输入的密码
     * @return 加密后的密码
     */
    @Override
    public String encrypt(String inputPassword) {
        // 用来存储盐
        String salt = IdUtil.simpleUUID();
        // 用来存储加密后的密码
        return "{kantboot}" + encryptFront(inputPassword, salt);
    }

    /**
     * 用来给密码做比较
     *
     * @param inputPassword   输入的密码，一般是用户输入的密码
     * @param storagePassword 存储的密码，一般是加密后的密码
     * @return 是否匹配
     */
    @Override
    public Boolean matches(String inputPassword, String storagePassword) {
        // 判断是否为kantboot加密
        if (!storagePassword.startsWith("{kantboot}")) {
            return false;
        }
        storagePassword = storagePassword.substring("{kantboot}".length());
        // 用来存储盐
        String[] split = storagePassword.split("\\.");
        String salt = split[0];
        String password = encryptFront(inputPassword, salt);

        // 用来做比较
        return password.equals(storagePassword);
    }

}
