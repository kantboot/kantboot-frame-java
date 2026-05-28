package com.kantboot.user.online.dao.repository;

import com.kantboot.user.online.domain.entity.UserAccountOnline;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface UserAccountOnlineRepository extends JpaRepository<UserAccountOnline, Long> {

    /**
     * 隐身
     */
    @Transactional
    @Modifying
    @Query("UPDATE UserAccountOnline u SET u.isInvisible = true")
    void invisible(Long userAccountId);

    /**
     * 根据用户id查询
     */
    UserAccountOnline findByUserAccountId(Long userAccountId);


    /**
     * 根据用户id查询是否存在
     */
    Boolean existsByUserAccountId(Long userAccountId);

    /**
     * 根据最大心跳时间查询
     */
    @Query("SELECT u FROM UserAccountOnline u WHERE u.isOnline = true AND u.gmtLastHeartbeat < :maxGmtLastHeartbeat")
    List<UserAccountOnline> getByMaxGmtLastHeartbeatAndIsOnline(@Param("maxGmtLastHeartbeat") Date maxGmtLastHeartbeat);

}
