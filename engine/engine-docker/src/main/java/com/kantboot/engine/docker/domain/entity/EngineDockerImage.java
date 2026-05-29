package com.kantboot.engine.docker.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class EngineDockerImage implements Serializable {

    /**
     * 镜像 ID
     */
    private String id;

    /**
     * 父镜像 ID
     */
    private String parentId;

    /**
     * 仓库标签列表（如：["nginx:latest", "nginx:1.25"]）
     */
    private List<String> repoTags;

    /**
     * 仓库摘要列表
     */
    private List<String> repoDigests;

    /**
     * 镜像创建时间
     */
    private Long created;

    /**
     * 镜像大小（字节）
     */
    private Long size;

    /**
     * 共享大小
     */
    private Long sharedSize;

    /**
     * 虚拟大小
     */
    private Long virtualSize;

    /**
     * 标签
     */
    private Map<String, String> labels;

    /**
     * 容器数量
     */
    private Integer containers;

    /**
     * 配置信息
     */
    private Map<String, Object> config;

    /**
     * 架构
     */
    private String architecture;

    /**
     * 操作系统
     */
    private String os;

}
