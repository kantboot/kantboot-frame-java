package com.kantboot.user.account.service;

import java.util.List;

public interface IUserAccountAuthRoleService {

    /**
     * 根据用户账号ID查询用户账号角色ID
     * Query by User Account ID
     * @param userAccountId 用户账号ID
     *                      User Account ID
     * @return 用户账号角色ID
     *        User Account Role ids
     */
    List<Long> getRoleIdsByUserAccountId(Long userAccountId);


    List<Long> getRoleIdsBySelf();

}
