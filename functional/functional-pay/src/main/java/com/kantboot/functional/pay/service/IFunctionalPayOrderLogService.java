package com.kantboot.functional.pay.service;

import com.kantboot.functional.pay.domain.entity.FunctionalPayOrder;

public interface IFunctionalPayOrderLogService {

    void addLog(FunctionalPayOrder order);

}
