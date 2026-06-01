package com.kantboot.engine.docker.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class EngineDockerContainer implements Serializable {

    /**
     * 容器 ID
     */
    private String id;

    /**
     * 容器名称
     */
    private List<String> names;

    /**
     * 镜像名称
     */
    private String image;

    /**
     * 镜像 ID
     */
    private String imageId;

    /**
     * 容器启动命令
     */
    private String command;

    /**
     * 容器创建时间
     */
    private Long created;

    /**
     * 容器状态（如：created, restarting, running, removing, paused, exited, dead）
     */
    private String state;

    /**
     * 状态描述（如：Up 2 hours）
     */
    private String status;

    /**
     * 端口映射
     */
    private List<Port> ports;

    /**
     * 标签
     */
    private Map<String, String> labels;

    /**
     * 容器大小
     */
    private Long sizeRw;

    /**
     * 根文件系统大小
     */
    private Long sizeRootFs;

    /**
     * 挂载点
     */
    private List<Mount> mounts;

    /**
     * 网络模式
     */
    private String hostConfig;

    /**
     * 网络设置
     */
    private Map<String, Object> networkSettings;

    @Data
    public static class Port implements Serializable {
        private String ip;
        private Integer privatePort;
        private Integer publicPort;
        private String type;
    }

    @Data
    public static class Mount implements Serializable {
        private String type;
        private String name;
        private String source;
        private String destination;
        private String driver;
        private String mode;
        private Boolean rw;
        private String propagation;
    }

}
