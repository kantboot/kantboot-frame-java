package com.kantboot.util.event.domain.dto;

import com.kantboot.util.log.Logger;
import com.kantboot.util.log.domain.LoggerItem;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class EventOnEndDTO {

    /**
     * 事件编码
     */
    private String code;

    /**
     * 事件唯一标识
     */
    private String uuid;

    /**
     * 开始时间
     */
    private Date gmtOnStart;

    /**
     * 结束时间
     */
    private Date gmtOnEnd;

    /**
     * 事件数据
     */
    private Object data;

    /**
     * 执行时长，单位毫秒
     */
    private Long duration;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 异常信息
     */
    private String exceptionMessage;

    /**
     * 日志信息
     */
    private List<LoggerItem> loggerItems;

    /**
     * 是否异常结束
     */
    private Boolean isExceptionEnd;

    /**
     * 异常信息
     */
    private Exception exception;


}
