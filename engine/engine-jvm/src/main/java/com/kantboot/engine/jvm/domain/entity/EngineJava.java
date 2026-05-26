package com.kantboot.engine.jvm.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class EngineJava implements Serializable {

    /**
     * Java版本
     */
    private String version;

    /**
     * Java运行时版本
     */
    private String runtimeVersion;

    /**
     * Java虚拟机版本
     */
    private String vmVersion;

    /**
     * Java供应商
     */
    private String vendor;

    /**
     * Java安装目录
     */
    private String home;

    /**
     * Java类版本
     */
    private String classVersion;


}
