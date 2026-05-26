package com.kantboot.util.scan.init;

import com.kantboot.util.scan.annotation.KantbootScan;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class ScanInit implements ApplicationContextAware {

    private static final String DEFAULT_BASE_PACKAGE = "com.kantboot";
    private static ApplicationContext applicationContext;
    public static List<String> PACKAGES_FOR_SCAN;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        ScanInit.applicationContext = applicationContext;
    }

    public static List<String> scanPackages() {
        // 始终包含默认包
        List<String> packagesToScan = new ArrayList<>();
        packagesToScan.add(DEFAULT_BASE_PACKAGE);

        try {
            // 1. 首先尝试通过启动堆栈识别主类
            Optional<Class<?>> mainClass = findMainClassFromStackTrace();

            // 2. 如果堆栈方式失败，尝试扫描注解
            if (mainClass.isEmpty()) {
                mainClass = findMainClassByAnnotation();
            }

            // 3. 如果找到主类，处理其注解
            if (mainClass.isPresent()) {
                processKantbootScanAnnotation(mainClass.get(), packagesToScan);
            } else {
                log.warn("未能识别主应用类，使用默认扫描包");
            }
        } catch (Exception e) {
            log.error("扫描包路径初始化失败: {}", e.getMessage(), e);
        }

        // 确保 com.kantboot 始终存在（双重保障）
        ensureDefaultPackage(packagesToScan);

        return packagesToScan;
    }

    private static void ensureDefaultPackage(List<String> packages) {
        if (!packages.contains(DEFAULT_BASE_PACKAGE)) {
            log.info("确保默认包 {} 被包含", DEFAULT_BASE_PACKAGE);
            packages.add(0, DEFAULT_BASE_PACKAGE); // 添加到首位
        }
    }

    private static void processKantbootScanAnnotation(Class<?> mainClass, List<String> packages) {
        // 处理可能的代理类
        Class<?> targetClass = getTargetClass(mainClass);

        KantbootScan kantbootScan = targetClass.getAnnotation(KantbootScan.class);
        if (kantbootScan != null) {
            log.debug("找到 @KantbootScan 注解在类: {}", targetClass.getName());

            // 添加注解中指定的所有包（如果尚未存在）
            for (String pkg : kantbootScan.value()) {
                if (!packages.contains(pkg)) {
                    packages.add(pkg);
                }
            }
        } else {
            log.debug("主类 {} 上未找到 @KantbootScan 注解", targetClass.getName());
        }
    }

    private static Class<?> getTargetClass(Class<?> clazz) {
        // 处理代理类获取原始类
        while (clazz.getName().contains("$$")) {
            clazz = clazz.getSuperclass();
        }
        return clazz;
    }

    private static Optional<Class<?>> findMainClassFromStackTrace() {
        try {
            // 获取当前线程的堆栈跟踪
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

            // 从堆栈底部开始查找（main方法通常在较深的位置）
            for (int i = stackTrace.length - 1; i >= 0; i--) {
                StackTraceElement element = stackTrace[i];
                if ("main".equals(element.getMethodName())) {
                    String className = element.getClassName();
                    try {
                        // 使用当前线程的类加载器加载类
                        Class<?> clazz = Class.forName(className);

                        // 检查是否是Spring Boot应用主类
                        if (isSpringBootApplication(clazz)) {
                            log.debug("通过堆栈找到主类: {}", className);
                            return Optional.of(clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        log.debug("无法加载类: {}", className);
                    }
                }
            }
        } catch (Exception e) {
            log.debug("通过堆栈跟踪查找主类失败: {}", e.getMessage());
        }
        return Optional.empty();
    }

    private static boolean isSpringBootApplication(Class<?> clazz) {
        // 检查是否包含SpringBootApplication注解
        return clazz.isAnnotationPresent(org.springframework.boot.autoconfigure.SpringBootApplication.class);
    }

    private static Optional<Class<?>> findMainClassByAnnotation() {
        try {
            ClassPathScanningCandidateComponentProvider scanner =
                    new ClassPathScanningCandidateComponentProvider(false);

            scanner.addIncludeFilter(new AnnotationTypeFilter(org.springframework.boot.autoconfigure.SpringBootApplication.class));

            // 从默认包开始扫描
            Set<BeanDefinition> candidates = scanner.findCandidateComponents(DEFAULT_BASE_PACKAGE);

            if (candidates.isEmpty()) {
                // 扩大扫描范围
                candidates = scanner.findCandidateComponents("");
            }

            for (BeanDefinition candidate : candidates) {
                try {
                    Class<?> clazz = Class.forName(candidate.getBeanClassName());
                    if (isSpringBootApplication(clazz)) {
                        log.debug("通过注解扫描找到主类: {}", clazz.getName());
                        return Optional.of(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    log.debug("无法加载候选类: {}", candidate.getBeanClassName());
                }
            }
        } catch (Exception e) {
            log.error("通过注解扫描查找主类失败: {}", e.getMessage(), e);
        }
        return Optional.empty();
    }

    public static List<String> getPackagesForScan() {
        return PACKAGES_FOR_SCAN != null ? PACKAGES_FOR_SCAN : Arrays.asList(DEFAULT_BASE_PACKAGE);
    }
}