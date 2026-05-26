package com.kantboot.fp.community.dao.repository;

import com.kantboot.fp.community.domain.entity.FpCommunityPostCollect;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FpCommunityPostCollectRepository
    extends JpaRepository<FpCommunityPostCollect,Long> {

    /**
     * 根据 postId 和 userAccountId 查询收藏记录
     */
    FpCommunityPostCollect findByPostIdAndUserAccountId(Long postId, Long userAccountId);

    /**
     * 根据 userAccountId 获取收藏列表
     */
    List<FpCommunityPostCollect> findByUserAccountId(Long userAccountId);

}
