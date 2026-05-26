package com.kantboot.util.event.domain.dto;

import com.kantboot.util.log.Logger;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class EventOnStartDTO {

    /**
     * 事件编码
     */
    private String code;

    /**
     * 事件数据
     */
    private List<Object> data;

    /**
     * 事件唯一标识
     */
    private String uuid;

    /**
     * 时间
     */
    private Date gmtOnStart;

    /**
     * 日志
     */
    private Logger logger;

    /**
     * 执行方法加参数
     */
    private String methodWithParams;

}
