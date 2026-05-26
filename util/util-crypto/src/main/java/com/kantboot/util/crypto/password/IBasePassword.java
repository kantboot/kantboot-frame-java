package com.kantboot.util.crypto.password;

/**
 * 密码处理方式的基类
 * @author 方某方
 */
public interface IBasePassword {

    /**
     * 用来给密码加密
     * @param inputPassword 输入的密码，一般是用户输入的密码
     * @return 加密后的密码
     */
    String encrypt(String inputPassword);

    /**
     * 用来给密码做比较
     * @param inputPassword 输入的密码，一般是用户输入的密码
     * @param storagePassword 存储的密码，一般是加密后的密码
     * @return 是否匹配
     */
    Boolean matches(String inputPassword, String storagePassword);

}
