package com.kantboot.user.account.dao.repository;

import com.kantboot.user.account.domain.entity.UserAccountToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAccountTokenRepository extends JpaRepository<UserAccountToken, Long> {

    /**
     * 根据令牌查询用户账户令牌
     * @param token 令牌
     * @return 用户账户令牌
     */
    UserAccountToken findByToken(String token);

    List<UserAccountToken> findByUserAccountId(Long userAccountId);
}
