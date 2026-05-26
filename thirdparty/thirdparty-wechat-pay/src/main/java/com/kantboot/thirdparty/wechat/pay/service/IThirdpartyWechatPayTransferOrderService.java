package com.kantboot.thirdparty.wechat.pay.service;

/**
 * 关于微信转账订单的服务
 */
public interface IThirdpartyWechatPayTransferOrderService {

    /**
     * 获取转账返回值
     * 根据小程序支付
     * @param orderId 订单id
     * @param code 小程序code
     * @return 支付返回值
     */
    Object getTransferResultMiniprogram(Long orderId, String code);


    /**
     * 根据订单ID检测是否转账成功
     */
    Boolean checkTransferSuccess(Long orderId);

    /**
     * 主动撤销转账（用户未确认前）
     */
    Boolean cancelTransfer(Long orderId);


}
