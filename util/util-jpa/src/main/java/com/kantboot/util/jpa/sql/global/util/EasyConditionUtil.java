package com.kantboot.util.jpa.sql.global.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.kantboot.util.jpa.sql.global.consts.ConditionOperatorCodeConsts;
import com.kantboot.util.jpa.sql.global.entity.ConditionEntity;
import com.kantboot.util.jpa.sql.global.entity.ConditionGlobeEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 简单条件实体类
 */
public class EasyConditionUtil {


    /**
     * 获取对应的条件值
     */
    @SuppressWarnings("unchecked")
    public static ConditionGlobeEntity getConditionGlobeEntity(Map<String, Object> conditionMap) {
        List<String> notNull = new ArrayList<String>();
        List<String> isNull = new ArrayList<String>();
        if(conditionMap.get("notNull")!=null){
            // 判断是否为字符串类型
            if(conditionMap.get("notNull") instanceof String) {
                notNull.add((String) conditionMap.get("notNull"));
            }
            // 判断是否为数组类型
            if(conditionMap.get("notNull") instanceof String[] notNullArray) {
                notNull.addAll(Arrays.asList(notNullArray));
            }
            // 判断是否为集合类型
            if(conditionMap.get("notNull") instanceof List) {
                List<String> notNullList = (List<String>) conditionMap.get("notNull");
                notNull.addAll(notNullList);
            }
        }
        if(conditionMap.get("isNull")!=null){
            // 判断是否为字符串类型
            if(conditionMap.get("isNull") instanceof String) {
                isNull.add((String) conditionMap.get("isNull"));
            }
            // 判断是否为数组类型
            if(conditionMap.get("isNull") instanceof String[] isNullArray) {
                isNull.addAll(Arrays.asList(isNullArray));
            }
            // 判断是否为集合类型
            if(conditionMap.get("isNull") instanceof List) {
                List<String> isNullList = (List<String>) conditionMap.get("isNull");
                isNull.addAll(isNullList);
            }
        }


        List<ConditionEntity> and = new ArrayList<ConditionEntity>();
        List<ConditionEntity> or = new ArrayList<ConditionEntity>();

        // 获取所有key
        List<String> keys = new ArrayList<String>();
        for (String key : conditionMap.keySet()) {
            if("notNull".equals(key) || "isNull".equals(key)){
                continue;
            }
            if(StrUtil.isEmpty(key)){
                continue;
            }
            String[] split = key.split(":");
            if(split.length!=3){
                continue;
            }
            String field = split[0];
            String type = split[1];
            String condition = split[2];
            Object value = conditionMap.get(key);
            if(StrUtil.isEmpty(field)||StrUtil.isEmpty(value+"")||StrUtil.isEmpty(type)||StrUtil.isEmpty(condition)||value==null){
                continue;
            }
            if("and".equals(type)){
                and.add(new ConditionEntity().setValue(value).setField(field).setOperatorCode(condition));
            }
            if("or".equals(type)){
                // 判断是否为数组类型
                if(value instanceof Object[] arr) {
                    if(ConditionOperatorCodeConsts.CLOSE_INTERVAL.equals(condition)||ConditionOperatorCodeConsts.OPEN_INTERVAL.equals(condition)){
                        // 判断是一维数组还是二维数组
                        if (arr.length > 0 && arr[0] instanceof Object[]) {
                            // 二维数组
                            for (Object[] interval : (Object[][]) arr) {
                                or.add(new ConditionEntity().setValue(interval).setField(field).setOperatorCode(condition));
                            }
                        } else {
                            // 一维数组
                            or.add(new ConditionEntity().setValue(arr).setField(field).setOperatorCode(condition));
                        }
                    }else{
                        for (Object o : arr) {
                            or.add(new ConditionEntity().setValue(o).setField(field).setOperatorCode(condition));
                        }
                    }


                }else if(value instanceof List) {
                    if(ConditionOperatorCodeConsts.CLOSE_INTERVAL.equals(condition)||ConditionOperatorCodeConsts.OPEN_INTERVAL.equals(condition)){
                        // 判断是一维集合还是二维集合
                        if (!((List<?>) value).isEmpty() && ((List<?>) value).getFirst() instanceof List) {
                            // 二维集合
                            for (List<?> interval : (List<List<?>>) value) {
                                or.add(new ConditionEntity().setValue(interval.toArray()).setField(field).setOperatorCode(condition));
                            }
                        } else {
                            // 一维集合
                            or.add(new ConditionEntity().setValue(((List<?>) value).toArray()).setField(field).setOperatorCode(condition));
                        }
                    }else{
                        List<Object> list = (List<Object>) value;
                        for (Object o : list) {
                            or.add(new ConditionEntity().setValue(o).setField(field).setOperatorCode(condition));
                        }
                    }

                }else{
                    or.add(new ConditionEntity().setValue(value).setField(field).setOperatorCode(condition));
                }
            }


            keys.add(key);

        }


        // 获取所有的条件
        ConditionGlobeEntity conditionGlobeEntity = new ConditionGlobeEntity();
        conditionGlobeEntity.setNotNull(notNull);
        conditionGlobeEntity.setIsNull(isNull);
        conditionGlobeEntity.setAnd(and);
        conditionGlobeEntity.setOr(or);
        conditionGlobeEntity.setOrderBy(conditionMap.get("orderBy")+"");
        conditionGlobeEntity.setSort(conditionMap.get("sort")+"");


        return conditionGlobeEntity;
    }
}
