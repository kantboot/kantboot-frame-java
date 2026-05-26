package com.kantboot.util.log.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoggerItem implements Serializable {

    /**
     * 日志级别
     * DEBUG, INFO, WARN, ERROR
     */
    private String level = "INFO";

    /**
     * 日志内容
     */
    private String message;

    /**
     * 日志模板内容
     */
    private String messageTemplate;

    /**
     * 参数
     */
    private Object[] params;

    /**
     * 异常信息
     */
    private String threadName;

    /**
     * 异常信息
     */
    private String loggerName;

    /**
     * 异常信息
     */
    private String className;

    /**
     * 异常信息
     */
    private String methodName;

    /**
     * 异常信息
     */
    private String fileName;

    /**
     * 行号
     */
    private int lineNumber;

    /**
     * 时间戳
     */
    private long timestamp;

    /**
     * 纳秒时间戳
     */
    private long nanoTime = System.nanoTime();

}
