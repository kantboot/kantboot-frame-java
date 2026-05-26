package com.kantboot.fp.community.service;

import com.kantboot.fp.community.domain.entity.FpCommunityPost;
import com.kantboot.fp.community.domain.entity.FpCommunityPostCollect;

import java.util.List;

public interface IFpCommunityPostService {

    /**
     * 发布帖子
     */
    FpCommunityPost push(FpCommunityPost post);

    /**
     * 编辑帖子
     */
    FpCommunityPost edit(FpCommunityPost post);

    /**
     * 根据ID获取帖子
     */
    FpCommunityPost getById(Long id);

    /**
     * 审核通过
     */
    FpCommunityPost pass(Long id);

    /**
     * 审核不通过
     */
    FpCommunityPost reject(Long id,String reason);

    /**
     * 修改帖子信息
     */
    FpCommunityPost update(FpCommunityPost post);

    /**
     * 根据用户账号ID发布的所有帖子
     */
    List<FpCommunityPost> getByUserAccountIdOfPusher(Long userAccountIdOfPusher);

    /**
     * 根据当前用户账号ID发布的所有帖子
     */
    List<FpCommunityPost> getBySelfUserAccountIdOfPusher();

    /**
     * 删除帖子（逻辑删除）
     */
    void deleteById(Long id);

    /**
     * 收藏
     */
    void collect(Long postId);

    /**
     * 取消收藏
     */
    void unCollect(Long postId);

    /**
     * 获取收藏列表
     */
    List<FpCommunityPostCollect> getCollectList();
}
