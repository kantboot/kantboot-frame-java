package com.kantboot.system.setting.aop;

import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.util.setting.annotation.Setting;
import com.kantboot.util.setting.annotation.SettingGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SettingSyncAspect {

    private final ISysSettingService sysSettingService;

    // 定义切点：拦截所有带有@SettingGroup注解的类的getter和setter方法
    @Pointcut("@within(com.kantboot.util.setting.annotation.SettingGroup) && (execution(* *.get*()) || execution(* *.set*(*)))")
    public void settingAccessMethods() {}

    @Around("settingAccessMethods() && target(targetBean)")
    public Object syncSetting(ProceedingJoinPoint joinPoint, Object targetBean) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Class<?> targetClass = AopUtils.getTargetClass(targetBean);

        // 1. 检查类是否标记了@SettingGroup
        SettingGroup groupAnnotation = targetClass.getAnnotation(SettingGroup.class);
        if (groupAnnotation == null) {
            return joinPoint.proceed(); // 非目标类，直接放行
        }

        // 2. 解析字段名（例如：getApiCode → apiCode）
        String fieldName = methodName.startsWith("get") ?
                methodName.substring(3) :
                methodName.startsWith("set") ? methodName.substring(3) : null;
        if (fieldName == null) return joinPoint.proceed();
        fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);

        // 3. 获取字段的@Setting注解
        Field field;
        try {
            field = targetClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            log.warn("Field {} not found in class {}", fieldName, targetClass.getSimpleName());
            return joinPoint.proceed();
        }
        Setting settingAnnotation = field.getAnnotation(Setting.class);
        if (settingAnnotation == null) return joinPoint.proceed();

        // 4. 构建完整编码（例如：test.apiCode）
        String fullCode = groupAnnotation.code() + "." + settingAnnotation.code();

        // 5. 处理Getter和Setter逻辑
        if (methodName.startsWith("get")) {
            // 从服务层获取值
            String value = sysSettingService.getValue(fullCode);
            return convertStringToValue(value, field.getType());
        } else if (methodName.startsWith("set")) {
            // 调用原始Setter方法（更新字段值）
            Object newValue = joinPoint.getArgs()[0];
            Object result = joinPoint.proceed();

            // 同步到服务层
            String valueStr = convertValueToString(newValue, field.getType());
            sysSettingService.setValue(fullCode, valueStr);
            return result;
        }

        return joinPoint.proceed();
    }

    // 类型转换：String → 实际类型
    private Object convertStringToValue(String value, Class<?> targetType) {
        if (targetType == String.class) return value;
        if (targetType == Integer.class) return Integer.parseInt(value);
        if (targetType == Boolean.class) return Boolean.parseBoolean(value);
        // 扩展其他类型...
        return value;
    }

    // 类型转换：实际类型 → String
    private String convertValueToString(Object value, Class<?> sourceType) {
        return value != null ? value.toString() : "";
    }
}