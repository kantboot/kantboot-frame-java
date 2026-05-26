package com.kantboot.functional.pay.service;

import com.kantboot.functional.pay.domain.entity.FunctionalPayInvoice;

import java.util.List;

public interface IFunctionalPayInvoiceService {

    /**
     * 根据ID查询
     */
    FunctionalPayInvoice getById(Long id);

    /**
     * 申请开具发票
     */
    void applyInvoice(FunctionalPayInvoice invoice);

    /**
     * 开具发票
     */
    void issueInvoice(FunctionalPayInvoice invoice);

    /**
     * 根据用户账号ID查询已开具发票的支付订单ID
     */
    List<Long> getInvoicedOPayOrderIdsByUserAccountId(Long userAccountId);


    /**
     * 获取当前用户已开具发票的支付订单ID
     */
    List<Long> getInvoicedOPayOrderIdsBySelf();

    /**
     * 获取发票
     */
    List<FunctionalPayInvoice> getByUserAccountId(Long userAccountId);

    /**
     * 获取当前用户发票
     */
    List<FunctionalPayInvoice> getBySelf();


}
