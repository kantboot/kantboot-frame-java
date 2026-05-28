package com.kantboot.util.scan.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface KantbootScan {

    /**
     * 需要扫描的包路径
     * @return 包路径数组
     */
    String[] value() default {};

}
