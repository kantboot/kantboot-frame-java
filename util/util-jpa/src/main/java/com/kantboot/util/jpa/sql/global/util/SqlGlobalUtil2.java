package com.kantboot.util.jpa.sql.global.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.kantboot.util.i18n.util.I18nUtil;
import com.kantboot.util.jpa.param.PageParam;
import com.kantboot.util.jpa.result.PageResult;
import com.kantboot.util.jpa.sql.global.consts.ConditionOperatorCodeConsts;
import com.kantboot.util.jpa.sql.global.entity.ConditionEntity;
import com.kantboot.util.jpa.sql.global.entity.ConditionGlobeEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
public class SqlGlobalUtil2<T> {

    /**
     * JPQL 命名参数（:xxx）里不能出现 '.' 以及各种符号，这里统一“净化”为 [A-Za-z0-9_]
     */
    private static String safeParamKey(String raw) {
        return raw.replaceAll("[^A-Za-z0-9_]", "_");
    }

    private static boolean isNumberType(Class<?> fieldType) {
        return fieldType == BigDecimal.class
                || fieldType == Integer.class
                || fieldType == Long.class
                || fieldType == Short.class
                || fieldType == Byte.class
                || fieldType == Double.class
                || fieldType == Float.class;
    }

    /**
     * 根据T的字段名称获取字段的类型（支持 a.b.c 的路径）
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<?> getFieldType(Class<T> tClass, String fieldName) {
        String[] split = fieldName.split("\\.");
        for (String s : split) {
            try {
                Field declaredField = tClass.getDeclaredField(s);
                tClass = (Class<T>) declaredField.getType();
            } catch (NoSuchFieldException e) {
                try {
                    Field field = tClass.getSuperclass().getDeclaredField(s);
                    tClass = (Class<T>) field.getType();
                } catch (NoSuchFieldException ex) {
                    try {
                        Field field = tClass.getSuperclass().getSuperclass().getDeclaredField(s);
                        tClass = (Class<T>) field.getType();
                    } catch (NoSuchFieldException exc) {
                        throw new RuntimeException(exc);
                    }
                }
            }
        }
        return tClass;
    }

    /**
     * 把 ConditionEntity 转成“参数 key”（不带冒号），并写入 parameterMap
     */
    private static String putParam(Map<String, Map<String, Object>> parameterMap,
                                   String fieldPath,
                                   String logic,
                                   String operatorCode,
                                   Object value) {
        String rawKey = fieldPath + "_$_" + logic + "_$_" + operatorCode;
        String paramKey = safeParamKey(rawKey);
        parameterMap.put(paramKey, Map.of(
                "value", value,
                "field", fieldPath,
                "operatorCode", operatorCode
        ));
        return paramKey;
    }

    /**
     * BETWEEN 的 start/end 参数 key
     */
    private static String betweenKey(String fieldPath, String which, String logic, String operatorCode) {
        // which: "start" / "end"
        String raw = fieldPath + "_" + which + "_$_" + logic + "_$_" + operatorCode;
        return safeParamKey(raw);
    }

    /**
     * 把 value 按字段类型转换（String->upper, 数字/日期等解析）
     */
    private static Object convertValueByType(Object value, Class<?> fieldType) {
        if (value == null) return null;

        if (fieldType == String.class) {
            return (value + "").toUpperCase();
        } else if (fieldType == Integer.class) {
            return Integer.parseInt(value + "");
        } else if (fieldType == Long.class) {
            return Long.parseLong(value + "");
        } else if (fieldType == Short.class) {
            return Short.parseShort(value + "");
        } else if (fieldType == Byte.class) {
            return Byte.parseByte(value + "");
        } else if (fieldType == Double.class) {
            return Double.parseDouble(value + "");
        } else if (fieldType == Float.class) {
            return Float.parseFloat(value + "");
        } else if (fieldType == Boolean.class) {
            return Boolean.parseBoolean(value + "");
        } else if (fieldType == Date.class) {
            // 假设 value 是时间戳
            return new Date(Long.parseLong(value + ""));
        } else if (fieldType == BigDecimal.class) {
            return new BigDecimal(value + "");
        }
        return value;
    }

