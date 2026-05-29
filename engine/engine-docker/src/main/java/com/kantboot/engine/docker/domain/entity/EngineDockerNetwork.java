package com.kantboot.engine.docker.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class EngineDockerNetwork implements Serializable {

    /**
     * 网络名称
     */
    private String name;

    /**
     * 网络 ID
     */
    private String id;

    /**
     * 创建时间
     */
    private String created;

    /**
     * 作用域（如：local, global, swarm）
     */
    private String scope;

    /**
     * 驱动类型（如：bridge, host, overlay, macvlan）
     */
    private String driver;

    /**
     * 是否启用 IPv6
     */
    private Boolean enableIPv6;

    /**
     * 内部网络
     */
    private Boolean internal;

    /**
     * 可附加
     */
    private Boolean attachable;

    /**
     * 入口
     */
    private Boolean ingress;

    /**
     * 配置项
     */
    private Map<String, Object> options;

    /**
     * 标签
     */
    private Map<String, String> labels;

    /**
     * IPAM 配置
     */
    private Map<String, Object> ipam;

    /**
     * 容器连接信息
     */
    private Map<String, Object> containers;

}
