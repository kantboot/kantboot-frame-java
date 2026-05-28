package com.kantboot.functional.pay.service.impl;

import cn.hutool.core.util.StrUtil;
import com.kantboot.functional.pay.dao.repository.FunctionalPayInvoiceOrderRepository;
import com.kantboot.functional.pay.dao.repository.FunctionalPayInvoiceRepository;
import com.kantboot.functional.pay.dao.repository.FunctionalPayOrderRepository;
import com.kantboot.functional.pay.domain.entity.FunctionalPayInvoice;
import com.kantboot.functional.pay.domain.entity.FunctionalPayInvoiceOrder;
import com.kantboot.functional.pay.domain.entity.FunctionalPayOrder;
import com.kantboot.functional.pay.service.IFunctionalPayInvoiceService;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.util.event.emit.EventEmit;
import com.kantboot.util.rest.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FunctionalPayInvoiceServiceImpl
    implements IFunctionalPayInvoiceService {

    @Resource
    private FunctionalPayInvoiceRepository repository;

    @Resource
    private FunctionalPayInvoiceOrderRepository invoiceOrderRepository;

    @Resource
    private FunctionalPayOrderRepository payOrderRepository;
    
    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private EventEmit eventEmit;

    @Override
    public FunctionalPayInvoice getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void applyInvoice(FunctionalPayInvoice invoice) {
        Long selfId = userAccountService.getSelfId();
        invoice.setId(null);
        invoice.setFileIdOfInvoice(null);
        invoice.setIsIssued(false);
        invoice.setUserAccountId(selfId);

        List<Long> payOrderIds = invoice.getPayOrderIds();
        BigDecimal amount = BigDecimal.ZERO;
        List<FunctionalPayOrder> byIdIn = payOrderRepository.findByIdIn(payOrderIds);
        for (FunctionalPayOrder functionalPayOrder : byIdIn) {
            BigDecimal add = amount.add(functionalPayOrder.getAmount());
            amount = add;
        }
        invoice.setAmount(amount);
        FunctionalPayInvoice save = repository.save(invoice);

            List<FunctionalPayInvoiceOrder> functionalPayInvoiceOrders = new ArrayList<>();
            for (FunctionalPayOrder functionalPayOrder : byIdIn) {
                FunctionalPayInvoiceOrder functionalPayInvoiceOrder = new FunctionalPayInvoiceOrder();
                functionalPayInvoiceOrder.setInvoiceId(save.getId());
                functionalPayInvoiceOrder.setPayOrderId(functionalPayOrder.getId());
                functionalPayInvoiceOrder.setUserAccountId(selfId);
                functionalPayInvoiceOrders.add(functionalPayInvoiceOrder);
            }
            invoiceOrderRepository.saveAll(functionalPayInvoiceOrders);
//        Thread.ofVirtual().name("FunctionalPayInvoiceServiceImpl.applyInvoice").start(()->{
//
//        });

    }

    @Override
    public void issueInvoice(FunctionalPayInvoice invoice) {
        FunctionalPayInvoice byId = repository.findById(invoice.getId()).orElse(null);
        if(byId==null){
            throw BaseException.of("invoiceMotFound","发票不存在","zh_CN");
        }
        byId.setIsIssued(true);
        byId.setInvoiceNo(invoice.getInvoiceNo());
        byId.setFileIdOfInvoice(invoice.getFileIdOfInvoice());
        byId.setGmtIssue(new Date());
        byId.setGmtIssueTime(System.currentTimeMillis());
        repository.save(byId);

        if(StrUtil.isNotEmpty(byId.getEmail())){
            eventEmit.to("FunctionalPayInvoice:issueInvoiceEmail", byId.getId());
        }

    }

    @Override
    public List<Long> getInvoicedOPayOrderIdsByUserAccountId(Long userAccountId) {
        List<FunctionalPayInvoiceOrder> byUserAccountId = invoiceOrderRepository.findByUserAccountId(userAccountId);
        if(byUserAccountId == null) {
            return new ArrayList<>();
        }
        List<Long> payOrderIds = new ArrayList<>();
        for (int i = 0; i < byUserAccountId.size(); i++) {
            payOrderIds.add(byUserAccountId.get(i).getPayOrderId());
        }
        return payOrderIds;
    }

    @Override
    public List<Long> getInvoicedOPayOrderIdsBySelf() {
        Long selfId = userAccountService.getSelfId();
        return getInvoicedOPayOrderIdsByUserAccountId(selfId);
    }

    @Override
    public List<FunctionalPayInvoice> getByUserAccountId(Long userAccountId) {
        return repository.findByUserAccountId(userAccountId);
    }

    @Override
    public List<FunctionalPayInvoice> getBySelf() {
        Long selfId = userAccountService.getSelfId();
        return getByUserAccountId(selfId);
    }
}
