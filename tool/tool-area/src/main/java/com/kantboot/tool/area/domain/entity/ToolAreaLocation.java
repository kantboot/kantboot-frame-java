package com.kantboot.tool.area.domain.entity;

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
@Table(name = "tool_area_location")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class ToolAreaLocation
        extends BaseI18nEntity
        implements Serializable {

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
     * 地区编码
     */
    @Column(name = "area_code", length = 64)
    private String areaCode;

    /**
     * 具体位置信息
     */
    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    /**
     * 请求得到的数据
     */
    @Column(name = "data_of_request", columnDefinition = "TEXT")
    private String dataOfRequest;

    /**
     * 是否不计算次数
     */
    @Transient
    private Boolean isNotCount = false;


}
