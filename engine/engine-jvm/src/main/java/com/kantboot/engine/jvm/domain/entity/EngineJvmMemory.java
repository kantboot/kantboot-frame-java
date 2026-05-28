package com.kantboot.engine.jvm.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * JVM内存信息
 */
@Data
public class EngineJvmMemory implements Serializable {

    /**
     * 最大内存（单位：字节）
     */
    private long maxMemory;

    /**
     * 已分配内存（单位：字节）
     */
    private long allocatedMemory;

    /**
     * 空闲内存（单位：字节）
     */
    private long freeMemory;

    /**
     * 已使用内存（单位：字节）
     */
    private long usedMemory;

    private EngineJvmHeapMemory heapMemory;

    private EngineJvmNonHeapMemory nonHeapMemory;

}
