package com.kantboot.user.interrelation.service;

import com.kantboot.user.interrelation.domain.dto.InterrelationSearchDTO;
import com.kantboot.user.interrelation.domain.entity.UserAccountInterrelation;
import com.kantboot.user.interrelation.domain.entity.UserAccountInterrelationFollow;

import java.util.List;

public interface IUserAccountInterrelationService {

    /**
     * 关注
     */
    void follow(Long userAccountId);

    /**
     * 取消关注
     */
    void unFollow(Long userAccountId);

    /**
     * 更新关系数量
     */
    UserAccountInterrelation updateRelationCount(Long userAccountId);

    /**
     * 根据用户账号ID获取
     */
    UserAccountInterrelation getByUserAccountId(Long userAccountId);

    /**
     * 根据当前用户获取
     */
    UserAccountInterrelation getBySelf();

    /**
     * 根据用户账号Id判断是否已关注
     */
    boolean isFollow(Long userAccountId);

    /**
     * 根据用户ID查询追随者列表
     */
    List<UserAccountInterrelationFollow> getFollowedList(InterrelationSearchDTO dto);

    /**
     * 获取用户自身的追随者列表
     */
    List<UserAccountInterrelationFollow> getSelfFollowedList(InterrelationSearchDTO dto);

    /**
     * 根据用户ID查询关注者列表
     */
    List<UserAccountInterrelationFollow> getFollowerList(InterrelationSearchDTO dto);

    /**
     * 获取用户自身的关注列表
     */
    List<UserAccountInterrelationFollow> getSelfFollowerList(InterrelationSearchDTO dto);

    /**
     * 根据用户ID查询互相关注列表
     * findMutualFollowPage
     */
    List<UserAccountInterrelationFollow> getMutualFollowList(InterrelationSearchDTO dto);

    /**
     * 获取用户自身的互相关注列表
     */
    List<UserAccountInterrelationFollow> getSelfMutualFollowList(InterrelationSearchDTO dto);

}
