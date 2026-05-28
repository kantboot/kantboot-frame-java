package com.kantboot.util.jpa.sql.global.util;

import com.alibaba.fastjson2.JSON;
import com.kantboot.util.jpa.sql.global.consts.ConditionOperatorCodeConsts;
import com.kantboot.util.jpa.sql.global.entity.ConditionEntity;
import com.kantboot.util.jpa.sql.global.entity.ConditionGlobeEntity;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class SqlGlobalUtil<T> {

    /**
     * 根据T的字段名称获取字段的类型
     */
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
                    throw new RuntimeException(ex);
                }
            }
        }
        return tClass;
    }


    /**
     * 获取Specification
     *
     * @param operatorEntities 条件
     * @param root             根
     * @param criteriaBuilder  构造器
     * @return Specification
     */
    public Predicate[] getPredicate(List<ConditionEntity> operatorEntities, Root<T> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        for (ConditionEntity operatorEntity : operatorEntities) {
            // 获取字段的类型
            Class<?> fieldType = getFieldType(root.getJavaType(), operatorEntity.getField());

            // 获取表达式
            Expression<String> expression = getExpression(root, operatorEntity.getField());
            // 判断操作符, 如果是模糊查询
            if (ConditionOperatorCodeConsts.VAGUE.equals(operatorEntity.getOperatorCode())) {
                if (!"".equals(operatorEntity.getValue()) && null != operatorEntity.getValue()) {
                    if(fieldType == String.class){
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(expression), "%" + (operatorEntity.getValue()+"").toLowerCase() + "%"));
                    }else{
                        log.error("模糊查询字段类型错误, 请检查字段类型是否为String");
                        continue;
                    }
                }
            }
            // 判断操作符, 如果是LIKE查询, 则使用like
            if (ConditionOperatorCodeConsts.LIKE.equals(operatorEntity.getOperatorCode())) {
                if (!"".equals(operatorEntity.getValue()) && null != operatorEntity.getValue()) {
                    if(fieldType == String.class){
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(expression), (operatorEntity.getValue()+"").toLowerCase()));
                    }else{
                        log.error("模糊查询字段类型错误, 请检查字段类型是否为String");
                        continue;
                    }
                }
            }
            // 判断操作符, 如果是等于查询, 则使用equal
            if (ConditionOperatorCodeConsts.EQ.equals(operatorEntity.getOperatorCode())) {
                if (!"".equals(operatorEntity.getValue()) && null != operatorEntity.getValue()) {
                    // 检查类型是否为String
                    if(fieldType == String.class) {
                        predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(expression), (operatorEntity.getValue() + "").toLowerCase()));
                    }else{
                        predicates.add(criteriaBuilder.equal(expression, operatorEntity.getValue()));
                    }
                }
            }

            // 判断操作符, 如果是小于查询, 则使用lessThan
            if (ConditionOperatorCodeConsts.LT.equals(operatorEntity.getOperatorCode())) {
                predicates.add(criteriaBuilder.lessThan(expression.as(BigDecimal.class), toBigDecimal(operatorEntity.getValue())));
            }

            // 判断操作符, 如果是大于查询, 则使用greaterThan
            if (ConditionOperatorCodeConsts.GT.equals(operatorEntity.getOperatorCode())) {
                predicates.add(criteriaBuilder.greaterThan(expression.as(BigDecimal.class), toBigDecimal(operatorEntity.getValue())));
            }

            // 判断操作符, 如果是小于等于查询, 则使用lessThanOrEqualTo
            if (ConditionOperatorCodeConsts.LE.equals(operatorEntity.getOperatorCode())) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(expression.as(BigDecimal.class), toBigDecimal(operatorEntity.getValue())));
            }

            // 判断操作符, 如果是大于等于查询, 则使用greaterThanOrEqualTo
            if (ConditionOperatorCodeConsts.GE.equals(operatorEntity.getOperatorCode())) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression.as(BigDecimal.class), toBigDecimal(operatorEntity.getValue())));
            }

            // 判断操作符，是否是开区间查询，如果是则使用between，且两个值都不包含
            if (ConditionOperatorCodeConsts.OPEN_INTERVAL.equals(operatorEntity.getOperatorCode())) {
                List<BigDecimal> valueF =
                        JSON.parseArray(JSON.toJSONString(operatorEntity.getValue()), BigDecimal.class);

                List<Object> value = new ArrayList<>();
                for (BigDecimal aLong : valueF) {
                    if (null != aLong) {
                        if (fieldType == BigDecimal.class) {
                            value.add(aLong);
                        } else if (fieldType == Date.class) {
                            value.add(new Date(aLong.longValue()));
                        } else if (fieldType == Long.class) {
                            value.add(aLong.longValue());
                        } else if (fieldType == Integer.class) {
                            value.add(aLong.intValue());
                        } else if (fieldType == Double.class) {
                            value.add(aLong.doubleValue());
                        } else if (fieldType == Float.class) {
                            value.add(aLong.floatValue());
                        } else if (fieldType == Short.class) {
                            value.add(aLong.shortValue());
                        } else if (fieldType == Byte.class) {
                            value.add(aLong.byteValue());
                        } else if (fieldType == Character.class) {
                            value.add((char) aLong.intValue());
                        } else if (fieldType == String.class) {
                            value.add(aLong.toString());
                        }
                    }
                }

                if (value.size() == 0) {
                    value.add(null);
                    value.add(null);
                }
                // 如果value只有一个值
                if (value.size() == 1) {
                    value.add(null);
                }

                // 如果是空字符串
                if ("".equals(value.get(0))) {
                    value.set(0, null);
                }
                if ("".equals(value.get(1))) {
                    value.set(1, null);
                }


                // 如果是空字符串
                if ("".equals(value.get(0))) {
                    value.set(0, null);
                }
                if ("".equals(value.get(1))) {
                    value.set(1, null);
                }
                // 如果第一个值为空与第二个值都为空
                if (null == value.get(0) && null == value.get(1)) {
                    // 什么都不做
                } else if (null == value.get(0)) {
                    // 如果第一个值为空，第二个值不为空
                    if (fieldType == BigDecimal.class) {
                        predicates.add(criteriaBuilder.lessThan(expression.as(BigDecimal.class), (BigDecimal) value.get(1)));
                    } else if (fieldType == Date.class) {
                        predicates.add(criteriaBuilder.lessThan(expression.as(Date.class), (Date) value.get(1)));
                    } else if (fieldType == Long.class) {
                        predicates.add(criteriaBuilder.lessThan(expression.as(Long.class), (Long) value.get(1)));
                    } else if (fieldType == Integer.class) {
                        predicates.add(criteriaBuilder.lessThan(expression.as(Integer.class), (Integer) value.get(1)));
                    } else if (fieldType == Double.class) {
                        predicates.add(criteriaBuilder.lessThan(expression.as(Double.class), (Double) value.get(1)));
                    } else if (fieldType == Float.class) {
                        predicates.add(criteriaBuilder.lessThan(expression.as(Float.class), (Float) value.get(1)));
                    } else if (fieldType == Short.class) {
                        predicates.add(criteriaBuilder.lessThan(expression.as(Short.class), (Short) value.get(1)));
                    } else if (fieldType == Byte.class) {
                        predicates.add(criteriaBuilder.lessThan(expression.as(Byte.class), (Byte) value.get(1)));
                    } else if (fieldType == Character.class) {
                        predicates.add(criteriaBuilder.lessThan(expression.as(Character.class), (Character) value.get(1)));
                    } else if (fieldType == String.class) {
                        predicates.add(criteriaBuilder.lessThan(expression.as(String.class), (String) value.get(1)));
                    }
                } else if (null == value.get(1)) {
                    // 如果第一个值不为空，第二个值为空
                    if (fieldType == BigDecimal.class) {
                        predicates.add(criteriaBuilder.greaterThan(expression.as(BigDecimal.class), (BigDecimal) value.get(0)));
                    } else if (fieldType == Date.class) {
                        predicates.add(criteriaBuilder.greaterThan(expression.as(Date.class), (Date) value.get(0)));
                    } else if (fieldType == Long.class) {
                        predicates.add(criteriaBuilder.greaterThan(expression.as(Long.class), (Long) value.get(0)));
                    } else if (fieldType == Integer.class) {
                        predicates.add(criteriaBuilder.greaterThan(expression.as(Integer.class), (Integer) value.get(0)));
                    } else if (fieldType == Double.class) {
                        predicates.add(criteriaBuilder.greaterThan(expression.as(Double.class), (Double) value.get(0)));
                    } else if (fieldType == Float.class) {
                        predicates.add(criteriaBuilder.greaterThan(expression.as(Float.class), (Float) value.get(0)));
                    } else if (fieldType == Short.class) {
                        predicates.add(criteriaBuilder.greaterThan(expression.as(Short.class), (Short) value.get(0)));
                    } else if (fieldType == Byte.class) {
                        predicates.add(criteriaBuilder.greaterThan(expression.as(Byte.class), (Byte) value.get(0)));
                    } else if (fieldType == Character.class) {
                        predicates.add(criteriaBuilder.greaterThan(expression.as(Character.class), (Character) value.get(0)));
                    } else if (fieldType == String.class) {
                        predicates.add(criteriaBuilder.greaterThan(expression.as(String.class), (String) value.get(0)));
                    }
                } else {
                    // 如果第一个值不为空，第二个值不为空
                    if (fieldType == BigDecimal.class) {
                        predicates.add(criteriaBuilder.between(expression.as(BigDecimal.class), (BigDecimal) value.get(0), (BigDecimal) value.get(1)));
                    } else if (fieldType == Date.class) {
                        predicates.add(criteriaBuilder.between(expression.as(Date.class), (Date) value.get(0), (Date) value.get(1)));
                    } else if (fieldType == Long.class) {
                        predicates.add(criteriaBuilder.between(expression.as(Long.class), (Long) value.get(0), (Long) value.get(1)));
                    } else if (fieldType == Integer.class) {
                        predicates.add(criteriaBuilder.between(expression.as(Integer.class), (Integer) value.get(0), (Integer) value.get(1)));
                    } else if (fieldType == Double.class) {
                        predicates.add(criteriaBuilder.between(expression.as(Double.class), (Double) value.get(0), (Double) value.get(1)));
                    } else if (fieldType == Float.class) {
                        predicates.add(criteriaBuilder.between(expression.as(Float.class), (Float) value.get(0), (Float) value.get(1)));
                    } else if (fieldType == Short.class) {
                        predicates.add(criteriaBuilder.between(expression.as(Short.class), (Short) value.get(0), (Short) value.get(1)));
                    } else if (fieldType == Byte.class) {
                        predicates.add(criteriaBuilder.between(expression.as(Byte.class), (Byte) value.get(0), (Byte) value.get(1)));
                    } else if (fieldType == Character.class) {
                        predicates.add(criteriaBuilder.between(expression.as(Character.class), (Character) value.get(0), (Character) value.get(1)));
                    } else if (fieldType == String.class) {
                        predicates.add(criteriaBuilder.between(expression.as(String.class), (String) value.get(0), (String) value.get(1)));
                    }
                }

            }


            // 判断操作符，是否是闭区间查询，如果是则使用between，且两个值都要包含
            if (ConditionOperatorCodeConsts.CLOSE_INTERVAL.equals(operatorEntity.getOperatorCode())) {
                List<BigDecimal> valueF =
                        JSON.parseArray(JSON.toJSONString(operatorEntity.getValue()), BigDecimal.class);

                List<Object> value = new ArrayList<>();
                for (BigDecimal aLong : valueF) {
                    if (null != aLong) {
                        if (fieldType == BigDecimal.class) {
                            value.add(aLong);
                        } else if (fieldType == Date.class) {
                            value.add(new Date(aLong.longValue()));
                        } else if (fieldType == Long.class) {
                            value.add(aLong.longValue());
                        } else if (fieldType == Integer.class) {
                            value.add(aLong.intValue());
                        } else if (fieldType == Double.class) {
                            value.add(aLong.doubleValue());
                        } else if (fieldType == Float.class) {
                            value.add(aLong.floatValue());
                        } else if (fieldType == Short.class) {
                            value.add(aLong.shortValue());
                        } else if (fieldType == Byte.class) {
                            value.add(aLong.byteValue());
                        } else if (fieldType == Character.class) {
                            value.add((char) aLong.intValue());
                        } else if (fieldType == String.class) {
                            value.add(aLong.toString());
                        }
                    }
                }


                if (value.size() == 0) {
                    value.add(null);
                    value.add(null);
                }
                // 如果value只有一个值
                if (value.size() == 1) {
                    value.add(null);
                }

                // 如果是空字符串
                if ("".equals(value.get(0))) {
                    value.set(0, null);
                }
                if ("".equals(value.get(1))) {
                    value.set(1, null);
                }
                // 如果第一个值为空与第二个值都为空
                if (null == value.get(0) && null == value.get(1)) {
                    // 什么都不做
                } else if (null == value.get(1)) {
                    if (fieldType == BigDecimal.class) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression.as(BigDecimal.class), (BigDecimal) value.get(0)));
                    } else if (fieldType == Date.class) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression.as(Date.class), (Date) value.get(0)));
                    } else if (fieldType == Long.class) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression.as(Long.class), (Long) value.get(0)));
                    } else if (fieldType == Integer.class) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression.as(Integer.class), (Integer) value.get(0)));
                    } else if (fieldType == Double.class) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression.as(Double.class), (Double) value.get(0)));
                    } else if (fieldType == Float.class) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression.as(Float.class), (Float) value.get(0)));
                    } else if (fieldType == Short.class) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression.as(Short.class), (Short) value.get(0)));
                    } else if (fieldType == Byte.class) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression.as(Byte.class), (Byte) value.get(0)));
                    } else if (fieldType == Character.class) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression.as(Character.class), (Character) value.get(0)));
                    } else if (fieldType == String.class) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression.as(String.class), (String) value.get(0)));
                    }


                } else if (null == value.get(0)) {
                    if (fieldType == BigDecimal.class) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(expression.as(BigDecimal.class), (BigDecimal) value.get(1)));
                    } else if (fieldType == Date.class) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(expression.as(Date.class), (Date) value.get(1)));
                    } else if (fieldType == Long.class) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(expression.as(Long.class), (Long) value.get(1)));
                    } else if (fieldType == Integer.class) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(expression.as(Integer.class), (Integer) value.get(1)));
                    } else if (fieldType == Double.class) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(expression.as(Double.class), (Double) value.get(1)));
                    } else if (fieldType == Float.class) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(expression.as(Float.class), (Float) value.get(1)));
                    } else if (fieldType == Short.class) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(expression.as(Short.class), (Short) value.get(1)));
                    } else if (fieldType == Byte.class) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(expression.as(Byte.class), (Byte) value.get(1)));
                    } else if (fieldType == Character.class) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(expression.as(Character.class), (Character) value.get(1)));
                    } else if (fieldType == String.class) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(expression.as(String.class), (String) value.get(1)));
                    }
                } else {
                    // 如果第一个值不为空，第二个值不为空
                    if (fieldType == BigDecimal.class) {
                        predicates.add(criteriaBuilder.between(expression.as(BigDecimal.class), (BigDecimal) value.get(0), (BigDecimal) value.get(1)));
                    } else if (fieldType == Date.class) {
                        predicates.add(criteriaBuilder.between(expression.as(Date.class), (Date) value.get(0), (Date) value.get(1)));
                    } else if (fieldType == Long.class) {
                        predicates.add(criteriaBuilder.between(expression.as(Long.class), (Long) value.get(0), (Long) value.get(1)));
                    } else if (fieldType == Integer.class) {
                        predicates.add(criteriaBuilder.between(expression.as(Integer.class), (Integer) value.get(0), (Integer) value.get(1)));
                    } else if (fieldType == Double.class) {
                        predicates.add(criteriaBuilder.between(expression.as(Double.class), (Double) value.get(0), (Double) value.get(1)));
                    } else if (fieldType == Float.class) {
                        predicates.add(criteriaBuilder.between(expression.as(Float.class), (Float) value.get(0), (Float) value.get(1)));
                    } else if (fieldType == Short.class) {
                        predicates.add(criteriaBuilder.between(expression.as(Short.class), (Short) value.get(0), (Short) value.get(1)));
                    } else if (fieldType == Byte.class) {
                        predicates.add(criteriaBuilder.between(expression.as(Byte.class), (Byte) value.get(0), (Byte) value.get(1)));
                    } else if (fieldType == Character.class) {
                        predicates.add(criteriaBuilder.between(expression.as(Character.class), (Character) value.get(0), (Character) value.get(1)));
                    } else if (fieldType == String.class) {
                        predicates.add(criteriaBuilder.between(expression.as(String.class), (String) value.get(0), (String) value.get(1)));
                    }
                }


            }

        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    /**
     * 获取Specification
     */
    public Specification<T> getSpecification(ConditionGlobeEntity entity) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                // 获取and的条件
                List<ConditionEntity> and = entity.getAnd();
                // 获取or的条件
                List<ConditionEntity> or = entity.getOr();
                // 获取不能为空
                List<String> notNull = entity.getNotNull();
                // 获取为空
                List<String> isNull = entity.getIsNull();
                // 如果and为空赋值为一个空的集合
                if (and == null) {
                    and = new ArrayList<>();
                }
                // 如果or为空赋值为一个空的集合
                if (or == null) {
                    or = new ArrayList<>();
                }
                // 如果notNull为空赋值为一个空的集合
                if (notNull == null) {
                    notNull = new ArrayList<>();
                }
                // 如果isNull为空赋值为一个空的集合
                if (isNull == null) {
                    isNull = new ArrayList<>();
                }

                Predicate[] predicatesOfAnd = getPredicate(and, root, criteriaBuilder);
                Predicate[] predicatesOfOr = getPredicate(or, root, criteriaBuilder);
                // 如果notNull不为空,添加notNull的条件
                Predicate[] predicatesOfNotNull = notNull.stream().map(field -> criteriaBuilder.isNotNull(getExpression(root, field))).toArray(Predicate[]::new);
                // 如果isNull不为空,添加isNull的条件
                Predicate[] predicatesOfIsNull = isNull.stream().map(field -> criteriaBuilder.isNull(getExpression(root, field))).toArray(Predicate[]::new);


                return criteriaBuilder.and(criteriaBuilder.and(predicatesOfAnd), criteriaBuilder.or(criteriaBuilder.or(predicatesOfOr), criteriaBuilder.and(predicatesOfNotNull)), criteriaBuilder.and(predicatesOfIsNull));
            }

        };
    }

    /**
     * 获取表达式
     */
    public static <T> Expression<String> getExpression(Root<T> root, String field) {
        // 判断operatorEntity.getField()有没有. 如果有, 则需要拆分
        String[] fields = field.split("\\.");
        if (fields.length == 1) {
            return root.get(field);
        }
        Expression<String> expression = root.get(fields[0]);
        for (int i = 1; i < fields.length; i++) {
            expression = ((Path<String>) expression).get(fields[i]);
        }
        return expression;
    }

    /**
     * 将所有都转为BigDecimal
     */
    public static BigDecimal toBigDecimal(Object value) {
        if (value instanceof Long) {
            return new BigDecimal((Long) value);
        }
        if (value instanceof Integer) {
            return new BigDecimal((Integer) value);
        }
        if (value instanceof Double) {
            return new BigDecimal((Double) value);
        }
        if (value instanceof Float) {
            return new BigDecimal((Float) value);
        }
        if (value instanceof Short) {
            return new BigDecimal((Short) value);
        }
        if (value instanceof Byte) {
            return new BigDecimal((Byte) value);
        }
        if (value instanceof Character) {
            return new BigDecimal((Character) value);
        }
        if (value instanceof Date) {
            return new BigDecimal(((Date) value).getTime());
        }

        if (value instanceof String) {
            try {
                return new BigDecimal((String) value);
            } catch (NumberFormatException e) {
                // 如果转换失败, 则将String转为char数组, 然后遍历, 将每个char转为int, 然后相加
                char[] chars = ((String) value).toCharArray();
                int sum = 0;
                for (char aChar : chars) {
                    sum += aChar;
                }
                return new BigDecimal(sum);
            }
        }

        return new BigDecimal(value.toString());
    }


}
