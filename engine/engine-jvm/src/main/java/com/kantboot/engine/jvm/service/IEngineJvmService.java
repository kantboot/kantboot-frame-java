package com.kantboot.engine.jvm.service;

import com.kantboot.engine.jvm.domain.entity.EngineJvmHeapMemory;
import com.kantboot.engine.jvm.domain.entity.EngineJvmMemory;
import com.kantboot.engine.jvm.domain.entity.EngineJvmNonHeapMemory;

import java.lang.management.ThreadInfo;
import java.util.List;

public interface IEngineJvmService {

    /**
     * 获取JVM堆内存信息
     * Get JVM heap memory information
     *
     * @return JVM堆内存信息
     */
    EngineJvmHeapMemory getHeapMemoryInfo();

    /**
     * 获取JVM非堆内存信息
     * Get JVM non-heap memory information
     *
     * @return JVM非堆内存信息
     */
    EngineJvmNonHeapMemory getNonHeapMemoryInfo();


    /**
     * 获取JVM内存使用情况
     */
    EngineJvmMemory getMemoryInfo();

    /**
     * 获取堆栈线程信息
     */
    List<ThreadInfo> getThreadInfos();


}
