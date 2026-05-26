package com.kantboot.util.location.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 位置实体类、
 * 用于返回最大和最小经纬度信息。
 */
@Data
@Accessors(chain = true)
public class LocationMaxMin implements Serializable {

    /**
     * 最大经度
     */
    private BigDecimal longitudeMax;

    /**
     * 最小经度
     */
    private BigDecimal longitudeMin;

    /**
     * 最大纬度
     */
    private BigDecimal latitudeMax;

    /**
     * 最小纬度
     */
    private BigDecimal latitudeMin;

}
