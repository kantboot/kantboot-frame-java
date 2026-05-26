package com.kantboot.thirdparty.google.dao.repository;

import com.kantboot.thirdparty.google.domain.entity.ThirdpartyGoogleUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThirdpartyGoogleUserInfoRepository
    extends JpaRepository<ThirdpartyGoogleUserInfo, Long> {

    ThirdpartyGoogleUserInfo findByGoogleId(String googleId);

}
