package com.kantboot.user.balance.exception;

import com.kantboot.util.rest.exception.BaseException;

public class BalanceNotEnoughException
        extends BaseException {

    public BalanceNotEnoughException(){
        super.setStateCode("balanceNotEnough");
        super.setLanguageCode("en");
        super.setMessage("Balance not enough");
    }

}
