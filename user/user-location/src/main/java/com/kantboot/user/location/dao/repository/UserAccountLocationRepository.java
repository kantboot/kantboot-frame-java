package com.kantboot.user.location.dao.repository;

import com.kantboot.user.location.domain.entity.UserAccountLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountLocationRepository extends JpaRepository<UserAccountLocation,Long> {

    /**
     * 根据用户账号ID查询用户的地理位置信息
     * @param userAccountId 用户账号ID
     * @return 用户的位置信息
     */
    UserAccountLocation findByUserAccountId(Long userAccountId);

}
