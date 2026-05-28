package com.kantboot.engine.jvm.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * JVM堆内存信息
 */
@Data
public class EngineJvmHeapMemory
        implements Serializable {

    /**
     * 初始化堆内存（单位：字节）
     */
    private long initMemory;

    /**
     * 最大堆内存（单位：字节）
     */
    private long maxMemory;

    /**
     * 已使用堆内存（单位：字节）
     */
    private long usedMemory;

    /**
     * 提交大小
     */
    private long committedMemory;

}
