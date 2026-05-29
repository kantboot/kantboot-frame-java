package com.kantboot.engine.docker.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class EngineDockerInfo implements Serializable {

    /**
     * Docker 守护进程 ID
     */
    private String id;

    /**
     * 容器数量
     */
    private Integer containers;

    /**
     * 运行中的容器数量
     */
    private Integer containersRunning;

    /**
     * 暂停的容器数量
     */
    private Integer containersPaused;

    /**
     * 停止的容器数量
     */
    private Integer containersStopped;

    /**
     * 镜像数量
     */
    private Integer images;

    /**
     * 驱动
     */
    private String driver;

    /**
     * 驱动状态
     */
    private Map<String, String> driverStatus;

    /**
     * Docker 根目录
     */
    private String dockerRootDir;

    /**
     * 是否启用调试
     */
    private Boolean debug;

    /**
     * 文件描述符数量
     */
    private Integer nfd;

    /**
     * Goroutine 数量
     */
    private Integer ngoroutines;

    /**
     * 系统时间
     */
    private String systemTime;

    /**
     * 日志驱动
     */
    private String loggingDriver;

    /**
     * Cgroup 驱动
     */
    private String cgroupDriver;

    /**
     * Cgroup 版本
     */
    private String cgroupVersion;

    /**
     * 内核版本
     */
    private String kernelVersion;

    /**
     * 操作系统
     */
    private String operatingSystem;

    /**
     * 操作系统类型
     */
    private String osType;

    /**
     * 架构
     */
    private String architecture;

    /**
     * CPU 数量
     */
    private Integer ncpu;

    /**
     * 内存总量
     */
    private Long memTotal;

    /**
     * 索引服务器地址
     */
    private String indexServerAddress;

    /**
     * 注册表配置
     */
    private Map<String, Object> registryConfig;

    /**
     * 通用标签
     */
    private Map<String, String> labels;

    /**
     * 实验性功能
     */
    private Boolean experimentalBuild;

    /**
     * 服务器版本
     */
    private String serverVersion;

    /**
     * 运行时列表
     */
    private List<String> runtimes;

    /**
     * 默认运行时
     */
    private String defaultRuntime;

    /**
     * 交换限制
     */
    private Boolean swapLimit;

    /**
     * 内存限制
     */
    private Boolean memoryLimit;

    /**
     * 内核内存限制
     */
    private Boolean kernelMemory;

    /**
     * 支持并发的下载数
     */
    private Integer httpProxy;

    /**
     * 支持并发的上传数
     */
    private Integer httpsProxy;

}
