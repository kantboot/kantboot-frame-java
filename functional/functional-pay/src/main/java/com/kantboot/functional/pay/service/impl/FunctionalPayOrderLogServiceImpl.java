package com.kantboot.functional.pay.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.kantboot.functional.pay.dao.repository.FunctionalPayOrderLogRepository;
import com.kantboot.functional.pay.domain.entity.FunctionalPayOrder;
import com.kantboot.functional.pay.service.IFunctionalPayOrderLogService;
import com.kantboot.functional.pay.order.domain.entity.FunctionalPayOrderLog;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class FunctionalPayOrderLogServiceImpl
        implements IFunctionalPayOrderLogService {

    @Resource
    private FunctionalPayOrderLogRepository repository;

    @Override
    public void addLog(FunctionalPayOrder order) {
        // 保存支付订单日志到数据库
        FunctionalPayOrderLog orderLog = BeanUtil.copyProperties(order, FunctionalPayOrderLog.class);
        orderLog.setId(null);
        orderLog.setPayOrderId(order.getId());
        repository.save(orderLog);
    }

}
