package com.kantboot.user.account.service;

import com.kantboot.user.account.domain.entity.UserAccount;

import java.util.List;

public interface IUserAccountInviteService {

    /**
     * 初始化直属码
     */
    void initDirectCode(Long userAccountId);

    /**
     * 当前的用户初始化直属码
     */
    void initDirectCodeSelf();

    /**
     * 设置邀请人
     */
    void setInviter(Long userAccountId, Long userAccountIdOfInviter);

    void setInviterSelf(Long userAccountIdOfInviter);

    /**
     * 根据用户账户ID获取邀请人
     */
    List<UserAccount> getAllInviterByUserAccountId(Long userAccountId);

    List<UserAccount> getAllInviterBySelf();


}
