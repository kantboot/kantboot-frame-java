package com.kantboot.util.admin.annotation;

public @interface AdminColumnSettingAnnotation {

    /**
     * 在时间类型的列中，是时间显示的类型
     */
    String format() default "";

    /**
     * 操作编码 eq、like、vague、gt、lt、ge、le、openInterval、closeInterval、between
     */
    String operatorCode() default "";

    /**
     * 分组
     * 例如：文件分组的编码
     */
    String groupCode() default "";

}
