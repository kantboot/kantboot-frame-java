package com.kantboot.functional.pay.dao.repository;

import com.kantboot.functional.pay.domain.entity.FunctionalPayTransferOrder;
import com.kantboot.functional.pay.domain.entity.FunctionalPayTransferOrderLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FunctionalPayTransferOrderLogRepository
    extends JpaRepository<FunctionalPayTransferOrderLog,Long> {

    /**
     * 根据订单ID和订单状态编码获取订单
     */
    FunctionalPayTransferOrder findByIdAndStatusCode(Long id, String statusCode);


}
