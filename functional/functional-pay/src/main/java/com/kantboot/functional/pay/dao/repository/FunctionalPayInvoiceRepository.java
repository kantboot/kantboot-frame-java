package com.kantboot.functional.pay.dao.repository;

import com.kantboot.functional.pay.domain.entity.FunctionalPayInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FunctionalPayInvoiceRepository
    extends JpaRepository<FunctionalPayInvoice,Long> {

    /**
     * 根据用户账号ID查询
     */
    List<FunctionalPayInvoice> findByUserAccountId(Long userAccountId);

}
