package com.kantboot.test.starter.main;

import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

public class Main02 {

    public static void main(String[] args) {
        int pid = 4;
        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();

        // 获取指定 PID 的进程
        OSProcess p = os.getProcess(pid);
        if (p == null) {
            System.err.println("Process with PID " + pid + " not found.");
            return;
        }

        // 开始时间
        long startTime = System.currentTimeMillis();

        int threadCount = p.getThreadCount();
        System.err.println("线程数量: " + threadCount);

//        List<OSThread> threadDetails = p.getThreadDetails();
//        用户态时间（单位：毫秒或时钟周期）。线程在用户模式下消耗的CPU时间，用于执行应用程序代码。
//        for (OSThread threadDetail : threadDetails) {
//            System.out.println("线程ID: " + threadDetail.getThreadId());
//            System.out.println("线程名称: " + threadDetail.getName());
//            System.out.println("上下文切换次数: " + threadDetail.getContextSwitches());
//            System.out.println("内核态时间: " + threadDetail.getKernelTime());
//            System.out.println("用户态时间: " + threadDetail.getUserTime());
//            System.out.println("线程状态: " + threadDetail.getState());
//            System.out.println("线程CPU负载: " + threadDetail.getThreadCpuLoadCumulative());
//            System.out.println("线程优先级: " + threadDetail.getPriority());
//            System.out.println("线程启动时间: " + threadDetail.getStartTime());
//            System.out.println("线程存活时间: " + threadDetail.getUpTime());
//            System.out.println("线程所属进程ID: " + threadDetail.getOwningProcessId());
//            System.out.println("线程起始内存地址: " + threadDetail.getStartMemoryAddress());
//            System.out.println("线程主要缺页错误次数: " + threadDetail.getMajorFaults());
//            System.out.println("线程次要缺页错误次数: " + threadDetail.getMinorFaults());
//            System.err.println("==========");
//        }
        // 结束时间
        long endTime = System.currentTimeMillis();
        System.err.println("Time taken to get process details: " + (endTime - startTime) + " ms");
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
