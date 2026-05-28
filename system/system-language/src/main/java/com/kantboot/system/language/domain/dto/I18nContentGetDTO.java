package com.kantboot.system.language.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class I18nContentGetDTO implements Serializable {

    /**
     * 语言编码
     */
    private String languageCode;

    /**
     * 顶部key
     */
    private String topKey;

    /**
     * 中间key
     */
    private String centerKey;

    /**
     * 中间key数组
     */
    private List<String> centerKeys;

    /**
     * 底部key
     */
    private String bottomKey;

}
