package com.kantboot.thirdparty.wechat.pay.dao.repository;

import com.kantboot.thirdparty.wechat.pay.domain.entity.ThirdpartyWechatPayTransferOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThirdpartyWechatPayTransferOrderRepository
    extends JpaRepository<ThirdpartyWechatPayTransferOrder,Long> {

    /**
     * 根据转账ID查询
     */
    ThirdpartyWechatPayTransferOrder findByTransferOrderId(Long transferOrderId);

    // 同一个 transferOrderId 可能有多条，按 id 倒序拿最新的
    ThirdpartyWechatPayTransferOrder findTopByTransferOrderIdOrderByIdDesc(Long transferOrderId);

    // outBillNo(你就是 orderId+"") 更稳定：同一转账单号找最新
    ThirdpartyWechatPayTransferOrder findTopByOutBillNoOrderByIdDesc(String outBillNo);

    // 如果你想在代码里做更强策略，也可以拿列表
    List<ThirdpartyWechatPayTransferOrder> findByTransferOrderIdOrderByIdDesc(Long transferOrderId);

}
