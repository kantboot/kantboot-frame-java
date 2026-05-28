package com.kantboot.engine.computer.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class EngineComputerRootDirectory implements Serializable {

    /**
     * 根目录名称
     */
    private String name;

    /**
     * 总空间
     */
    private long totalSpace;

    /**
     * 可用空间
     */
    private long freeSpace;

    /**
     * 可用空间（不包括系统保留空间）
     */
    private long usableSpace;

    /**
     * 已用
     */
    private long usedSpace;

}
