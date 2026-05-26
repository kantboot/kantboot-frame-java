package com.kantboot.util.base.control.domian.entity;

import com.kantboot.util.admin.annotation.AdminColumnAnnotation;
import com.kantboot.util.i18n.annotation.I18nQuery;
import com.kantboot.util.i18n.annotation.I18nSave;
import com.kantboot.util.i18n.annotation.I18nTopKey;
import com.kantboot.util.jpa.consts.IdGenerationTypeConsts;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Accessors(chain = true)
@Getter
@Setter
@MappedSuperclass
public class BaseI18nEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = IdGenerationTypeConsts.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 最后一次修改时间
     */
    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;

    @I18nSave
    @Type(JsonBinaryType.class)
    @Column(name = "i18n_save",columnDefinition = "TEXT")
    private Map<String, Map<String,Object>> i18nSave;

    @I18nQuery
    @Column(name = "i18n_query",columnDefinition = "TEXT")
    private String i18nQuery;

    /**
     * 源语言编码（为了更好的国际化）
     */
    @Column(name = "source_language_code", length = 32)
    private String sourceLanguageCode;

}
