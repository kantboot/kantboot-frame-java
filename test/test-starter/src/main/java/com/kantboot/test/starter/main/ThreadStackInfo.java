package com.kantboot.test.starter.main;

import com.alibaba.fastjson2.JSON;
import lombok.SneakyThrows;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class ThreadStackInfo {
    @SneakyThrows
    public static void main(String[] args) {
        aa();
        Thread.sleep(1000);
        // 获取ThreadMXBean
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        
        // 获取所有线程ID
        long[] threadIds = threadMXBean.getAllThreadIds();
        System.out.println("##### 线程堆栈信息 #####");
        System.out.println("线程数: " + threadIds.length);
        
        // 获取每个线程的详细信息
        for (long threadId : threadIds) {
            ThreadInfo threadInfo = threadMXBean.getThreadInfo(threadId);
            if (threadInfo != null) {
//                System.out.println("\n线程ID: " + threadInfo.getThreadId());
//                System.out.println("线程名称: " + threadInfo.getThreadName());
//                System.out.println("线程状态: " + threadInfo.getThreadState());
//                System.out.println("优先级: " + threadInfo.getPriority());
//                System.out.println("是否守护线程: " + threadInfo.isDaemon());
//                System.out.println("阻塞时间: " + threadInfo.getBlockedTime());
//                System.out.println("等待时间: " + threadInfo.getWaitedTime());

//                {"blockedCount":7,"blockedTime":-1,"daemon":true,"inNative":false,"lockInfo":{"className":"[I","identityHashCode":171497379},"lockName":"[I@a38d7a3","lockOwnerId":-1,"lockedMonitors":[],"lockedSynchronizers":[],"priority":5,"stackTrace":[],"suspended":false,"threadId":27,"threadName":"JMX server connection timeout 27","threadState":"TIMED_WAITING","waitedCount":8,"waitedTime":-1}
                System.out.println("\n线程ID: " + threadInfo.getThreadId());
                System.out.println("线程名称: " + threadInfo.getThreadName());
                System.out.println("线程状态: " + threadInfo.getThreadState());
                System.out.println("优先级: " + threadInfo.getPriority());
                System.out.println("是否守护线程: " + threadInfo.isDaemon());
                System.out.println("阻塞时间: " + threadInfo.getBlockedTime());
                System.out.println("等待时间: " + threadInfo.getWaitedTime());
                System.out.println("锁信息: " + JSON.toJSONString(threadInfo.getLockInfo()));
                System.out.println("锁名称: " + JSON.toJSONString(threadInfo.getLockName()));
                System.out.println("锁拥有者ID: " + threadInfo.getLockOwnerId());
                System.out.println("等待计数: " + threadInfo.getWaitedCount());
                System.out.println("阻塞计数: " + threadInfo.getBlockedCount());
                System.out.println("挂起状态: " + threadInfo.isSuspended());
                System.out.println("是否在本地方法中: " + threadInfo.isInNative());



                // 打印堆栈跟踪
                System.out.println("堆栈跟踪:");
                for (StackTraceElement stackTraceElement : threadInfo.getStackTrace()) {
                    System.out.println("\t" + stackTraceElement);
                }
                System.err.println(JSON.toJSONString(threadInfo));
            }
        }
    }

    public static void aa(){
        new Thread(()->{
            System.err.println("====");
            try {
                Thread.sleep(1000000000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}