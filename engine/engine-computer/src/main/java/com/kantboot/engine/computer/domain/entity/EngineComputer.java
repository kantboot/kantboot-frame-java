package com.kantboot.engine.computer.domain.entity;

import lombok.Data;

import java.util.List;

@Data
public class EngineComputer {

    /**
     * 计算机系统信息
     */
    EngineComputerSystem system;

    /**
     * 根目录
     */
    List<EngineComputerRootDirectory> rootDirectories;

    /**
     * 物理内存
     */
    EngineComputerPhysicalMemory physicalMemory;

    /**
     * CPU
     */
    EngineComputerCpu cpu;

    /**
     * GPU
     */
    List<EngineComputerGpu> gpus;

}
