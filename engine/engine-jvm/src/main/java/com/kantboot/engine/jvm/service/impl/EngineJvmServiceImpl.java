package com.kantboot.engine.jvm.service.impl;

import com.kantboot.engine.jvm.domain.entity.EngineJvmHeapMemory;
import com.kantboot.engine.jvm.domain.entity.EngineJvmMemory;
import com.kantboot.engine.jvm.domain.entity.EngineJvmNonHeapMemory;
import com.kantboot.engine.jvm.service.IEngineJvmService;
import org.springframework.stereotype.Service;

import java.lang.management.*;
import java.util.List;

@Service
public class EngineJvmServiceImpl implements IEngineJvmService {

    @Override
    public EngineJvmHeapMemory getHeapMemoryInfo() {
        // 获取MemoryMXBean
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        // 堆内存使用情况
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        EngineJvmHeapMemory engineJvmHeapMemory = new EngineJvmHeapMemory();
        engineJvmHeapMemory.setInitMemory(heapMemoryUsage.getInit());
        engineJvmHeapMemory.setUsedMemory(heapMemoryUsage.getUsed());
        engineJvmHeapMemory.setCommittedMemory(heapMemoryUsage.getCommitted());
        engineJvmHeapMemory.setMaxMemory(heapMemoryUsage.getMax());
        return engineJvmHeapMemory;
    }

    @Override
    public EngineJvmNonHeapMemory getNonHeapMemoryInfo() {
        // 获取MemoryMXBean
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        // 非堆内存使用情况
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        EngineJvmNonHeapMemory engineJvmNonHeapMemory = new EngineJvmNonHeapMemory();
        engineJvmNonHeapMemory.setInitMemory(nonHeapMemoryUsage.getInit());
        engineJvmNonHeapMemory.setUsedMemory(nonHeapMemoryUsage.getUsed());
        engineJvmNonHeapMemory.setCommittedMemory(nonHeapMemoryUsage.getCommitted());
        engineJvmNonHeapMemory.setMaxMemory(nonHeapMemoryUsage.getMax());
        return engineJvmNonHeapMemory;
    }

    @Override
    public EngineJvmMemory getMemoryInfo() {
        EngineJvmMemory engineJvmMemory = new EngineJvmMemory();
        Runtime runtime = Runtime.getRuntime();
        engineJvmMemory.setMaxMemory(runtime.maxMemory());
        engineJvmMemory.setAllocatedMemory(runtime.totalMemory());
        engineJvmMemory.setFreeMemory(runtime.freeMemory());
        engineJvmMemory.setUsedMemory((runtime.totalMemory() - runtime.freeMemory()));
        engineJvmMemory.setHeapMemory(getHeapMemoryInfo());
        engineJvmMemory.setNonHeapMemory(getNonHeapMemoryInfo());
        return engineJvmMemory;
    }

    @Override
    public List<ThreadInfo> getThreadInfos() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        // 获取所有线程ID
        long[] threadIds = threadMXBean.getAllThreadIds();
        // 获取每个线程的详细信息
        List<ThreadInfo> threadInfos = new java.util.ArrayList<>();
        for (long threadId : threadIds) {
            ThreadInfo threadInfo = threadMXBean.getThreadInfo(threadId);
            if (threadInfo != null) {
                threadInfos.add(threadInfo);
            }
        }
        return threadInfos;
    }
}
