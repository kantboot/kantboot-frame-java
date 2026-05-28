package com.kantboot.system.setting.exception;

import com.kantboot.util.rest.exception.BaseException;

/**
 * 系统设置异常类
 * 定义系统设置相关的异常
 */
public class SystemSettingException {

    /**
     * 分组编码已存在
     */
    public static final BaseException GROUP_CODE_EXIST = BaseException.of("SystemSetting.groupCodeExist", "分组编码已存在","zh_CN");

    /**
     * 分组不存在
     */
    public static final BaseException GROUP_NOT_EXIST = BaseException.of("SystemSetting.groupNotExist", "分组不存在","zh_CN");

    /**
     * 分组不可为空
     */
    public static final BaseException GROUP_NOT_NULL = BaseException.of("SystemSetting.groupNotNull", "分组不可为空","zh_CN");

    /**
     * 编码已存在
     */
    public static final BaseException CODE_EXIST = BaseException.of("SystemSetting.codeExist", "编码已存在","zh_CN");

    /**
     * 编码不可为空
     */
    public static final BaseException CODE_NOT_EMPTY = BaseException.of("SystemSetting.codeNotEmpty", "编码不可为空","zh_CN");

}
