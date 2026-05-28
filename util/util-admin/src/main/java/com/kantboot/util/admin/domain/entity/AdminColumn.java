package com.kantboot.util.admin.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdminColumn
        implements Serializable {

    /**
     * 列名
     */
    public String label;

    /**
     * 字段名
     */
    private String field;

    /**
     * 列宽
     */
    private String width;

    /**
     * 列类型
     */
    private String type;

    /**
     * 是否是ID
     */
    private Boolean isId;

    /**
     * 是否可搜索
     */
    private Boolean isSearch;

    /**
     * 是否可编辑
     */
    private Boolean isEdit;

    /**
     * 是否隐藏
     */
    private Boolean isHide;

    /**
     * 是否国际化中心key
     */
    private Boolean isI18nCenterKey;

    /**
     * 是否国际化底部key
     */
    private Boolean isI18nBottomKey;

    /**
     * 是否可排序
     */
    private Boolean isSort;

    /**
     * 排序方式
     */
    private String orderBy;

    /**
     * 设置
     */
    private AdminColumnSetting setting;


}
