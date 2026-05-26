package com.kantboot.user.account.service;

/**
 * 用户账户令牌服务
 */
public interface IUserAccountTokenService {

    /**
     * 获取过期时长
     */
    long getExpireTime();

    /**
     * 生成令牌
     */
    String generateToken(Long userAccountId);

    /**
     * 是否登录
     */
    boolean isLogin();

    /**
     * 获取用户账户ID
     */
    Long getUserAccountIdBySelf();

    /**
     * token切换用户
     */
    void switchUser(Long userAccountId);

    /**
     * 删除自身的token
     */
    void removeTokenBySelf();

    /**
     * 根据用户id删除所有TOKEN
     */

    void removeTokenByUserAccountId(Long userAccountId);

    /**
     * 设置过期时间
     */
    void setGmtExpire(String token,long expireTime);

}
