package com.kantboot.util.jpa.sql.global.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 关于运算符条件的实体类
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class ConditionGlobeEntity implements Serializable {

    private List<ConditionEntity> and;

    private List<ConditionEntity> or;

    private List<String> notNull;

    private List<String> isNull;

    private String orderBy;

    private String sort;

}
