package com.kantboot.engine.computer.domain.entity;

import lombok.Data;

@Data
public class EngineComputerProcessThread {

    /**
     * 线程ID
     */
    private long threadId;

    /**
     * 线程名称
     */
    private String name;

    /**
     * 上下文切换次数
     */
    private long contextSwitches;

    /**
     * 内核态时间（单位：毫秒）
     */
    private long kernelTime;

    /**
     * 用户态时间（单位：毫秒）
     */
    private long userTime;

    /**
     * 线程状态
     */
    private String state;

    /**
     * 线程CPU负载（累积）
     */
    private double threadCpuLoadCumulative;

    /**
     * 线程优先级
     */
    private int priority;

    /**
     * 线程启动时间（单位：毫秒）
     */
    private long startTime;

    /**
     * 线程存活时间（单位：毫秒）
     */
    private long upTime;

    /**
     * 线程所属进程ID
     */
    private int owningProcessId;

    /**
     * 线程起始内存地址
     */
    private long startMemoryAddress;

    /**
     * 线程主要缺页错误次数
     */
    private long majorFaults;

    /**
     * 线程次要缺页错误次数
     */
    private long minorFaults;

    /**
     * 线程所属进程名称
     */
    private String owningProcessName;

    /**
     * 线程所属进程用户
     */
    private String owningProcessUser;

    /**
     * 线程所属进程状态
     */
    private String owningProcessState;

    /**
     * 线程所属进程CPU使用率
     */
    private double owningProcessCpuUsage;

}
