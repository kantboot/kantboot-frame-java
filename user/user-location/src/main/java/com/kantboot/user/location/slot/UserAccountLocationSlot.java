package com.kantboot.user.location.slot;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UserAccountLocationSlot {

    /**
     * 获取用户账号的区域编码
     * 需要配置插槽才能生效
     * @param longitude 经度
     * @param latitude 纬度
     * @param ip 用户IP地址
     * @return 用户账号的区域编码
     */
    public String getAreaCode(BigDecimal longitude, BigDecimal latitude,String ip) {
        return null;
    }

}
