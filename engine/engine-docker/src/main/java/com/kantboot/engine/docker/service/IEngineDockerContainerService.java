package com.kantboot.engine.docker.service;

import com.kantboot.engine.docker.domain.entity.EngineDockerContainer;

import java.util.List;

public interface IEngineDockerContainerService {

    /**
     * 获取容器列表
     * @param all 是否包含已停止的容器
     * @return 容器列表
     */
    List<EngineDockerContainer> list(boolean all);

    /**
     * 获取容器详情
     * @param id 容器 ID
     * @return 容器详情
     */
    EngineDockerContainer inspect(String id);

    /**
     * 启动容器
     * @param id 容器 ID
     */
    void start(String id);

    /**
     * 停止容器
     * @param id 容器 ID
     */
    void stop(String id);

    /**
     * 重启容器
     * @param id 容器 ID
     */
    void restart(String id);

    /**
     * 暂停容器
     * @param id 容器 ID
     */
    void pause(String id);

    /**
     * 恢复容器
     * @param id 容器 ID
     */
    void unpause(String id);

    /**
     * 删除容器
     * @param id 容器 ID
     * @param force 是否强制删除
     * @param removeVolumes 是否删除关联卷
     */
    void remove(String id, boolean force, boolean removeVolumes);

    /**
     * 获取容器日志
     * @param id 容器 ID
     * @param tail 返回最后 N 行，0 表示全部
     * @return 日志内容
     */
    String logs(String id, int tail);

    /**
     * 在容器中执行命令
     * @param id 容器 ID
     * @param command 命令数组
     * @return 命令输出
     */
    String exec(String id, String[] command);

}
