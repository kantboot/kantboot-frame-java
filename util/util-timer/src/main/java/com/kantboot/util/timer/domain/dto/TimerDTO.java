package com.kantboot.util.timer.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;

/**
 * 定时器数据传输对象
 * 用于传输定时器相关信息
 *
 * @author 方某方
 */
@Data
@Accessors(chain = true)
public class TimerDTO {

    /**
     * 定时器编码
     */
    private String code;

    /**
     * 定时器名称
     */
    private String name;

    /**
     * 定时器描述
     */
    private String description;

    /**
     * 定时器方法
     */
    private Method method;

    /**
     * 时间间隔
     * 单位为毫秒
     */
    private long time;

    /**
     * 是否需要锁
     */
    private boolean lock;

    /**
     * 锁的最大等待时间
     * 单位为毫秒
     */
    private long lockMaxWaitTime;

    /**
     * 锁走完后，最低继续时间
     * 单位为毫秒
     */
    private long lockMinContinueTime;

    /**
     * 定时器状态
     * true: 运行中
     * false: 已停止
     */
    private boolean running;

    /**
     * 下次执行时间
     */
    private long nextExecuteTime;

    /**
     * 执行次数
     */
    private long executeCount;

    /**
     * 最后执行时间
     */
    private long lastExecuteTime;

}
