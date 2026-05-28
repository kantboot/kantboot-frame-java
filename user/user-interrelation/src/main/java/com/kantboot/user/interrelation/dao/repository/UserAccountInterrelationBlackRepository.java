package com.kantboot.user.interrelation.dao.repository;

import com.kantboot.user.interrelation.domain.entity.UserAccountInterrelation;
import com.kantboot.user.interrelation.domain.entity.UserAccountInterrelationBlack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAccountInterrelationBlackRepository
    extends JpaRepository<UserAccountInterrelationBlack,Long> {

    /**
     * 判断是否存在拉黑关系
     */
    boolean existsByUserAccountIdAndBlackUserAccountId(
        Long userAccountId,
        Long blackUserAccountId);

    /**
     * 根据用户ID查询
     */
    List<UserAccountInterrelationBlack> findByUserAccountId(Long userAccountId);

}
