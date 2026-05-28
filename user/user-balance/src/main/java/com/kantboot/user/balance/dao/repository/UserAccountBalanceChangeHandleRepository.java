package com.kantboot.user.balance.dao.repository;

import com.kantboot.user.balance.domain.entity.UserAccountBalanceChangeHandle;
import com.kantboot.util.jpa.param.PageParam;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserAccountBalanceChangeHandleRepository
    extends JpaRepository<UserAccountBalanceChangeHandle,Long> {

    /**
     * 根据状态编码查询
     */
    List<UserAccountBalanceChangeHandle> findByStatusCode(String statusCode);

    /**
     * 根据状态编码和是否被锁定查询
     */
    List<UserAccountBalanceChangeHandle> findByStatusCodeAndLocked(String statusCode, Boolean locked);

    Page<UserAccountBalanceChangeHandle> findPageByStatusCodeAndLocked(String statusCode,Boolean locked, Pageable pageable);

    /**
     * 根据uuid查询
     */
    UserAccountBalanceChangeHandle findByUuid(String uuid);

    /**
     * 根据uuid删除
     */
    @Transactional
    @Modifying
    void deleteByUuid(String uuid);

    /**
     * 查询状态编码和提交状态编码不同的处理
     */
    @Query("""
            FROM UserAccountBalanceChangeHandle t
            WHERE t.statusCode != t.commitStatusCode
            OR t.commitStatusCode IS NULL
        """)
    List<UserAccountBalanceChangeHandle> findByStatusCodeNotAndCommitStatusCodeNot();

    /**
     * 分页查询状态编码和提交状态编码不同的处理
     */
    @Query("""
            FROM UserAccountBalanceChangeHandle t
            WHERE t.statusCode != t.commitStatusCode
            OR t.commitStatusCode IS NULL
        """)
    Page<UserAccountBalanceChangeHandle> findPageByStatusCodeNotAndCommitStatusCodeNot(Pageable pageable);

    /**
     * 根据UUID修改提交状态编码
     */
    @Transactional
    @Modifying
    @Query("""
            UPDATE UserAccountBalanceChangeHandle
            SET commitStatusCode = :commitStatusCode
            WHERE uuid = :uuid
        """)
    void updateCommitStatusCodeByUuid(
            String uuid,
            String commitStatusCode);

}
