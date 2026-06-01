package com.kantboot.engine.docker.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class EngineDockerVolume implements Serializable {

    /**
     * 卷名称
     */
    private String name;

    /**
     * 卷驱动
     */
    private String driver;

    /**
     * 挂载点
     */
    private String mountpoint;

    /**
     * 创建时间
     */
    private String createdAt;

    /**
     * 状态
     */
    private Map<String, Object> status;

    /**
     * 标签
     */
    private Map<String, String> labels;

    /**
     * 作用域（如：local, global）
     */
    private String scope;

    /**
     * 选项
     */
    private Map<String, String> options;

    /**
     * 使用此卷的容器数量
     */
    private Integer usageData;

}
