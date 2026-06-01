package com.kantboot.util.event.emit;

import com.alibaba.fastjson2.JSON;
import com.kantboot.util.event.domain.dto.EventOnEndDTO;
import com.kantboot.util.event.domain.dto.EventOnInProgressDTO;
import com.kantboot.util.event.domain.dto.EventOnStartDTO;
import com.kantboot.util.event.init.EmitInit;
import com.kantboot.util.event.slot.UtilEventSlot;
import com.kantboot.util.log.Logger;
import com.kantboot.util.log.domain.LoggerItem;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
public class EventEmit implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Resource
    private UtilEventSlot utilEventSlot;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void to(String code, Object... values) {
        log.info("GlobalEventEmit.emit: code={}", code);
        List<Method> methods = EmitInit.getMethod(code);
        if (methods == null) {
            log.info("GlobalEventEmit.emit: Method is null");
            return;
        }

        Exception firstException = null;
        for (Method method : methods) {
            method.setAccessible(true);
            Object bean = resolveBean(method);
            if (bean == null) {
                log.warn("未找到事件监听 Bean: {}", method.getDeclaringClass().getName());
                continue;
            }

            String methodWithParams = method.getDeclaringClass().getName() + "." + method.getName() + JSON.toJSONString(method.getParameterTypes());

            Date startTime = new Date();
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");

            // 创建 CountDownLatch 用于同步
            CountDownLatch startLatch = new CountDownLatch(1);

            Thread.ofVirtual()
                    .name("event-on-start-" + code + "-" + uuid)
                    .start(() -> {
                        try {
                            utilEventSlot.onStart(new EventOnStartDTO()
                                    .setCode(code)
                                    .setUuid(uuid)
                                    .setData(copyValues(values))
                                    .setGmtOnStart(startTime)
                                    .setMethodWithParams(methodWithParams)
                            );
                        } finally {
                            // 确保无论成功与否都释放锁
                            startLatch.countDown();
                        }
                    });

            Logger logger = new Logger() {
                @Override
                public void callback(LoggerItem loggerItem) {
                    try {
                        // 等待 event-on-start 完成
                        startLatch.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.error("等待 event-on-start 时被中断", e);
                    }
                    Thread.ofVirtual()
                            .name("event-on-progress-" + code + "-" + uuid + "-" + System.nanoTime())
                            .start(() -> {
                                utilEventSlot.onInProgress(new EventOnInProgressDTO()
                                        .setGmtOn(new Date())
                                        .setCode(code)
                                        .setUuid(uuid)
                                        .setData(values)
                                        .setLoggerItem(loggerItem)
                                );
                            });
                }
            };

            Object[] invokeParams = prepareInvokeParameters(method, logger, values);
            Boolean isExceptionEnd = false;
            Exception exception = null;

            try {
                method.invoke(bean, invokeParams);
            } catch (Exception e) {
                isExceptionEnd = true;
                exception = e;
                if (firstException == null) {
                    firstException = e;
                }
                log.error("事件监听执行失败: {}.{} - {}", method.getDeclaringClass().getName(), method.getName(), e.getMessage(), e);
            } finally {
                Date endTime = new Date();
                Boolean finalIsExceptionEnd = isExceptionEnd;
                Exception finalException = exception;
                Thread.ofVirtual()
                        .name("event-on-end-" + code + "-" + uuid)
                        .start(() -> {
                            long duration = endTime.getTime() - startTime.getTime();
                            utilEventSlot.onEnd(new EventOnEndDTO()
                                    .setIsExceptionEnd(finalIsExceptionEnd)
                                    .setException(finalException)
                                    .setDuration(duration)
                                    .setSuccess(!finalIsExceptionEnd)
                                    .setExceptionMessage(finalException == null ? null : finalException.getMessage())
                                    .setCode(code)
                                    .setUuid(uuid)
                                    .setData(values)
                                    .setGmtOnStart(startTime)
                                    .setGmtOnEnd(endTime)
                                    .setLoggerItems(logger.getLoggerItems())
                            );
                });
            }
        }

        if (firstException != null) {
            throw new RuntimeException(firstException);
        }
    }

    private Object[] prepareInvokeParameters(Method method, Logger logger, Object... value) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] params = new Object[parameterTypes.length];

        int valueIndex = 0;
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] == Logger.class) {
                params[i] = logger;
            } else if (valueIndex < value.length) {
                if (parameterTypes[i].isInstance(value[valueIndex])) {
                    params[i] = value[valueIndex];
                } else {
                    log.warn("参数类型不匹配，期望: {}, 实际: {}",
                            parameterTypes[i].getName(),
                            value[valueIndex] == null ? "null" : value[valueIndex].getClass().getName());
                    params[i] = tryConvertType(value[valueIndex], parameterTypes[i]);
                }
                valueIndex++;
            } else {
                params[i] = null;
            }
        }

        return params;
    }

    private Object tryConvertType(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }
        if (targetType == String.class) {
            return value.toString();
        }
        return value;
    }

    private Object resolveBean(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        try {
            return applicationContext.getBean(declaringClass);
        } catch (Exception ignore) {
            for (String beanName : applicationContext.getBeanNamesForType(Object.class)) {
                Object bean = applicationContext.getBean(beanName);
                Class<?> targetClass = AopUtils.getTargetClass(bean);
                if (declaringClass.isAssignableFrom(targetClass) || targetClass.equals(declaringClass)) {
                    return bean;
                }
            }
        }
        return null;
    }

    private List<Object> copyValues(Object... values) {
        List<Object> result = new ArrayList<>();
        if (values == null) {
            return result;
        }
        result.addAll(Arrays.asList(values));
        return result;
    }
}
