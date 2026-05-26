package com.kantboot.thirdparty.wechat.pay.dao.repository;

import com.kantboot.thirdparty.wechat.pay.domain.entity.ThirdpartyWechatPayOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThirdpartyWechatPayOrderRepository
    extends JpaRepository<ThirdpartyWechatPayOrder,Long> {

    /**
     * 根据订单号查询订单
     */
    ThirdpartyWechatPayOrder findByPayOrderId(Long payOrderId);

}
