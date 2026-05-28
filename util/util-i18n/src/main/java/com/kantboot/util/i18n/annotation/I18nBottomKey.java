package com.kantboot.util.i18n.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface I18nBottomKey {

    /**
     * key
     */
    String key() default "";

    /**
     * key的名称
     */
    String name() default "";

    /**
     * key的描述
     */
    String description() default "";

    /**
     * 语言编码
     */
    String languageCode() default "zh_CN";


}
