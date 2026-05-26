package com.kantboot.user.balance.exception;

import com.kantboot.util.rest.exception.BaseException;

public class BalanceHandleIsLockException
        extends BaseException {

    public BalanceHandleIsLockException(){
        super.setStateCode("balanceHandleIsLock");
        super.setLanguageCode("zh_CN");
        super.setMessage("正在处理中，预计1分钟后到账");
    }

}
