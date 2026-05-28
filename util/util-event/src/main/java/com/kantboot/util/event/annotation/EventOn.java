package com.kantboot.util.event.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 全局监听注解
 * 用于标记方法为事件监听方法，并指定事件编码
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventOn {

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
}