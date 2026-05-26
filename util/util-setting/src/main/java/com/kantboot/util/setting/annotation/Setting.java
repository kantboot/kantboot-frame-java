package com.kantboot.util.setting.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Setting {

    String code();

    String name() default "";

    String description() default "";

    /**
     * 默认值
     * 一般情况下，为空
     */
    String defaultValue() default "";

    String sourceLanguageCode() default "zh_CN";


}
