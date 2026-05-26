package com.kantboot.engine.computer.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class EngineComputerSystem implements Serializable {

    /**
     * 系统家族（如 "Windows"）
     */
    private String family;

    /**
     * 制造商（如 "Microsoft"）
     */
    private String manufacturer;

    /**
     * 详细版本信息（含构建号）
     */
    private String version;

    /**
     * 版本代号
     */
    private String versionCodeName;

    /**
     * 构建号
     */
    private String versionBuildNumber;

    /**
     * 位数（32/64）
     */
    private int bitness;

    /**
     * 系统启动时间（Unix 时间戳）
     */
    private long bootTime;

    /**
     * 当前进程数
     */
    private int processCount;


}
