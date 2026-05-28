package com.kantboot.tool.area.service;

import com.kantboot.tool.area.domain.entity.ToolAreaLocation;

import java.math.BigDecimal;

public interface IToolAreaLocationService {

    ToolAreaLocation getLocationByLongitudeAndLatitude(BigDecimal longitude, BigDecimal latitude);

    ToolAreaLocation getLocationBySelf();

}
