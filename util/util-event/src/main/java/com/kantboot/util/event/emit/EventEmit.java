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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
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

        for (Method method : methods) {
            Class<?> clazz = method.getDeclaringClass();
            Object bean = applicationContext.getBean(clazz);
            method.setAccessible(true);

            String methodWithParams = method.getClass().getName() + "." + method.getName() + JSON.toJSONString(method.getParameterTypes());

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
                                    .setData(List.of(values))
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
                throw new RuntimeException(e);
            } finally {
                Date endTime = new Date();
                Boolean finalIsExceptionEnd = isExceptionEnd;
                Exception finalException = exception;
                Thread.ofVirtual()
                        .name("event-on-end-" + code + "-" + uuid)
                        .start(() -> {
                            utilEventSlot.onEnd(new EventOnEndDTO()
                                    .setIsExceptionEnd(finalIsExceptionEnd)
                                    .setException(finalException)
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
                            value[valueIndex].getClass().getName());
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
        if (targetType == String.class) {
            return value.toString();
        }
        return value;
    }
}