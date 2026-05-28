package com.kantboot.functional.icon.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class IconVisitDTO
    implements Serializable {

    /**
     * 编码
     */
    private String code;

    /**
     * 颜色
     */
    private String color;

}
