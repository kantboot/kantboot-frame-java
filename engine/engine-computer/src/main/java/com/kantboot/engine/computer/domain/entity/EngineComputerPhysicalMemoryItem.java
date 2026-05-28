package com.kantboot.engine.computer.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class EngineComputerPhysicalMemoryItem
        implements Serializable {

    /**
     * 制造商
     */
    private String manufacturer;

    /**
     * 物理内存名称
     */
    private String model;

    /**
     * 物理内存类型
     */
    private long capacity;

    /**
     * 物理内存速度，单位MHz
     */
    private long clockSpeed;

    /**
     * 物理内存标签
     */
    private String bankLabel;

}
