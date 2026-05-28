package com.kantboot.util.event.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO
 * 事件参数注解
 * 用于标记方法的参数为事件参数
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
public @interface EventParam {

    /**
     * 事件参数名称
     * @return 名称
     */
    String name() default "";

    /**
     * 事件参数描述
     * @return 描述
     */
    String description() default "";

}
