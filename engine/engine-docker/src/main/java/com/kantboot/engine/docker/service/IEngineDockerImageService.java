package com.kantboot.engine.docker.service;

import com.kantboot.engine.docker.domain.entity.EngineDockerImage;

import java.util.List;

public interface IEngineDockerImageService {

    /**
     * 获取镜像列表
     * @return 镜像列表
     */
    List<EngineDockerImage> list();

    /**
     * 获取镜像详情
     * @param id 镜像 ID
     * @return 镜像详情
     */
    EngineDockerImage inspect(String id);

    /**
     * 删除镜像
     * @param id 镜像 ID
     * @param force 是否强制删除
     * @param noPrune 是否不删除未标记的父镜像
     */
    void remove(String id, boolean force, boolean noPrune);

    /**
     * 拉取镜像
     * @param repository 仓库名称（如：nginx）
     * @param tag 标签（如：latest）
     */
    void pull(String repository, String tag);

    /**
     * 清理未使用的镜像
     * @return 清理结果
     */
    List<String> prune();

}
