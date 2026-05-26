package com.kantboot.user.interrelation.service;

import com.kantboot.user.interrelation.domain.entity.UserAccountInterrelationRemark;

import java.util.List;

public interface IUserAccountInterrelationRemarkService {

    /**
     * 设置备注
     */
    void setRemark(Long userAccountIdOfRemark, String remark);

    /**
     * 获取备注
     */
    UserAccountInterrelationRemark getRemark(Long userAccountIdOfRemark);

    /**
     * 根据用户账号ID获取列表
     */
    List<UserAccountInterrelationRemark> getByUserAccountId(Long userAccountId);

    List<UserAccountInterrelationRemark> getBySelf();
}
