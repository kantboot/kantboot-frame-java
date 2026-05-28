package com.kantboot.util.i18n.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class I18nEntity implements Serializable {

    /**
     * 国际化内容
     */
    private String content;

    /**
     * 国际化语言
     */
    private String languageCode;

    /**
     * 字段名称
     */
    private String attr;

}
