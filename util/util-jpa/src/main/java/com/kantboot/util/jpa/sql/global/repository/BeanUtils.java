package com.kantboot.util.jpa.sql.global.repository;

import org.springframework.beans.FatalBeanException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
     * bean copy不复制null值
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyPropertiesWithoutNull(Object source, Object target) {
        if(source == null || target == null){
            return;
        }
        Class<?> actualEditable = target.getClass();
        Class<?> sourceClass = source.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        for (PropertyDescriptor targetPd : targetPds) {
            if(targetPd.getWriteMethod() == null) {
                continue;
            }
            PropertyDescriptor sourcePd = getPropertyDescriptor(sourceClass, targetPd.getName());
            if(sourcePd == null || sourcePd.getReadMethod() == null) {
                continue;
            }
            try {
                Method readMethod = sourcePd.getReadMethod();
                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                    readMethod.setAccessible(true);
                }
                Object value = readMethod.invoke(source);
                setValue(target, targetPd, value);
            } catch (Exception ex) {
                throw new FatalBeanException("Could not copy properties from source to target", ex);
            }
        }
    }

    /**
     * 设置值到目标bean
     * @param target 目标bean
     * @param targetPd 目标bean的属性描述器
     * @param value 值
     */
    private static void setValue(Object target, PropertyDescriptor targetPd, Object value) throws IllegalAccessException, InvocationTargetException {
        // 这里判断以下value是否为空
        if (value != null) {
            Method writeMethod = targetPd.getWriteMethod();
            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                writeMethod.setAccessible(true);
            }
            writeMethod.invoke(target, value);
        }
    }
}