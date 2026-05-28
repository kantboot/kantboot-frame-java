package com.kantboot.fp.carousel.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseI18nEntity;
import com.kantboot.util.jpa.consts.IdGenerationTypeConsts;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

/**
 * 轮播图类型
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "fp_carousel_type")
@DynamicUpdate
@DynamicInsert
public class FpCarouselType
        extends BaseI18nEntity
        implements Serializable {

    /**
     * 编码
     */
    @Column(name = "code",unique = true,length = 32)
    private String code;

    /**
     * 名称
     */
    @Column(name = "name",length = 32)
    private String name;

}
