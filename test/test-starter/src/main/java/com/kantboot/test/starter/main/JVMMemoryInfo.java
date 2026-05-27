package com.kantboot.test.starter.main;

public class JVMMemoryInfo {
    public static void main(String[] args) {
        // 获取Runtime实例
        Runtime runtime = Runtime.getRuntime();
        
        // 打印内存信息
        System.out.println("##### JVM内存信息 #####");
        System.out.println("最大内存: " + runtime.maxMemory() / 1024 / 1024 + "MB");
        System.out.println("总内存: " + runtime.totalMemory() / 1024 / 1024 + "MB");
        System.out.println("空闲内存: " + runtime.freeMemory() / 1024 / 1024 + "MB");
        System.out.println("已用内存: " + (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024 + "MB");
    }
}