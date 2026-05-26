package com.kantboot.test.starter.main;

import java.util.Properties;

public class JavaInfoViewer {
    public static void main(String[] args) {
        // 获取Java运行时属性
        Properties props = System.getProperties();
        
        // 输出核心Java信息
        System.out.println("============= Java核心信息 =============");
        System.out.println("Java版本: " + props.getProperty("java.version"));
        System.out.println("Java运行时版本: " + props.getProperty("java.runtime.version"));
        System.out.println("Java虚拟机版本: " + props.getProperty("java.vm.version"));
        System.out.println("Java供应商: " + props.getProperty("java.vendor"));
        System.out.println("Java安装目录: " + props.getProperty("java.home"));
        System.out.println("Java类路径: " + props.getProperty("java.class.path"));
        System.out.println("Java类版本: " + props.getProperty("java.class.version"));
        System.out.println("Java编译器: " + props.getProperty("java.compiler"));


        // 输出JVM信息
        System.out.println("\n============= JVM信息 =============");
        System.out.println("JVM名称: " + props.getProperty("java.vm.name"));
        System.out.println("JVM供应商: " + props.getProperty("java.vm.vendor"));
        System.out.println("JVM规范版本: " + props.getProperty("java.vm.specification.version"));
        System.out.println("JVM执行模式: " + (props.getProperty("java.vm.info").contains("mixed mode") ? "混合模式" : "解释模式"));
        
        // 输出系统信息
        System.out.println("\n============= 系统信息 =============");
        System.out.println("操作系统: " + props.getProperty("os.name"));
        System.out.println("操作系统版本: " + props.getProperty("os.version"));
        System.out.println("系统架构: " + props.getProperty("os.arch"));
        System.out.println("可用处理器核心数: " + Runtime.getRuntime().availableProcessors());
        
        // 输出内存信息
        System.out.println("\n============= 内存信息 =============");
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        System.out.printf("最大内存: %.2f MB\n", maxMemory / (1024.0 * 1024));
        System.out.printf("已分配内存: %.2f MB\n", totalMemory / (1024.0 * 1024));
        System.out.printf("已使用内存: %.2f MB\n", (totalMemory - freeMemory) / (1024.0 * 1024));
        System.out.printf("可用内存: %.2f MB\n", freeMemory / (1024.0 * 1024));
        
        // 输出其他信息
        System.out.println("\n============= 其他信息 =============");
        System.out.println("类路径: " + props.getProperty("java.class.path"));
        System.out.println("用户目录: " + props.getProperty("user.dir"));
        System.out.println("临时目录: " + props.getProperty("java.io.tmpdir"));
    }
}