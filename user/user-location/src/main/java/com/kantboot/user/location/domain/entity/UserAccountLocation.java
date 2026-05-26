package com.kantboot.user.location.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseEntity;
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
@Table(name = "user_account_location")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class UserAccountLocation
        extends BaseEntity
        implements Serializable {

    /**
     * 用户账户id
     * User account id
     */
    @Column(name = "user_account_id",unique = true)
    private Long userAccountId;

    /**
     * 纬度
     * Latitude
     */
    @Column(name = "latitude", precision = 16, scale = 8)
    private BigDecimal latitude;

    /**
     * 经度
     * Longitude
     */
    @Column(name = "longitude", precision = 16, scale = 8)
    private BigDecimal longitude;

    /**
     * 用户IP
     * User IP
     */
    @Column(name = "ip")
    private String ip;

    /**
     * 区域编码
     */
    @Column(name = "area_code", length = 64)
    private String areaCode;

    /**
     * 根据IP获取的区域编码
     */
    @Column(name = "area_code_by_ip", length = 64)
    private String areaCodeByIp;


}
