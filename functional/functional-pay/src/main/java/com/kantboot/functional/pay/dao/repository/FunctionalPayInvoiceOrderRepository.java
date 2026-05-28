package com.kantboot.functional.pay.dao.repository;

import com.kantboot.functional.pay.domain.entity.FunctionalPayInvoice;
import com.kantboot.functional.pay.domain.entity.FunctionalPayInvoiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FunctionalPayInvoiceOrderRepository
    extends JpaRepository<FunctionalPayInvoiceOrder,Long> {

    /**
     * 根据用户账号ID查询
     */
    List<FunctionalPayInvoiceOrder> findByUserAccountId(Long userAccountId);

}
