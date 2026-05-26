package com.kantboot.user.interrelation.dao.repository;

import com.kantboot.user.interrelation.domain.entity.UserAccountInterrelationRemark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAccountInterrelationRemarkRepository
    extends JpaRepository<UserAccountInterrelationRemark,Long> {

    /**
     * 根据用户账户id查询用户账户备注
     */
    UserAccountInterrelationRemark findByUserAccountIdAndUserAccountIdOfRemark(Long userAccountId,
                                                                               Long userAccountIdOfRemark);

    /**
     * 根据用户账户id查询用户账户备注
     */
    List<UserAccountInterrelationRemark> findByUserAccountId(Long userAccountId);

}
