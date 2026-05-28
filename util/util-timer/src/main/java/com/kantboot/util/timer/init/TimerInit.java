package com.kantboot.util.timer.init;

import com.kantboot.util.scan.init.ScanInit;
import com.kantboot.util.timer.annotation.TimerOn;
import com.kantboot.util.timer.domain.dto.TimerDTO;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
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
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定时器初始化类
 * 用于初始化定时器监听方法并将其存储在映射中
 *
 * @author 方某方
 */
@Component("timerInit")
public class TimerInit implements ApplicationContextAware {

    public static final List<TimerDTO> TIMER_LIST = new ArrayList<>();
    private static final Map<String, TimerDTO> TIMER_MAP = new ConcurrentHashMap<>();
    private static final List<String> SCANNED_CLASSES = new ArrayList<>();

    private static ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        TimerInit.applicationContext = applicationContext;
    }

    @PostConstruct
    @Priority(1) // 设置高优先级，确保最先执行
    public void init() {
        initTimerMap();
        logScannedClasses();
        logTimerMap();
    }

    /**
     * 初始化定时器映射
     */
    public void initTimerMap() {
        List<String> packagesToScan = ScanInit.scanPackages();

        for (String packageToScan : packagesToScan) {
            scanPackageForTimerMethods(packageToScan);
        }
    }

    /**
     * 打印扫描到的类信息
     */
    private static void logScannedClasses() {
//        SCANNED_CLASSES.forEach(className -> System.out.println(" - " + className));
    }

    /**
     * 打印定时器映射日志
     */
    private static void logTimerMap() {
        System.out.println("Registered timer listeners (" + TIMER_MAP.size() + " timers total):");
        TIMER_MAP.forEach((code, timerDTO) -> {
            System.out.println(" - Timer code '" + code + "': " + 
                    timerDTO.getMethod().getDeclaringClass().getSimpleName() + "." +
                    timerDTO.getMethod().getName() + "() - " + timerDTO.getTime() + "ms interval");
        });
    }

    /**
     * 扫描指定包中的定时器监听方法
     */
    private static void scanPackageForTimerMethods(String packageToScan) {
        try {
//            System.out.println("Scanning package: " + packageToScan);

            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage(packageToScan))
                    .setScanners(new SubTypesScanner(false),
                            new MethodAnnotationsScanner(),
                            new TypeAnnotationsScanner()));

            // 获取所有被扫描的类
            Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);
            allClasses.forEach(clazz -> SCANNED_CLASSES.add(clazz.getName()));

//            System.out.println("Found " + allClasses.size() + " classes in package " + packageToScan);

            // 直接扫描带有TimerOn注解的方法
            Set<Method> methods = reflections.getMethodsAnnotatedWith(TimerOn.class);

            for (Method method : methods) {
                try {
                    TimerOn annotation = method.getAnnotation(TimerOn.class);
                    String timerCode = annotation.code();
                    String timerName = annotation.name();
                    String timerDescription = annotation.description();
                    long time = annotation.time();
                    boolean lock = annotation.lock();
                    long lockMaxWaitTime = annotation.lockMaxWaitTime();
                    long lockMinContinueTime = annotation.lockMinContinueTime();

                    TimerDTO timerDTO = new TimerDTO()
                            .setCode(timerCode)
                            .setName(timerName)
                            .setDescription(timerDescription)
                            .setMethod(method)
                            .setTime(time)
                            .setLock(lock)
                            .setLockMaxWaitTime(lockMaxWaitTime)
                            .setLockMinContinueTime(lockMinContinueTime)
                            .setRunning(false)
                            .setExecuteCount(0)
                            .setNextExecuteTime(0)
                            .setLastExecuteTime(0);

                    TIMER_MAP.put(timerCode, timerDTO);
                    TIMER_LIST.add(timerDTO);

//                    System.out.println("Registered timer listener: " + timerCode + " -> " +
//                            method.getDeclaringClass().getSimpleName() + "." + method.getName() +
//                            " - " + time + "ms");

                } catch (Exception e) {
                    System.err.println("Error processing method " + method + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error scanning package " + packageToScan + ": " + e.getMessage());
        }
//        TIMER_LIST去重
        Set<TimerDTO> uniqueTimers = new HashSet<>(TIMER_LIST);
        TIMER_LIST.clear();
        TIMER_LIST.addAll(uniqueTimers);
    }

    public static TimerDTO getTimer(String code) {
        return TIMER_MAP.get(code);
    }

    public static Map<String, TimerDTO> getAllTimers() {
        return new HashMap<>(TIMER_MAP);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 更新定时器状态
     */
    public static void updateTimerStatus(String code, boolean running) {
        TimerDTO timer = TIMER_MAP.get(code);
        if (timer != null) {
            timer.setRunning(running);
        }
    }

    /**
     * 更新定时器执行信息
     */
    public static void updateTimerExecuteInfo(String code, long executeTime) {
        TimerDTO timer = TIMER_MAP.get(code);
        if (timer != null) {
            timer.setLastExecuteTime(executeTime);
            timer.setExecuteCount(timer.getExecuteCount() + 1);
            timer.setNextExecuteTime(executeTime + timer.getTime());
        }
    }
}
