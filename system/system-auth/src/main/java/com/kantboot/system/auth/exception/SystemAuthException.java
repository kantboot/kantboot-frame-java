package com.kantboot.system.auth.exception;

import com.kantboot.util.rest.exception.BaseException;

public class SystemAuthException {

    /**
     * 没有权限
     * No permission
     */
    public static final BaseException NO_PERMISSION = BaseException.of("noPermission", "No permission");

}
