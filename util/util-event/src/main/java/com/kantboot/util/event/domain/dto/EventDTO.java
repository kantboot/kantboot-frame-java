package com.kantboot.util.event.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.util.List;

@Data
@Accessors(chain = true)
public class EventDTO {

    /**
     * 事件编码
     */
    private String code;

    /**
     * 事件名称
     */
    private String name;

    /**
     * 事件描述
     */
    private String description;

    /**
     * 事件方法
     */
    private Method method;

    /**
     * 方法加参数
     */
    private String methodWithParams;

    /**
     * 事件参数
     */
    private List<ParamInEventDTO> params;


}
