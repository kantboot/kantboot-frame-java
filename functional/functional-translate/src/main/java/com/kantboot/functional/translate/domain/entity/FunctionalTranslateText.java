package com.kantboot.functional.translate.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

@Table(name="functional_translate_text")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class FunctionalTranslateText implements Serializable {

    /**
     * 主键
     * Primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 创建时间
     * Create time
     */
    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     * Modified time
     */
    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;

    /**
     * 源语言编码
     * Source language code
     */
    @Column(name = "source_language_code", length = 64)
    private String sourceLanguageCode;

    /**
     * 目标语言编码
     * Target language code
     */
    @Column(name = "target_language_code", length = 64)
    private String targetLanguageCode;

    /**
     * 源文本
     * Source text
     */
    @Column(name = "source_text", length = 65535)
    private String sourceText;

    /**
     * 目标文本
     * Target text
     */
    @Column(name = "target_text", length = 65535)
    private String targetText;

}
