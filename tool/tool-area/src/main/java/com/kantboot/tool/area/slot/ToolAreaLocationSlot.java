package com.kantboot.tool.area.slot;

import com.kantboot.tool.area.domain.entity.ToolAreaLocation;
import com.kantboot.util.rest.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class ToolAreaLocationSlot {

    /**
     * 根据经纬度获取位置
     * Get location by latitude and longitude
     *
     * @param longitude 经度
     * @param latitude 纬度
     *
     */
    public ToolAreaLocation getLocationByLongitudeAndLatitude(BigDecimal longitude, BigDecimal latitude) {
        log.info("getLocationByLatAndLng longitude: {}, latitude: {}", longitude, latitude);
        // 提示工具区域模块未使用地图插件
        throw BaseException.of("toolAreaSlotNotUseMapPlugin", "工具区域模块未使用地图插件", "zh_CN");
    }

}
