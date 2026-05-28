package com.kantboot.user.balance.exception;

import com.kantboot.util.rest.exception.BaseException;

public class BalanceHandleNotExistException
        extends BaseException {

    public BalanceHandleNotExistException(){
        super.setStateCode("balanceHandleNotExist");
        super.setLanguageCode("en");
        super.setMessage("Balance handle not exist");
    }

}
