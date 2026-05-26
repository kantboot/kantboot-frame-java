package com.kantboot.functional.pay.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.kantboot.functional.pay.domain.entity.FunctionalPayTransferOrder;
import com.kantboot.functional.pay.dao.repository.FunctionalPayTransferOrderLogRepository;
import com.kantboot.functional.pay.domain.entity.FunctionalPayTransferOrderLog;
import com.kantboot.functional.pay.service.IFunctionalPayTransferOrderLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;



@Service
public class FunctionalPayTransferOrderLogServiceImpl
    implements IFunctionalPayTransferOrderLogService {

    @Resource
    private FunctionalPayTransferOrderLogRepository repository;

    @Override
    public void addLog(FunctionalPayTransferOrder order) {
        FunctionalPayTransferOrderLog orderLog = BeanUtil.copyProperties(order, FunctionalPayTransferOrderLog.class);
        orderLog.setId(null);
        orderLog.setTransferOrderId(order.getId());
        repository.save(orderLog);
    }

}
