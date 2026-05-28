package com.kantboot.thirdparty.wechat.mp.dao.repository;

import com.kantboot.thirdparty.wechat.mp.domain.entity.ThirdpartyWechatMiniprogramRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThirdpartyWechatMiniprogramRequestRepository
    extends JpaRepository<ThirdpartyWechatMiniprogramRequest,Long> {

    ThirdpartyWechatMiniprogramRequest findByCode(String code);

}
