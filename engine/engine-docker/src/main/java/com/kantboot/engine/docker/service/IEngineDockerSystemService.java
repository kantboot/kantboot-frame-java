package com.kantboot.engine.docker.service;

import com.kantboot.engine.docker.domain.entity.EngineDockerInfo;
import com.kantboot.engine.docker.domain.entity.EngineDockerSystemUsage;

public interface IEngineDockerSystemService {

    /**
     * 获取 Docker 系统信息
     * @return Docker 系统信息
     */
    EngineDockerInfo getInfo();

    /**
     * 获取 Docker 磁盘使用情况
     * @return 磁盘使用情况
     */
    EngineDockerSystemUsage getUsage();

    /**
     * 获取 Docker 版本信息
     * @return 版本信息
     */
    String getVersion();

    /**
     * 清理未使用的数据（容器、网络、镜像、卷）
     * @return 清理结果
     */
    String pruneAll();

}
