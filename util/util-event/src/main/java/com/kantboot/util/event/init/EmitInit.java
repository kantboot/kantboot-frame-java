package com.kantboot.util.event.init;

import com.alibaba.fastjson2.JSON;
import com.kantboot.util.event.annotation.EventOn;
import com.kantboot.util.event.annotation.EventParam;
import com.kantboot.util.event.domain.dto.EventDTO;
import com.kantboot.util.event.domain.dto.ParamInEventDTO;
import com.kantboot.util.scan.init.ScanInit;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 事件初始化类
 * 用于初始化事件监听方法并将其存储在映射中
 */
@Slf4j
@Component
public class EmitInit implements ApplicationContextAware {

    public static final List<EventDTO> EVENT_LIST = new ArrayList<>();
    private static final Map<String, List<Method>> EVENT_MAP = new HashMap<>();
    private static final List<String> SCANNED_CLASSES = new ArrayList<>();

    private static ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        EmitInit.applicationContext = applicationContext;
    }

    @PostConstruct
    @Priority(2)
    public void init() {
        initEventMap();
        logScannedClasses();
        logEventMap();
    }

    /**
     * 初始化事件映射
     */
    public void initEventMap() {
        List<String> packagesToScan = ScanInit.scanPackages();

        for (String packageToScan : packagesToScan) {
            scanPackageForEventMethods(packageToScan);
        }
    }

    /**
     * 打印扫描到的类信息
     */
    private static void logScannedClasses() {
        SCANNED_CLASSES.forEach(className -> log.info(" - {}", className));
    }

    /**
     * 打印事件映射日志
     */
    private static void logEventMap() {
        EVENT_MAP.forEach((code, methods) -> {
            methods.forEach(method ->
                    log.info("   > {}.{}()",
                            method.getDeclaringClass().getSimpleName(),
                            method.getName())
            );
        });
    }

    /**
     * 扫描指定包中的事件监听方法
     */
    private static void scanPackageForEventMethods(String packageToScan) {
        try {

            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage(packageToScan))
                    .setScanners(new SubTypesScanner(false),
                            new MethodAnnotationsScanner(),
                            new TypeAnnotationsScanner()));

            // 获取所有被扫描的类
            Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);
            allClasses.forEach(clazz -> SCANNED_CLASSES.add(clazz.getName()));

            // 直接扫描带有EventOn注解的方法
            Set<Method> methods = reflections.getMethodsAnnotatedWith(EventOn.class);

            for (Method method : methods) {
                try {
                    EventOn annotation = method.getAnnotation(EventOn.class);
                    String eventCode = annotation.code();
                    String eventName = annotation.name();
                    String eventDescription = annotation.description();

                    List<ParamInEventDTO> params = new ArrayList<>();

                    Class<?>[] parameterTypes = method.getParameterTypes();
                    // 获取参数前的EventParam注解
                    for (int i = 0; i < parameterTypes.length; i++) {
                        if("com.kantboot.util.log.Logger".equals(parameterTypes[i].getName())){
                            continue;
                        }
                        ParamInEventDTO param = new ParamInEventDTO();
                        param.setIndexInMethod(i);
                        param.setType(parameterTypes[i].getName());
                        if (method.getParameters()[i].isAnnotationPresent(EventParam.class)) {
                            EventParam eventParamAnnotation = method.getParameters()[i].getAnnotation(EventParam.class);
                            param.setName(eventParamAnnotation.name());
                            param.setDescription(eventParamAnnotation.description());
                        }
                        params.add(param);
                    }


                    String methodWithParams = method.getClass().getName()+"."+method.getName()+ JSON.toJSONString(method.getParameterTypes());

                    EVENT_MAP.computeIfAbsent(eventCode, k -> new ArrayList<>()).add(method);
                    EVENT_LIST.add(new EventDTO()
                            .setCode(eventCode)
                            .setParams(params)
                            .setName(eventName)
                            .setDescription(eventDescription)
                            .setMethod(method)
                            .setMethodWithParams(methodWithParams)
                    );


                } catch (Exception e) {
                    log.error("Error processing method {}: {}", method, e.getMessage());
                }
            }
        } catch (Exception e) {
            log.warn("Error scanning package {}: {}", packageToScan, e.getMessage());
        }
        // EVENT_LIST去重
        Set<EventDTO> uniqueEvents = new HashSet<>(EVENT_LIST);
        EVENT_LIST.clear();
        EVENT_LIST.addAll(uniqueEvents);
    }

    public static List<Method> getMethod(String code) {
        return EVENT_MAP.getOrDefault(code, new ArrayList<>());
    }

}