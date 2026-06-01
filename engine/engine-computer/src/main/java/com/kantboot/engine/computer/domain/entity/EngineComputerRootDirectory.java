package com.kantboot.engine.computer.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 计算机根目录（磁盘分区）信息
 * <p>
 * 注意：Linux 下 root 用户会保留一部分磁盘空间（默认 5%），
 * 这部分空间包含在 totalSpace 中，但不在 usableSpace 中。
 * 因此 usedSpace = totalSpace - freeSpace 才是真实的已用空间。
 */
@Data
public class EngineComputerRootDirectory implements Serializable {

    /**
     * 根目录名称，如 C:\ 或 /
     */
    private String name;

    /**
     * 总空间（字节）
     * <p>
     * 文件系统的总容量，包含系统保留空间
     */
    private long totalSpace;

    /**
     * 空闲空间（字节）
     * <p>
     * 文件系统中真正未使用的空间，包含系统保留部分
     */
    private long freeSpace;

    /**
     * 普通用户可用空间（字节）
     * <p>
     * 不包含系统为 root 保留的空间（Linux 默认保留 5%）
     */
    private long usableSpace;

    /**
     * 已用空间（字节）
     * <p>
     * 计算公式：totalSpace - freeSpace
     * 这是真实的已用空间，与 df 命令的 Used 列一致
     */
    private long usedSpace;

}
