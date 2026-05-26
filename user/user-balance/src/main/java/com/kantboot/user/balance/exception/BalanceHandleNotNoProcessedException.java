package com.kantboot.user.balance.exception;

import com.kantboot.util.rest.exception.BaseException;

public class BalanceHandleNotNoProcessedException
        extends BaseException {

    public BalanceHandleNotNoProcessedException(){
        super.setStateCode("balanceHandleNotNoProcessed");
        super.setLanguageCode("en");
        super.setMessage("Balance handle not no processed");
    }

}
