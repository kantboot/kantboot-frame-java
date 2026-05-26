package com.kantboot.util.event.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 事件扫描注解
 * 用于指定需要扫描的包路径
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventScan {

    /**
     * 需要扫描的包路径
     *
     * @return 包路径数组
     */
    String[] value() default {};
}