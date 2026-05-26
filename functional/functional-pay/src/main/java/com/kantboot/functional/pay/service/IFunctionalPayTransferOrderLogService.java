package com.kantboot.functional.pay.service;

import com.kantboot.functional.pay.domain.entity.FunctionalPayTransferOrder;

public interface IFunctionalPayTransferOrderLogService {

    void addLog(FunctionalPayTransferOrder order);

}
