package com.kantboot.util.setting.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SettingGroup {

    String code();

    String name() default "";

    String description() default "";

    String sourceLanguageCode() default "zh_CN";

}
