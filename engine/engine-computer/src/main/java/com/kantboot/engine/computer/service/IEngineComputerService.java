package com.kantboot.engine.computer.service;

import com.kantboot.engine.computer.domain.entity.*;

import java.util.List;

public interface IEngineComputerService {

    /**
     * 获取计算机系统信息
     * @return 计算机系统信息
     */
    EngineComputerSystem getSystemInfo();

    /**
     * 获取所有根目录的信息
     * @return 根目录列表
     */
    List<EngineComputerRootDirectory> getRootDirectories();

    /**
     * 获取物理内存信息
     */
     EngineComputerPhysicalMemory getPhysicalMemory();

    /**
     * 获取物理内存条信息
     */
    List<EngineComputerPhysicalMemoryItem> getPhysicalMemoryItem();

    /**
      * 获取CPU信息
      */
    EngineComputerCpu getCpu();

    /**
     * 获取GPU信息
     */
    List<EngineComputerGpu> getGpus();

    /**
     * 获取计算机信息
     */
     EngineComputer getInfo();

}
