package com.kantboot.util.i18n.util;

import com.kantboot.util.i18n.annotation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class I18nUtil {

    /**
     * 判断该类是否有I18nTopKey注解
     */
    public static boolean hasI18nTopKey(Class<?> clazz) {
        I18nTopKey i18nTopKeyAnnotation = clazz.getAnnotation(I18nTopKey.class);
        return i18nTopKeyAnnotation != null;
    }

    /**
     * 根据类上的I18nTopKey注解获取国际化键
     */
    public static String getI18nTopKey(Class<?> clazz) {
        I18nTopKey i18nTopKeyAnnotation = clazz.getAnnotation(I18nTopKey.class);
        if (i18nTopKeyAnnotation == null) {
            return null;
        }
        String topKey = i18nTopKeyAnnotation.key();
        if (topKey.isEmpty()) {
            topKey = clazz.getSimpleName();
        }
        return topKey;
    }

    /**
     * 获取该类上的I18nCenterKey注解的字段
     */
    public static String getFieldFromI18nCenterKey(Class<?> clazz) {
        try{
            Field[] parentFields = clazz.getSuperclass().getDeclaredFields();
            for (Field field : parentFields) {
                I18nCenterKey i18nCenterKeyAnnotation = field.getAnnotation(I18nCenterKey.class);
                if (i18nCenterKeyAnnotation != null) {
                    return field.getName();
                }
                if (field.isAnnotationPresent(I18nCenterKey.class)) {
                    return field.getName();
                }
            }
        } catch (Exception e) {
            // 不处理
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            I18nCenterKey i18nCenterKeyAnnotation = field.getAnnotation(I18nCenterKey.class);
            if (i18nCenterKeyAnnotation != null) {
                return field.getName();
            }
            if (field.isAnnotationPresent(I18nCenterKey.class)) {
                return field.getName();
            }
        }
        return null;
    }

    /**
     * 获取该字段上是否有I18nBottomKey注解
     */
    public static boolean hasI18nBottomKey(Class<?> targetClass, String fieldName) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            return field.getAnnotation(I18nBottomKey.class) != null;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    /**
     * 获取该类中所有的字段上有I18nBottomKey注解的属性
     */
    public static List<String> getFieldsFromI18nBottomKey(Class<?> clazz) {
        List<String> fieldNames = new ArrayList<>();

        Class<?> superclass = clazz.getSuperclass();
        Field[] superclassFields = superclass.getDeclaredFields();
        for (Field field : superclassFields) {
            I18nBottomKey i18nBottomKeyAnnotation = field.getAnnotation(I18nBottomKey.class);
            if (i18nBottomKeyAnnotation != null) {
                fieldNames.add(field.getName());
            }
            if (field.isAnnotationPresent(I18nBottomKey.class)) {
                fieldNames.add(field.getName());
            }
        }


        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            I18nBottomKey i18nBottomKeyAnnotation = field.getAnnotation(I18nBottomKey.class);
            if (i18nBottomKeyAnnotation != null) {
                fieldNames.add(field.getName());
            }
            if (field.isAnnotationPresent(I18nBottomKey.class)) {
                fieldNames.add(field.getName());
            }
        }
        return fieldNames;
    }

    /**
     * 获取存储前缀
     */
    public static String getI18nSavePrefix(String languageCode, String fieldName) {
        return languageCode + "-" + fieldName + ":";
    }

    /**
     * 构建国际化存储结构
     * @param languageCode 语言代码 (如 en-US)
     * @param fieldName 字段名
     * @param fieldValue 字段值
     * @param oldValue 历史存储数据
     * @return 嵌套Map结构 {语言代码: {字段键: 字段值}}
     */
    public static Map<String, Map<String, Object>> getI18nSave(
            String languageCode,
            String fieldName,
            Object fieldValue,
            Map<String, Map<String, Object>> oldValue
    ) {
        // 1. 初始化或复用旧数据
        Map<String, Map<String, Object>> result =
                (oldValue != null) ? new HashMap<>(oldValue) : new HashMap<>();

        // 2. 获取/创建当前语言的存储Map
        Map<String, Object> langMap = result.computeIfAbsent(
                languageCode,
                k -> new HashMap<>()
        );

        // 3. 更新字段值（使用统一键格式）
        String storageKey = getI18nSavePrefix(languageCode, fieldName);
        langMap.put(storageKey, fieldValue);

        return result;
    }

    /**
     * 获取I18nSave的字段
     */
    public static String getFieldFromI18nSave(Class<?> clazz) {
        Class<?> superclass = clazz.getSuperclass();
        Field[] superclassFields = superclass.getDeclaredFields();

        for (Field field : superclassFields) {
            if (field.isAnnotationPresent(I18nSave.class)) {
                return field.getName();
            }
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            I18nSave i18nSaveAnnotation = field.getAnnotation(I18nSave.class);
            if (i18nSaveAnnotation != null) {
                return field.getName();
            }

        }
        return null;
    }

    /**
     * 获取I18nQuery的字段
     */
    public static String getFieldFromI18nQuery(Class<?> clazz) {
        Class<?> superclass = clazz.getSuperclass();
        Field[] superclassFields = superclass.getDeclaredFields();

        for (Field field : superclassFields) {
            if (field.isAnnotationPresent(I18nQuery.class)) {
                return field.getName();
            }
        }


        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(com.kantboot.util.i18n.annotation.I18nQuery.class)) {
                return field.getName();
            }
        }
        return null;
    }
}