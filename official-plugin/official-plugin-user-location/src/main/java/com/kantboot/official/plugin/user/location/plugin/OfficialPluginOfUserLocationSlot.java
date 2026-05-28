package com.kantboot.official.plugin.user.location.plugin;

import cn.hutool.core.util.StrUtil;
import com.kantboot.tool.area.domain.entity.ToolArea;
import com.kantboot.tool.area.domain.entity.ToolAreaLocation;
import com.kantboot.tool.area.service.IToolAreaLocationService;
import com.kantboot.tool.area.service.IToolAreaService;
import com.kantboot.tool.ip.service.IToolIpService;
import com.kantboot.user.location.slot.UserAccountLocationSlot;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class OfficialPluginOfUserLocationSlot {

    @Resource
    private IToolIpService toolIpService;

    @Resource
    private IToolAreaLocationService toolAreaLocationService;

    @Bean
    public UserAccountLocationSlot userAccountLocationSlot() {
        return new UserAccountLocationSlot() {
            @Override
            public String getAreaCode(BigDecimal longitude, BigDecimal latitude, String ip) {
                // 判断经度纬度是否为空
                if (longitude == null || latitude == null) {
                    return getAreaCodeByIp(ip);
                }
                // 判断IP是否为空
                if (ip == null || ip.isEmpty()) {
                    return getAreaCodeByIp(ip);
                }
                // 如果IP查询不到，则尝试根据经纬度查询
                String areaCode = getAreaCodeByIp(ip);
                if (StrUtil.isNotEmpty(areaCode)) {
                    return areaCode;
                }
                areaCode = getAreaCodeByLongitudeAndLatitude(longitude, latitude);
                if (StrUtil.isNotEmpty(areaCode)) {
                    return areaCode;
                }
                // 如果都没有获取到，则返回null
                return null;
            }
        };
    }

    /**
     * 根据IP获取区域编码
     */
    private String getAreaCodeByIp(String ip) {
        try {
            return toolIpService.getByIp(ip).getAreaCode();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据经纬度获取区域编码
     */
    private String getAreaCodeByLongitudeAndLatitude(BigDecimal longitude, BigDecimal latitude) {
        ToolAreaLocation locationByLongitudeAndLatitude = toolAreaLocationService.getLocationByLongitudeAndLatitude(longitude, latitude);
        if (locationByLongitudeAndLatitude != null) {
            return locationByLongitudeAndLatitude.getAreaCode();
        }
        return null;
    }

}
