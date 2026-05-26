package com.kantboot.thirdparty.github.dao.repository;

import com.kantboot.thirdparty.github.domain.entity.ThirdpartyGithubUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThirdpartyGithubUserInfoRepository
    extends JpaRepository<ThirdpartyGithubUserInfo, Long> {

    /**
     * 根据githubId获取用户信息
     */
    ThirdpartyGithubUserInfo getByGithubId(Long githubId);

}
