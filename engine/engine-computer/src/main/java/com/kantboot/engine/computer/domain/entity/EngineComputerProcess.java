package com.kantboot.engine.computer.domain.entity;

import lombok.Data;

/**
 * 计算机进程信息
 */
@Data
public class EngineComputerProcess {

    /**
     * 进程ID
     */
    private int pid;

    /**
     * 进程名称
     */
    private String name;

    /**
     * 运行用户
     */
    private String user;

    /**
     * 进程状态（如 RUNNING, SLEEPING, ZOMBIE 等）
     */
    private String state;

    /**
     * CPU使用率
     */
    private double cpuUsage;

    /**
     * 完整命令行
     */
    private String commandLine;

    /**
     * 进程所属的线程数
     */
    private int threadCount;

    /**
     * 虚拟内存大小（单位：字节）
     */
    private long virtualSize;

    /**
     * 物理内存占用（单位：字节）
     */
    private long residentSetSize;

    /**
     * 内核态 CPU 时间（单位：毫秒）
     */
    private long kernelTime;

    /**
     * 用户态 CPU 时间（单位：毫秒）
     */
    private long userTime;

    /**
     * 进程启动时间（Unix 时间戳）
     */
    private long startTime;

}