    /**
     * 获取 Query（非分页）
     */
    public Query buildQuery(EntityManager entityManager,
                            Class<?> entityClass,
                            ConditionGlobeEntity entity) {

        List<ConditionEntity> and = entity.getAnd();
        List<ConditionEntity> or = entity.getOr(); // 你目前没实现 OR，这里保持不动
        List<String> notNull = entity.getNotNull();
        List<String> isNull = entity.getIsNull();

        String orderBy = entity.getOrderBy(); // ASC/DESC
        String sort = entity.getSort();       // 字段名

        if (and == null) and = new ArrayList<>();
        if (or == null) or = new ArrayList<>();
        if (notNull == null) notNull = new ArrayList<>();
        if (isNull == null) isNull = new ArrayList<>();

        String i18nTopKey = I18nUtil.getI18nTopKey(entityClass);
        String i18nQueryFiled = I18nUtil.getFieldFromI18nQuery(entityClass);
        boolean isI18nQuery = StrUtil.isNotEmpty(i18nTopKey);

        String entityName = entityClass.getSimpleName();
        StringBuilder jpql = new StringBuilder("SELECT t FROM ").append(entityName).append(" t");
        Map<String, Map<String, Object>> parameterMap = new HashMap<>();

        jpql.append(" WHERE 1=1 ");

        for (ConditionEntity conditionEntity : and) {
            String fieldPath = conditionEntity.getField();
            String operatorCode = conditionEntity.getOperatorCode();

            // 统一用安全参数 key（不带冒号）
            String paramKey = putParam(parameterMap, fieldPath, "and", operatorCode, conditionEntity.getValue());
            String parameterName = ":" + paramKey;

            if (ConditionOperatorCodeConsts.EQ.equals(operatorCode)) {
                Class<?> fieldType = getFieldType(entityClass, fieldPath);

                String sqlIn = "UPPER(t." + fieldPath + ") = UPPER(" + parameterName + ")";
                if (isNumberType(fieldType) || fieldType == Boolean.class) {
                    sqlIn = "t." + fieldPath + " = " + parameterName;
                }

                if (isI18nQuery) {
                    boolean hasI18nBottomKey = I18nUtil.hasI18nBottomKey(entityClass, fieldPath);
                    if (hasI18nBottomKey) {
                        sqlIn = sqlIn + " OR t." + i18nQueryFiled
                                + " LIKE CONCAT('%<kantbootI18n.attr." + fieldPath + ">', UPPER(" + parameterName + "), '</kantbootI18n.attr." + fieldPath + ">%')";
                    }
                }
                jpql.append(" AND (").append(sqlIn).append(") ");
            }

            if (ConditionOperatorCodeConsts.VAGUE.equals(operatorCode)) {
                String sqlIn = "UPPER(t." + fieldPath + ") LIKE CONCAT('%', UPPER(" + parameterName + "), '%')";
                if (isI18nQuery) {
                    boolean hasI18nBottomKey = I18nUtil.hasI18nBottomKey(entityClass, fieldPath);
                    if (hasI18nBottomKey) {
                        sqlIn = sqlIn + " OR t." + i18nQueryFiled
                                + " LIKE CONCAT('%<kantbootI18n.attr." + fieldPath + ">%', UPPER(" + parameterName + "), '%</kantbootI18n.attr." + fieldPath + ">%')";
                    }
                }
                jpql.append(" AND (").append(sqlIn).append(") ");
            }

            if (ConditionOperatorCodeConsts.LIKE.equals(operatorCode)) {
                String sqlIn = "UPPER(t." + fieldPath + ") LIKE CONCAT('', UPPER(" + parameterName + "), '')";
                if (isI18nQuery) {
                    boolean hasI18nBottomKey = I18nUtil.hasI18nBottomKey(entityClass, fieldPath);
                    if (hasI18nBottomKey) {
                        sqlIn = sqlIn + " OR t." + i18nQueryFiled
                                + " LIKE CONCAT('%<kantbootI18n.attr." + fieldPath + ">', UPPER(" + parameterName + "), '</kantbootI18n.attr." + fieldPath + ">%')";
                    }
                }
                jpql.append(" AND (").append(sqlIn).append(") ");
            }

            if (ConditionOperatorCodeConsts.GT.equals(operatorCode)) {
                jpql.append(" AND t.").append(fieldPath).append(" > ").append(parameterName).append(" ");
            }
            if (ConditionOperatorCodeConsts.LT.equals(operatorCode)) {
                jpql.append(" AND t.").append(fieldPath).append(" < ").append(parameterName).append(" ");
            }
            if (ConditionOperatorCodeConsts.GE.equals(operatorCode)) {
                jpql.append(" AND t.").append(fieldPath).append(" >= ").append(parameterName).append(" ");
            }
            if (ConditionOperatorCodeConsts.LE.equals(operatorCode)) {
                jpql.append(" AND t.").append(fieldPath).append(" <= ").append(parameterName).append(" ");
            }

            if (ConditionOperatorCodeConsts.BETWEEN.equals(operatorCode)) {
                // BETWEEN 的 value 是数组/列表： [start, end]
                String jsonString = JSON.toJSONString(conditionEntity.getValue());
                Class<?> fieldType = getFieldType(entityClass, fieldPath);

                List<?> list = List.of();
                if (fieldType == String.class) list = JSONArray.parseArray(jsonString, String.class);
                else if (fieldType == Integer.class) list = JSONArray.parseArray(jsonString, Integer.class);
                else if (fieldType == Long.class) list = JSONArray.parseArray(jsonString, Long.class);
                else if (fieldType == Short.class) list = JSONArray.parseArray(jsonString, Short.class);
                else if (fieldType == Byte.class) list = JSONArray.parseArray(jsonString, Byte.class);
                else if (fieldType == Double.class) list = JSONArray.parseArray(jsonString, Double.class);
                else if (fieldType == Float.class) list = JSONArray.parseArray(jsonString, Float.class);
                else if (fieldType == Boolean.class) list = JSONArray.parseArray(jsonString, Boolean.class);
                else if (fieldType == Date.class) list = JSONArray.parseArray(jsonString, Date.class);
                else if (fieldType == BigDecimal.class) list = JSONArray.parseArray(jsonString, BigDecimal.class);

                // 移除旧的 BETWEEN 单参数
                parameterMap.remove(paramKey);

                String startKey = betweenKey(fieldPath, "start", "and", operatorCode);
                String endKey = betweenKey(fieldPath, "end", "and", operatorCode);

                Object startVal = (list.size() > 0) ? list.get(0) : null;
                Object endVal = (list.size() > 1) ? list.get(1) : null;

                if (startVal != null) {
                    parameterMap.put(startKey, Map.of("value", startVal, "field", fieldPath, "operatorCode", operatorCode));
                }
                if (endVal != null) {
                    parameterMap.put(endKey, Map.of("value", endVal, "field", fieldPath, "operatorCode", operatorCode));
                }

                if (startVal != null && endVal != null) {
                    jpql.append(" AND (t.").append(fieldPath).append(" >= :").append(startKey)
                            .append(" AND t.").append(fieldPath).append(" <= :").append(endKey).append(") ");
                } else if (startVal != null) {
                    jpql.append(" AND (t.").append(fieldPath).append(" >= :").append(startKey).append(") ");
                } else if (endVal != null) {
                    jpql.append(" AND (t.").append(fieldPath).append(" <= :").append(endKey).append(") ");
                }
            }
        }

        for (String field : notNull) {
            jpql.append(" AND t.").append(field).append(" IS NOT NULL ");
        }
        for (String field : isNull) {
            jpql.append(" AND t.").append(field).append(" IS NULL ");
        }

        // ✅ 修正排序拼接：ORDER BY t.{sort} {orderBy}
        if (StrUtil.isNotEmpty(sort)) {
            jpql.append(" ORDER BY t.").append(sort);
            if (StrUtil.isNotEmpty(orderBy)) {
                jpql.append(" ").append(orderBy);
            }
        }

        Query query = entityManager.createQuery(jpql.toString());

        // setParameter（key 不带冒号，且已安全化）
        for (Map.Entry<String, Map<String, Object>> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String fieldPath = entry.getValue().get("field").toString();
            String operatorCode = entry.getValue().get("operatorCode").toString();
            Object value = entry.getValue().get("value");

            // BETWEEN 的 start/end 也会走到这儿；它们的 fieldPath 仍然是原字段
            if (!ConditionOperatorCodeConsts.BETWEEN.equals(operatorCode)) {
                Class<?> fieldType = getFieldType(entityClass, fieldPath);
                value = convertValueByType(value, fieldType);
            } else {
                // BETWEEN 的 list 里如果是 String，也给你保持原逻辑：同样 upper
                Class<?> fieldType = getFieldType(entityClass, fieldPath);
                value = convertValueByType(value, fieldType);
            }

            query.setParameter(key, value);
        }

        return query;
    }

