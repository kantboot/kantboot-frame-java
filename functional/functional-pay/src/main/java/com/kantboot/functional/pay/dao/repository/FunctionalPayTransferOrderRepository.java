package com.kantboot.functional.pay.dao.repository;

import com.kantboot.functional.pay.domain.entity.FunctionalPayTransferOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FunctionalPayTransferOrderRepository
    extends JpaRepository<FunctionalPayTransferOrder,Long> {

    /**
     * 根据订单ID和订单状态编码获取订单
     */
    FunctionalPayTransferOrder findByIdAndStatusCode(Long id, String statusCode);

    /**
     * 根据用户ID和订单状态编码获取最后一个订单
     */
    FunctionalPayTransferOrder findFirstByUserAccountIdAndStatusCodeOrderByIdDesc(Long userAccountId, String statusCode);

    /**
     * 根据用户账号ID和订单状态查询列表
     */
    List<FunctionalPayTransferOrder> findByUserAccountIdAndStatusCodeOrderByIdDesc(Long userAccountId, String statusCode);

}
