package com.kantboot.user.balance.dao.repository;

import com.kantboot.user.balance.domain.entity.UserAccountBalance;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface UserAccountBalanceRepository extends JpaRepository<UserAccountBalance, Long> {

    /**
     * 根据用户ID和余额类型代码查询
     */
    UserAccountBalance findByUserAccountIdAndBalanceCode(Long userAccountId, String balanceCode);

    /**
     * 根据用户ID查询
     */
    List<UserAccountBalance> findByUserAccountId(Long userAccountId);

    @Transactional
    @Modifying
    @Query("""
            UPDATE UserAccountBalance
                SET number = number + :number
                WHERE userAccountId = :userAccountId
                AND balanceCode = :balanceCode
    """)
    void addNumber(
            @Param("userAccountId") Long userAccountId,
            @Param("balanceCode") String balanceCode,
            @Param("number") BigDecimal number);

}
