package com.kantboot.util.timer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定时器监听注解
 * 用于标记方法为定时器监听方法，并指定定时器配置
 * 
 * @author 方某方
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TimerOn {

    /**
     * 事件编码
     * Event code
     *
     * @return 事件编码
     */
    String code();

    /**
     * 事件名称
     * Event name
     *
     * @return 事件名称
     */
    String name() default "";

    /**
     * 事件描述
     * Event description
     *
     * @return 事件描述
     */
    String description() default "";

    /**
     * 时间
     * 单位为毫秒
     * Time in milliseconds
     *
     * @return 时间
     */
    long time() default 1000L;

    /**
     * 是否锁
     */
    boolean lock() default true;

    /**
     * 锁的最大等待时间
     * 单位为毫秒
     * 0表示不限制
     *
     * @return 锁的最大等待时间
     */
    long lockMaxWaitTime() default 0L;

    /**
     * 锁走完后，最低继续时间
     * 单位为毫秒
     * 0表示不限制
     * 如果锁的时间小于这个时间，则会继续等待
     *
     * @return 锁走完后，最低继续时间
     */
    long lockMinContinueTime() default 0L;


}
