package com.kantboot.tool.ip.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseI18nEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "tool_ip")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class ToolIp
        extends BaseI18nEntity
        implements Serializable {

    /**
     * 海拔
     */
    @Column(name = "elevation")
    private String elevation;

    /**
     * 洲编码
     */
    @Column(name = "continent_code", length = 64)
    private String continentCode;

    /**
     * ip
     */
    @Column(name = "ip")
    private String ip;

    /**
     * 运营商
     */
    @Column(name = "isp")
    private String isp;

    /**
     * 时区
     */
    @Column(name = "time_zone")
    private String timeZone;

    /**
     * 邮编
     */
    @Column(name = "zip_code")
    private String zipCode;

    /**
     * 气象站
     */
    @Column(name = "weather_station")
    private String weatherStation;

    /**
     * 国家
     */
    @Column(name = "lv_0_name")
    private String lv0Name;

    /**
     * 省份
     */
    @Column(name = "lv_1_name")
    private String lv1Name;

    /**
     * 城市
     */
    @Column(name = "lv_2_name")
    private String lv2Name;

    /**
     * 经度
     */
    @Column(name = "longitude", precision = 16, scale = 8)
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Column(name = "latitude", precision = 16, scale = 8)
    private BigDecimal latitude;

    /**
     * 编码
     */
    @Column(name = "area_code",length = 64)
    private String areaCode;

    /**
     * 请求得到的数据
     */
    @Column(name = "data_of_request", columnDefinition = "TEXT")
    private String dataOfRequest;

}
