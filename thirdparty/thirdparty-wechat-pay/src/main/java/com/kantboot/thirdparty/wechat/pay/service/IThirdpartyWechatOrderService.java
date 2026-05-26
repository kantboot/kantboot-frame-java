package com.kantboot.thirdparty.wechat.pay.service;

import com.kantboot.thirdparty.wechat.pay.domain.entity.ThirdpartyWechatPayOrder;

/**
 * 关于微信支付的服务
 */
public interface IThirdpartyWechatOrderService {

    /**
     * 获取支付返回值
     * 根据小程序支付
     * @param orderId 订单id
     * @param code 小程序code
     * @return 支付返回值
     */
    Object getPayResultMiniprogram(Long orderId, String code);

    /**
     * 根据订单ID检测是否支付成功
     */
    Boolean checkPaySuccess(Long payOrderId);

    ThirdpartyWechatPayOrder getByPayOrderId(Long payOrderId);

}
