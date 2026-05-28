package com.kantboot.util.event.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ParamInEventDTO {

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 参数的class类型
     */
    private String type;

    /**
     * 参数所在的方法参数中的index
     */
    private int indexInMethod;


}
