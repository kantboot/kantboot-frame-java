package com.kantboot.test.starter.main;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class HeapMemoryInfo {
    public static void main(String[] args) {
        // 获取MemoryMXBean
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        
        // 堆内存使用情况
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        System.out.println("##### 堆内存信息 #####");
        printMemoryUsage(heapMemoryUsage);
        
        // 非堆内存使用情况
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        System.out.println("##### 非堆内存信息 #####");
        printMemoryUsage(nonHeapMemoryUsage);
    }
    
    private static void printMemoryUsage(MemoryUsage memoryUsage) {
        System.out.println("初始大小: " + memoryUsage.getInit() / 1024 / 1024 + "MB");
        System.out.println("已使用: " + memoryUsage.getUsed() / 1024 / 1024 + "MB");
        System.out.println("提交大小: " + memoryUsage.getCommitted() / 1024 / 1024 + "MB");
        System.out.println("最大大小: " + memoryUsage.getMax() / 1024 / 1024 + "MB");
    }
}