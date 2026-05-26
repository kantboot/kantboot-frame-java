package com.kantboot.util.event.domain.dto;

import com.kantboot.util.log.domain.LoggerItem;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class EventOnInProgressDTO implements Serializable {

    /**
     * 事件编码
     */
    private String code;

    /**
     * 事件数据
     */
    private Object data;

    /**
     * 事件唯一标识
     */
    private String uuid;

    /**
     * 时间
     */
    private Date gmtOn;

    /**
     * 日志
     */
    private LoggerItem loggerItem;

}
