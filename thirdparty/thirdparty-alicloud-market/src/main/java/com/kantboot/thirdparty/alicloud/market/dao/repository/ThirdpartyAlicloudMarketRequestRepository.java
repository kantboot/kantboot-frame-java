package com.kantboot.thirdparty.alicloud.market.dao.repository;

import com.kantboot.thirdparty.alicloud.market.domain.entity.ThirdpartyAlicloudMarketRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThirdpartyAlicloudMarketRequestRepository
    extends JpaRepository<ThirdpartyAlicloudMarketRequest, Long> {

    /**
     * 根据code获取实体
     */
    ThirdpartyAlicloudMarketRequest findByCode(String code);

}
