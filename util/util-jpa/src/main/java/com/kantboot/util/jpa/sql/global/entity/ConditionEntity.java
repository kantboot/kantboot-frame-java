package com.kantboot.util.jpa.sql.global.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 关于运算符的实体类
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class ConditionEntity implements Serializable {

    /**
     * 操作值字段
     */
    private String field;

    /**
     * 操作符
     * 等于 eq
     * like查询 like
     * 模糊查询 vague
     * 大于 gt
     * 小于 lt
     * 大于等于 ge
     * 小于等于 le
     * 开区间查询 openInterval
     * 闭区间查询 closeInterval
     */
    private String operatorCode;


    /**
     * 操作值
     */
    private Object value;


    /**
     * 用区间查询时的开始值
     * 开始区间的值
     */
    private Object startValue;

    /**
     * 用区间查询时的结束值
     * 结束区间的值
     */
    private Object endValue;

}
