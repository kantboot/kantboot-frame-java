package com.kantboot.user.interrelation.dao.repository;

import com.kantboot.user.interrelation.domain.entity.UserAccountInterrelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountInterrelationRepository
    extends JpaRepository<UserAccountInterrelation,Long> {

    /**
     * 根据用户ID获取
     */
    UserAccountInterrelation findByUserAccountId(Long userAccountId);

}
