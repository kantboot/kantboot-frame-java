package com.kantboot.engine.computer.domain.entity;

import lombok.Data;

@Data
public class EngineComputerGpu {
    /**
     * GPU 名称（如 "NVIDIA RTX 3080"）
     */
    private String name;
    /**
     * 厂商
     */
    private String vendor;
    /**
     * 驱动版本
     */
    private String driverVersion;
    /**
     * 显存总量（字节）
     */
    private long vramTotal;

    /**
     * 已用显存（字节）
     */
    private long vramUsed;
    /**
     * GPU 使用率（%）
     */
    private double gpuUsage;
    /**
     * 核心温度（℃）
     */
    private int temperature;
    /**
     * 风扇转速（%）
     */
    private double fanSpeed;
    /**
     * 功耗（W）
     */
    private double powerDraw;
}