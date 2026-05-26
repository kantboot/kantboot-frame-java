package com.kantboot.fp.community.dao.repository;

import com.kantboot.fp.community.domain.entity.FpCommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FpCommunityPostRepository
    extends JpaRepository<FpCommunityPost, Long> {

    @Query("""
        select p from FpCommunityPost p
        where p.userAccountIdOfPusher = ?1
            and p.isDelete = false
    """)
    List<FpCommunityPost> findByUserAccountIdOfPusher(Long userAccountIdOfPusher);

}
