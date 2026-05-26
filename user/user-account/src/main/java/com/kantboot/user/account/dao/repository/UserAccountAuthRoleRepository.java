package com.kantboot.user.account.dao.repository;

import com.kantboot.user.account.domain.entity.UserAccountAuthRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserAccountAuthRoleRepository extends JpaRepository<UserAccountAuthRole,Long> {

    /**
     * 根据用户账号ID查询用户账号角色ID
     * Query by User Account ID
     * @param userAccountId 用户账号ID
     *                      User Account ID
     * @return 用户账号角色ID
     *        User Account Role ids
     */
    @Query("""
            SELECT uar.roleId FROM UserAccountAuthRole uar
            WHERE uar.userAccountId = :userAccountId
    """)
    List<Long> getRoleIdsByUserAccountId(Long userAccountId);


}
