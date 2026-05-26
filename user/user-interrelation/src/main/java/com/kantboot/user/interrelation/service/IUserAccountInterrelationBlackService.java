package com.kantboot.user.interrelation.service;

import com.kantboot.user.account.domain.entity.UserAccount;
import com.kantboot.user.interrelation.domain.entity.UserAccountInterrelationBlack;

import java.util.List;

public interface IUserAccountInterrelationBlackService {

    /**
     * 判断是否存在拉黑关系
     */
    boolean isBlack(
        Long blackUserAccountId);

    /**
     * 判断是否被拉黑
     */
    boolean isBeBlack(
        Long userAccountId);

    /**
     * 拉黑
     */
    void black(
        Long blackUserAccountId);

    List<UserAccountInterrelationBlack> getBySelf();

    /**
     * 移出黑名单
     */
    void unBlack(
        Long blackUserAccountId);

}
