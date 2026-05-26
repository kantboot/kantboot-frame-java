package com.kantboot.user.balance.service;

import com.kantboot.user.balance.domain.dto.ChangeHandleDTO;
import com.kantboot.user.balance.domain.entity.UserAccountBalance;
import com.kantboot.user.balance.domain.entity.UserAccountBalanceChangeHandle;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IUserAccountBalanceService {

    /**
     * 添加余额记录
     */
    UserAccountBalanceChangeHandle add(ChangeHandleDTO record);

    /**
     * 改变
     */
    UserAccountBalanceChangeHandle change(ChangeHandleDTO record);

    /**
     * 处理成功
     */
    UserAccountBalanceChangeHandle handle(String uuid);

    /**
     * 处理失败
     */
    void handleFail(String uuid, String failReasonCode);

    /**
     * 余额清零
     */
    UserAccountBalanceChangeHandle clear(ChangeHandleDTO changeRecord);

    /**
     * 获取用户余额
     */
    BigDecimal getBalance(Long userAccountId,  String balanceCode);


    List<UserAccountBalance> getBalanceList(Long userAccountId);

    Map<String,BigDecimal> getBalanceMap(Long userAccountId);

    Map<String,BigDecimal> getBySelf();

}
