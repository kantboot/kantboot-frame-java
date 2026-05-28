package com.kantboot.util.location.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 位置实体类
 * 用于表示地理位置
 * 可以扩展添加更多属性，如经度、纬度等
 */
@Data
@Accessors(chain = true)
public class Location implements Serializable {

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;


}
