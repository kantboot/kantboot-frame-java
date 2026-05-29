package com.kantboot.engine.docker.service;

import com.kantboot.engine.docker.domain.entity.EngineDockerVolume;

import java.util.List;
import java.util.Map;

public interface IEngineDockerVolumeService {

    /**
     * 获取卷列表
     * @return 卷列表
     */
    List<EngineDockerVolume> list();

    /**
     * 获取卷详情
     * @param name 卷名称
     * @return 卷详情
     */
    EngineDockerVolume inspect(String name);

    /**
     * 创建卷
     * @param name 卷名称
     * @param driver 驱动
     * @param driverOpts 驱动选项
     * @param labels 标签
     * @return 创建的卷
     */
    EngineDockerVolume create(String name, String driver, Map<String, String> driverOpts, Map<String, String> labels);

    /**
     * 删除卷
     * @param name 卷名称
     * @param force 是否强制删除
     */
    void remove(String name, boolean force);

    /**
     * 清理未使用的卷
     * @return 清理结果
     */
    List<String> prune();

}
