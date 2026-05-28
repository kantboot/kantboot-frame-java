package com.kantboot.user.online.dao.repository;

import com.kantboot.user.online.domain.entity.UserAccountOnlineShow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountOnlineShowRepository extends JpaRepository<UserAccountOnlineShow, Long> {

    /**
     * 根据用户账号ID查询
     */
    UserAccountOnlineShow findByUserAccountId(Long userAccountId);

    /**
     * 根据用户账号ID查询是否存在
     */
    Boolean existsByUserAccountId(Long userAccountId);

}
