package com.kantboot.functional.pay.service;

import com.kantboot.functional.pay.domain.dto.PayOrderGenerateDTO;
import com.kantboot.functional.pay.domain.dto.PayTransferFailDTO;
import com.kantboot.functional.pay.domain.entity.FunctionalPayTransferOrder;

import java.util.List;

public interface IFunctionalPayTransferOrderService {

    /**
     * 根据ID获取
     */
    FunctionalPayTransferOrder getById(Long id);

    /**
     * 生成支付订单
     */
    FunctionalPayTransferOrder generate(PayOrderGenerateDTO dto);

    /**
     * 生成自身支付订单
     */
    FunctionalPayTransferOrder generateSelf(PayOrderGenerateDTO dto);

    /**
     * 修改订单转账方式
     */
    FunctionalPayTransferOrder confirmTransferMethodCodeById(Long id, String transferMethodCode);

    /**
     * 开始转账
     */
    FunctionalPayTransferOrder startTransfer(Long id);

    /**
     * 完成转账
     */
    FunctionalPayTransferOrder transferComplete(Long id);

    /**
     * 开始取消转账
     */
    FunctionalPayTransferOrder startTransferCancel(Long id);

    /**
     * 取消转账
     */
    FunctionalPayTransferOrder transferCancelComplete(Long id);

    /**
     * 转账失败
     */
    FunctionalPayTransferOrder transferFail(PayTransferFailDTO dto);

    /**
     * 根据用户账号ID和状态编码查询订单列表
     */
    List<FunctionalPayTransferOrder> getByUserAccountIdAndStatusCode(Long userAccountId, String statusCode);

    /**
     * 根据状态编码查询当前用户的订单列表
     */
    List<FunctionalPayTransferOrder> getSelfByStatusCode(String statusCode);

}
