package com.kantboot.test.starter.main;

import com.sun.management.OperatingSystemMXBean;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

import java.lang.management.ManagementFactory;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Main01 {

    public static void main(String[] args) {
        // 获取所有硬盘
        Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
        for (Path name : rootDirectories) {
            System.out.println("Root: " + name.toFile().getName());
            // 获取容量
            try {
                // 总空间
                long totalSpace = name.toFile().getTotalSpace();
                // 可用空间
                long freeSpace = name.toFile().getFreeSpace();
                // 可用空间（不包括系统保留空间）
                long usableSpace = name.toFile().getUsableSpace();
                // 已用空间
                long usedSpace = totalSpace - freeSpace;

                System.out.println("总空间: " + totalSpace / (1024 * 1024 * 1024) + " GB");
                System.out.println("可用空间: " + freeSpace / (1024 * 1024 * 1024) + " GB");
                System.out.println("可用空间（不包括系统保留空间）: " + usableSpace / (1024 * 1024 * 1024) + " GB");
                System.out.println("已用空间: " + usedSpace / (1024 * 1024 * 1024) + " GB");
            } catch (Exception e) {
                System.err.println("Error retrieving space for " + name + ": " + e.getMessage());
            }
            System.out.println("===================");
        }

        System.err.println("===================");


        // 获取物理内存使用信息
        OperatingSystemMXBean osBean = (OperatingSystemMXBean)
                ManagementFactory.getOperatingSystemMXBean();
        long totalPhysicalMemory = osBean.getTotalPhysicalMemorySize();
        long freePhysicalMemory = osBean.getFreePhysicalMemorySize();
        long usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory;

        System.out.println("物理内存信息:");
        System.out.println("总物理内存: " + (totalPhysicalMemory / (1024 * 1024)) + " MB");
        System.out.println("空闲物理内存: " + (freePhysicalMemory / (1024 * 1024)) + " MB");
        System.out.println("已用物理内存: " + (usedPhysicalMemory / (1024 * 1024)) + " MB");
        System.out.println("===================");

        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        CentralProcessor processor = hal.getProcessor();

        System.out.println("CPU信息:");
        System.out.println("制造商: " + processor.getProcessorIdentifier().getVendor());
        System.out.println("名称: " + processor.getProcessorIdentifier().getName());
        System.out.println("物理核心数: " + processor.getPhysicalProcessorCount());
        System.out.println("逻辑核心数: " + processor.getLogicalProcessorCount());
        System.out.println("标识符: " + processor.getProcessorIdentifier().getIdentifier());
        System.out.println("最大频率: " + processor.getMaxFreq() / 1_000_000.0 + " GHz");

        // 获取CPU使用率
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        try {
            Thread.sleep(1000); // 等待1秒获取差值
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        double cpuUsage = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
        System.out.printf("当前CPU使用率: %.1f%%\n", cpuUsage);

        System.err.println("===================");
        // 获取JVM内存信息
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();
        long usedMemory = totalMemory - freeMemory;
        System.out.println("JVM总内存: " + totalMemory / (1024 * 1024) + " MB");
        System.out.println("JVM可用内存: " + freeMemory / (1024 * 1024) + " MB");
        System.out.println("JVM最大内存: " + maxMemory / (1024 * 1024) + " MB");
        System.out.println("JVM已用内存: " + usedMemory / (1024 * 1024) + " MB");


    }

}
