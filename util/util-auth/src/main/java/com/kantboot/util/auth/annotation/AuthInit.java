package com.kantboot.util.auth.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AuthInit {

    /**
     * 名称
     */
    String name() default "";

    /**
     * 描述
     */
    String description() default "";

    /**
     * 源语言编码
     */
    String sourceLanguageCode() default "zh_CN";

    /**
     * 无需登录
     */
    boolean noNeedLogin() default false;

    /**
     * 全员放行（需要登录）
     */
    boolean allPass() default false;

    /**
     * 权限编码
     */
    String[] permissionCodes() default "";

}
