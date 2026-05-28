package com.kantboot.engine.computer.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class EngineComputerCpu implements Serializable {

    /**
     * 制造商
     */
    private String vendor;

    /**
     * CPU名称
     */
    private String name;

    /**
     * 物理CPU数量
     */
    private int physicalPackageCount;

    /**
     * 物理CPU核心数
     */
    private int physicalProcessorCount;

    /**
     * 逻辑CPU数量
     */
    private int logicalProcessorCount;

    /**
     * 系统负载平均值
     */
    private double systemLoadAverage;

    /**
     * 使用率
     */
    private double usage;

}
