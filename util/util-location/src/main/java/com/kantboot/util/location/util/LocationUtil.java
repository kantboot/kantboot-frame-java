package com.kantboot.util.location.util;

import com.kantboot.util.location.domain.Location;
import com.kantboot.util.location.domain.LocationMaxMin;

import java.math.BigDecimal;

/**
 * 地理位置工具类
 */
public class LocationUtil {

    /**
     * 计算两个经纬度之间的距离
     * 使用BigDecimal进行高精度计算
     * 单位：m
     */
    public static BigDecimal calculateDistance(
            Location location1,
            Location location2) {
        BigDecimal longitude1 = location1.getLongitude();
        BigDecimal latitude1 = location1.getLatitude();
        BigDecimal longitude2 = location2.getLongitude();
        BigDecimal latitude2 = location2.getLatitude();

        final int EARTH_RADIUS = 6371000; // 地球半径，单位：米

        double radLat1 = Math.toRadians(latitude1.doubleValue());
        double radLat2 = Math.toRadians(latitude2.doubleValue());
        double deltaLat = radLat1 - radLat2;
        double deltaLng = Math.toRadians(longitude1.doubleValue() - longitude2.doubleValue());

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return BigDecimal.valueOf(EARTH_RADIUS * c);
    }

    /**
     * 根据距离获取最大经纬度和最小经纬度
     */
    public static LocationMaxMin getMaxMinLocationByDistance(
            Location location,
            BigDecimal distance) {
        BigDecimal earthRadius = BigDecimal.valueOf(6371000); // 地球半径，单位：米
        BigDecimal radLatitude = BigDecimal.valueOf(Math.toRadians(location.getLatitude().doubleValue()));
        BigDecimal radLongitude = BigDecimal.valueOf(Math.toRadians(location.getLongitude().doubleValue()));

        // 计算最大和最小经纬度
        BigDecimal maxLatitude = location.getLatitude().add(distance.divide(earthRadius, 10, BigDecimal.ROUND_HALF_UP));
        BigDecimal minLatitude = location.getLatitude().subtract(distance.divide(earthRadius, 10, BigDecimal.ROUND_HALF_UP));
        BigDecimal maxLongitude = location.getLongitude().add(distance.divide(earthRadius.multiply(BigDecimal.valueOf(Math.cos(radLatitude.doubleValue()))), 10, BigDecimal.ROUND_HALF_UP));
        BigDecimal minLongitude = location.getLongitude().subtract(distance.divide(earthRadius.multiply(BigDecimal.valueOf(Math.cos(radLatitude.doubleValue()))), 10, BigDecimal.ROUND_HALF_UP));

        return new LocationMaxMin()
                .setLatitudeMax(maxLatitude)
                .setLatitudeMin(minLatitude)
                .setLongitudeMax(maxLongitude)
                .setLongitudeMin(minLongitude);
    }


}
