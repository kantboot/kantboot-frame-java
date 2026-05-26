package com.kantboot.user.balance.dao.repository;

import com.kantboot.user.balance.domain.entity.UserAccountBalanceChangeRecord;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface UserAccountBalanceChangeRecordRepository extends JpaRepository<UserAccountBalanceChangeRecord, Long> {

    /**
     * 根据状态编码查询
     */
    List<UserAccountBalanceChangeRecord> findByStatusCode(String statusCode);

    /**
     * 根据编码加最小时间值查询
     */
    @Query("FROM UserAccountBalanceChangeRecord WHERE statusCode = ?1 AND gmtCreate <= ?2")
    List<UserAccountBalanceChangeRecord> findByStatusCodeAndGmtCreate(String statusCode, Date gmtCreated);

    /**
     * 根据uuid查询
     */
    UserAccountBalanceChangeRecord findByUuid(String uuid);


}
