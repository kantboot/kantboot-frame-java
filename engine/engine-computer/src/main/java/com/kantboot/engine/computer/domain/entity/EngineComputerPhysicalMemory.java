package com.kantboot.engine.computer.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EngineComputerPhysicalMemory
        implements Serializable {

    /**
     * 总物理内存，单位字节
     */
    private long totalPhysicalMemory;

    /**
     * 空闲物理内存，单位字节
     */
    private long freePhysicalMemory;

    /**
     * 已用物理内存，单位字节
     */
    private long usedPhysicalMemory;

    private List<EngineComputerPhysicalMemoryItem> items;


}