    public PageResult buildPageQuery(EntityManager entityManager,
                                     Class<?> entityClass,
                                     PageParam<ConditionGlobeEntity> pageParam) {

        ConditionGlobeEntity entity = pageParam.getData();

        List<ConditionEntity> and = entity.getAnd();
        List<ConditionEntity> or = entity.getOr(); // 你目前没实现 OR，这里保持不动
        List<String> notNull = entity.getNotNull();
        List<String> isNull = entity.getIsNull();

        String orderBy = entity.getOrderBy(); // ASC/DESC
        String sort = entity.getSort();       // 字段名

        if ("null".equals(orderBy) || StrUtil.isEmpty(orderBy)) {
            orderBy = pageParam.getOrderBy();
        }
        if ("null".equals(sort) || StrUtil.isEmpty(sort)) {
            sort = pageParam.getSort();
        }
        if ("null".equals(orderBy)) orderBy = null;
        if ("null".equals(sort)) sort = null;

        if (and == null) and = new ArrayList<>();
        if (or == null) or = new ArrayList<>();
        if (notNull == null) notNull = new ArrayList<>();
        if (isNull == null) isNull = new ArrayList<>();

        String i18nTopKey = I18nUtil.getI18nTopKey(entityClass);
        String i18nQueryFiled = I18nUtil.getFieldFromI18nQuery(entityClass);
        boolean isI18nQuery = StrUtil.isNotEmpty(i18nTopKey);

        String entityName = entityClass.getSimpleName();
        StringBuilder jpql = new StringBuilder("FROM ").append(entityName).append(" t");

        Map<String, Map<String, Object>> parameterMap = new HashMap<>();

        jpql.append(" WHERE 1=1 ");

        for (ConditionEntity conditionEntity : and) {
            String fieldPath = conditionEntity.getField();
            String operatorCode = conditionEntity.getOperatorCode();

            String paramKey = putParam(parameterMap, fieldPath, "and", operatorCode, conditionEntity.getValue());
            String parameterName = ":" + paramKey;

            if (ConditionOperatorCodeConsts.EQ.equals(operatorCode)) {
                Class<?> fieldType = getFieldType(entityClass, fieldPath);

                String sqlIn = "UPPER(t." + fieldPath + ") = UPPER(" + parameterName + ")";
                if (isNumberType(fieldType) || fieldType == Boolean.class) {
                    sqlIn = "t." + fieldPath + " = " + parameterName;
                }

                if (isI18nQuery) {
                    boolean hasI18nBottomKey = I18nUtil.hasI18nBottomKey(entityClass, fieldPath);
                    if (hasI18nBottomKey) {
                        sqlIn = sqlIn + " OR t." + i18nQueryFiled
                                + " LIKE CONCAT('%<kantbootI18n.attr." + fieldPath + ">', UPPER(" + parameterName + "), '</kantbootI18n.attr." + fieldPath + ">%')";
                    }
                }
                jpql.append(" AND (").append(sqlIn).append(") ");
            }

            if (ConditionOperatorCodeConsts.VAGUE.equals(operatorCode)) {
                String sqlIn = "UPPER(t." + fieldPath + ") LIKE CONCAT('%', UPPER(" + parameterName + "), '%')";
                if (isI18nQuery) {
                    boolean hasI18nBottomKey = I18nUtil.hasI18nBottomKey(entityClass, fieldPath);
                    if (hasI18nBottomKey) {
                        sqlIn = sqlIn + " OR t." + i18nQueryFiled
                                + " LIKE CONCAT('%<kantbootI18n.attr." + fieldPath + ">%', UPPER(" + parameterName + "), '%</kantbootI18n.attr." + fieldPath + ">%')";
                    }
                }
                jpql.append(" AND (").append(sqlIn).append(") ");
            }

            if (ConditionOperatorCodeConsts.LIKE.equals(operatorCode)) {
                String sqlIn = "UPPER(t." + fieldPath + ") LIKE CONCAT('', UPPER(" + parameterName + "), '')";
                if (isI18nQuery) {
                    boolean hasI18nBottomKey = I18nUtil.hasI18nBottomKey(entityClass, fieldPath);
                    if (hasI18nBottomKey) {
                        sqlIn = sqlIn + " OR t." + i18nQueryFiled
                                + " LIKE CONCAT('%<kantbootI18n.attr." + fieldPath + ">', UPPER(" + parameterName + "), '</kantbootI18n.attr." + fieldPath + ">%')";
                    }
                }
                jpql.append(" AND (").append(sqlIn).append(") ");
            }

            if (ConditionOperatorCodeConsts.GT.equals(operatorCode)) {
                jpql.append(" AND t.").append(fieldPath).append(" > ").append(parameterName).append(" ");
            }
            if (ConditionOperatorCodeConsts.LT.equals(operatorCode)) {
                jpql.append(" AND t.").append(fieldPath).append(" < ").append(parameterName).append(" ");
            }
            if (ConditionOperatorCodeConsts.GE.equals(operatorCode)) {
                jpql.append(" AND t.").append(fieldPath).append(" >= ").append(parameterName).append(" ");
            }
            if (ConditionOperatorCodeConsts.LE.equals(operatorCode)) {
                jpql.append(" AND t.").append(fieldPath).append(" <= ").append(parameterName).append(" ");
            }

            if (ConditionOperatorCodeConsts.BETWEEN.equals(operatorCode)) {
                String jsonString = JSON.toJSONString(conditionEntity.getValue());
                Class<?> fieldType = getFieldType(entityClass, fieldPath);

                List<?> list = List.of();
                if (fieldType == String.class) list = JSONArray.parseArray(jsonString, String.class);
                else if (fieldType == Integer.class) list = JSONArray.parseArray(jsonString, Integer.class);
                else if (fieldType == Long.class) list = JSONArray.parseArray(jsonString, Long.class);
                else if (fieldType == Short.class) list = JSONArray.parseArray(jsonString, Short.class);
                else if (fieldType == Byte.class) list = JSONArray.parseArray(jsonString, Byte.class);
                else if (fieldType == Double.class) list = JSONArray.parseArray(jsonString, Double.class);
                else if (fieldType == Float.class) list = JSONArray.parseArray(jsonString, Float.class);
                else if (fieldType == Boolean.class) list = JSONArray.parseArray(jsonString, Boolean.class);
                else if (fieldType == Date.class) list = JSONArray.parseArray(jsonString, Date.class);
                else if (fieldType == BigDecimal.class) list = JSONArray.parseArray(jsonString, BigDecimal.class);

                parameterMap.remove(paramKey);

                String startKey = betweenKey(fieldPath, "start", "and", operatorCode);
                String endKey = betweenKey(fieldPath, "end", "and", operatorCode);

                Object startVal = (list.size() > 0) ? list.get(0) : null;
                Object endVal = (list.size() > 1) ? list.get(1) : null;

                if (startVal != null) {
                    parameterMap.put(startKey, Map.of("value", startVal, "field", fieldPath, "operatorCode", operatorCode));
                }
                if (endVal != null) {
                    parameterMap.put(endKey, Map.of("value", endVal, "field", fieldPath, "operatorCode", operatorCode));
                }

                if (startVal != null && endVal != null) {
                    jpql.append(" AND (t.").append(fieldPath).append(" >= :").append(startKey)
                            .append(" AND t.").append(fieldPath).append(" <= :").append(endKey).append(") ");
                } else if (startVal != null) {
                    jpql.append(" AND (t.").append(fieldPath).append(" >= :").append(startKey).append(") ");
                } else if (endVal != null) {
                    jpql.append(" AND (t.").append(fieldPath).append(" <= :").append(endKey).append(") ");
                }
            }
        }

        for (String field : notNull) {
            jpql.append(" AND t.").append(field).append(" IS NOT NULL ");
        }
        for (String field : isNull) {
            jpql.append(" AND t.").append(field).append(" IS NULL ");
        }

        // ✅ 修正排序拼接：ORDER BY t.{sort} {orderBy}
        if (!StrUtil.isEmpty(sort)) {
            jpql.append(" ORDER BY t.").append(sort);
            if (!StrUtil.isEmpty(orderBy)) {
                jpql.append(" ").append(orderBy);
            }
        }

        Query query = entityManager.createQuery("SELECT t " + jpql);

        // setParameter（key 不带冒号，且已安全化）
        for (Map.Entry<String, Map<String, Object>> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String fieldPath = entry.getValue().get("field").toString();
            String operatorCode = entry.getValue().get("operatorCode").toString();
            Object value = entry.getValue().get("value");

            Class<?> fieldType = getFieldType(entityClass, fieldPath);
            value = convertValueByType(value, fieldType);

            query.setParameter(key, value);
        }

        // 分页
        int pageNumber = pageParam.getPageable().getPageNumber();
        int pageSize = pageParam.getPageable().getPageSize();
        int firstResult = pageNumber * pageSize;

        query.setFirstResult(firstResult);
        query.setMaxResults(pageSize);

        List content = query.getResultList();

        // count
        String countJpql = "SELECT COUNT(t) " + jpql;
        int orderByIndex = countJpql.toUpperCase().indexOf(" ORDER BY ");
        if (orderByIndex != -1) {
            countJpql = countJpql.substring(0, orderByIndex);
        }

        Query countQuery = entityManager.createQuery(countJpql);

        for (Map.Entry<String, Map<String, Object>> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String fieldPath = entry.getValue().get("field").toString();
            Object value = entry.getValue().get("value");

            Class<?> fieldType = getFieldType(entityClass, fieldPath);
            value = convertValueByType(value, fieldType);

            countQuery.setParameter(key, value);
        }

        long totalElements = (long) countQuery.getSingleResult();
        int totalPage = (int) Math.ceil((double) totalElements / pageSize);

        return new PageResult(
                totalElements,
                totalPage,
                content,
                pageNumber + 1,
                pageSize
        );
    }
}
