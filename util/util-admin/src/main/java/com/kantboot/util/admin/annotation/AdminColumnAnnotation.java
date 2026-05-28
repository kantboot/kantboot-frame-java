package com.kantboot.util.admin.annotation;

public @interface AdminColumnAnnotation {

    /**
     * 列名
     */
    String label() default "";

    /**
     * 字段
     */
    String field() default "";

    /**
     * 列宽
     */
    String width() default "";

    /**
     * 列类型
     */
    String type() default "text";

    /**
     * 是否是ID
     */
    boolean isId() default false;

    /**
     * 是否可搜索
     */
    boolean isSearch() default true;

    /**
     * 是否可编辑
     */
    boolean isEdit() default true;

    /**
     * 是否隐藏
     */
    boolean isHide() default false;

    /**
     * 是否国际化中心key
     */
    boolean isI18nCenterKey() default false;

    /**
     * 是否国际化底部key
     */
    boolean isI18nBottomKey() default false;

    /**
     * 是否可排序
     */
    boolean isSort() default false;

    /**
     * 排序方式
     * 在前端表格中，isSort为true时有效
     */
    String orderBy() default "desc";

    AdminColumnSettingAnnotation setting() default @AdminColumnSettingAnnotation();

}
