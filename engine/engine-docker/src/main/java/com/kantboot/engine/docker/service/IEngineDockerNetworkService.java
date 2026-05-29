package com.kantboot.engine.docker.service;

import com.kantboot.engine.docker.domain.entity.EngineDockerNetwork;

import java.util.List;
import java.util.Map;

public interface IEngineDockerNetworkService {

    /**
     * 获取网络列表
     * @return 网络列表
     */
    List<EngineDockerNetwork> list();

    /**
     * 获取网络详情
     * @param id 网络 ID
     * @return 网络详情
     */
    EngineDockerNetwork inspect(String id);

    /**
     * 创建网络
     * @param name 网络名称
     * @param driver 驱动类型
     * @param options 选项
     * @return 创建的网络
     */
    EngineDockerNetwork create(String name, String driver, Map<String, String> options);

    /**
     * 删除网络
     * @param id 网络 ID
     */
    void remove(String id);

    /**
     * 清理未使用的网络
     * @return 清理结果
     */
    List<String> prune();

}
