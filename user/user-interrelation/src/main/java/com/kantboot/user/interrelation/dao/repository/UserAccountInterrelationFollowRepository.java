package com.kantboot.user.interrelation.dao.repository;

import com.kantboot.user.interrelation.domain.dto.InterrelationSearchDTO;
import com.kantboot.user.interrelation.domain.entity.UserAccountInterrelationFollow;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserAccountInterrelationFollowRepository
    extends JpaRepository<UserAccountInterrelationFollow,Long> {

    /**
     * 根据关注者和被关注者查询
     */
    boolean existsByUserAccountIdOfFollowerAndUserAccountIdOfFollowed(
        Long userAccountIdOfFollower,
        Long userAccountIdOfFollowed
    );

    /**
     * 根据关注者和被关注者删除
     */
    @Modifying
    @Transactional
    void deleteByUserAccountIdOfFollowerAndUserAccountIdOfFollowed(
        Long userAccountIdOfFollower,
        Long userAccountIdOfFollowed);

    /**
     * 根据用户账号ID查询关注的人的数量
     */
    Long countByUserAccountIdOfFollower(Long userAccountIdOfFollower);

    /**
     * 根据用户账号ID查询被关注的人的数量
     */
    Long countByUserAccountIdOfFollowed(Long userAccountIdOfFollowed);

    /**
     * 计算互相关注的数量
     */
    @Query("""
        SELECT COUNT(f1)
        FROM UserAccountInterrelationFollow f1, UserAccountInterrelationFollow f2
        WHERE f1.userAccountIdOfFollower = f2.userAccountIdOfFollowed
          AND f1.userAccountIdOfFollowed = f2.userAccountIdOfFollower
          AND f1.userAccountIdOfFollower = :userAccountId
    """)
    Long countMutualFollows(Long userAccountId);

    /**
     * 查询追随者
     */
    @Query("""
        SELECT t
        FROM UserAccountInterrelationFollow t
        LEFT JOIN UserAccount user
        ON t.userAccountIdOfFollower = user.id
        WHERE t.userAccountIdOfFollowed = :#{#param.userAccountId}
        AND (:#{#param.keyword} IS NULL OR :#{#param.keyword} = '' OR LOWER(user.nickname) LIKE CONCAT('%',LOWER(:#{#param.keyword}),'%'))
        AND (:#{#param.maxId} IS NULL OR t.id < :#{#param.maxId})
        AND (:#{#param.minId} IS NULL OR t.id > :#{#param.minId})
        ORDER BY t.id DESC
        """)
    Page<UserAccountInterrelationFollow> findFollowedPage(
        @Param("param") InterrelationSearchDTO searchDTO,
        Pageable pageable
    );

    /**
     * 查询追随者
     */
    @Query("""
        SELECT t
        FROM UserAccountInterrelationFollow t
        LEFT JOIN UserAccount user
        ON t.userAccountIdOfFollowed = user.id
        WHERE t.userAccountIdOfFollower = :#{#param.userAccountId}
        AND (:#{#param.keyword} IS NULL OR :#{#param.keyword} = '' OR LOWER(user.nickname) LIKE CONCAT('%',LOWER(:#{#param.keyword}),'%'))
        AND (:#{#param.maxId} IS NULL OR t.id < :#{#param.maxId})
        AND (:#{#param.minId} IS NULL OR t.id > :#{#param.minId})
        ORDER BY t.id DESC
        """)
    Page<UserAccountInterrelationFollow> findFollowerPage(
        @Param("param") InterrelationSearchDTO searchDTO,
        Pageable pageable
    );

    /**
     * 查询互相关注
     */
    /**
     * 查询互相关注
     */
    @Query("""
    SELECT f1
    FROM UserAccountInterrelationFollow f1, UserAccountInterrelationFollow f2
    WHERE f1.userAccountIdOfFollower = f2.userAccountIdOfFollowed
      AND f1.userAccountIdOfFollowed = f2.userAccountIdOfFollower
      AND f1.userAccountIdOfFollower = :#{#param.userAccountId}
      AND (:#{#param.keyword} IS NULL OR :#{#param.keyword} = '' OR LOWER(f1.userAccountOfFollowed.nickname) LIKE CONCAT('%',LOWER(:#{#param.keyword}),'%'))
      AND (:#{#param.maxId} IS NULL OR f1.id < :#{#param.maxId})
      AND (:#{#param.minId} IS NULL OR f1.id > :#{#param.minId})
    ORDER BY f1.id DESC
    """)
    Page<UserAccountInterrelationFollow> findMutualFollowPage(
        @Param("param") InterrelationSearchDTO searchDTO,
        Pageable pageable
    );

}
