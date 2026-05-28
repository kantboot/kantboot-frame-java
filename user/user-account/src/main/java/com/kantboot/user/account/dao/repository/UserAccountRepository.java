package com.kantboot.user.account.dao.repository;

import com.kantboot.user.account.domain.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long>{

    /**
     * 搜索用户，搜索用户名、手机号、邮箱
     * 如果keyword可以为数字，则可以搜索ID
     */
    @Query("""
    SELECT ua FROM  UserAccount ua
    WHERE ua.username LIKE CONCAT('%', :keyword, '%')
    OR ua.phone LIKE CONCAT('%', :keyword, '%')
    OR ua.email LIKE CONCAT('%', :keyword, '%')
    OR ua.phoneAreaCode LIKE CONCAT('%', :keyword, '%')
    OR ua.realName LIKE CONCAT('%', :keyword, '%')
    OR ua.nickname LIKE CONCAT('%', :keyword, '%')
    OR ua.introduction LIKE CONCAT('%', :keyword, '%')
    OR CONCAT('', ua.id, '') LIKE CONCAT('%', :keyword, '%')
    OR :keyword IS NULL
    OR :keyword = ''
    """)
    Page<UserAccount> search(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 根据用户名前缀查询
     */
    @Query("""
            SELECT ua FROM UserAccount ua
            WHERE ua.username LIKE CONCAT(:prefix, '%') 
    """)
    List<UserAccount> findByUsernamePrefix(@Param("prefix") String prefix);

    /**
     * 根据用户名前缀获取数量
     */
    @Query("""
            SELECT COUNT(ua) FROM UserAccount ua
            WHERE ua.username LIKE CONCAT(:prefix, '%')
            """)
    Long countByUsernamePrefix(@Param("prefix") String prefix);

    /**
     * 根据用户名查询，不区分大小写
     */
    @Query("""
        SELECT ua FROM UserAccount ua 
        WHERE lower(ua.username) = lower(:username)
    """)
    UserAccount findByUsername(String username);

    /**
     * 根据用户名模糊查询
     */
    @Query("""
            select ua from UserAccount ua
            where ua.username like concat('%', :username, '%')
    """)
    List<UserAccount> findByUsernameVague(@Param("username") String username);

    /**
     * 根据手机号查询
     */
    UserAccount findByPhoneAreaCodeAndPhone(String phoneAreaCode, String phone);

    /**
     * 根据邮箱查询
     */
    UserAccount findByEmail(String email);

    /**
     * 根据手机区号和手机号码查询是否存在
     * @param phoneAreaCode 手机区号
     * @param phone 手机号码
     * @return 是否存在
     */
    boolean existsByPhoneAreaCodeAndPhone(String phoneAreaCode, String phone);

    /**
     * 根据邮箱查询是否存在
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 根据用户名查询是否存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 根据ids查询
     */
    @Query("""
            select ua from UserAccount ua
            where ua.id in :ids
    """)
    List<UserAccount> getByIds(List<Long> ids);

    /**
     * 根据直属码查询
     */
    @Query("""
            select ua from UserAccount ua
            where ua.directCode = :directCode
    """)
    UserAccount getByDirectCode(String directCode);

    @Query("""
        FROM UserAccount ua
        WHERE ua.userAccountIdOfInviter = :userAccountIdOfInviter
    """)
    List<UserAccount> getByUserAccountIdOfInviter(Long userAccountIdOfInviter);

    /**
     * 根据是否是管理员查询
     */
    @Query("""
        FROM UserAccount ua
        WHERE ua.isAdmin = :isAdmin
    """)
    List<UserAccount> findByIsAdmin(Boolean isAdmin);

}
