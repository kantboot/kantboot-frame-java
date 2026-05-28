package com.kantboot.engine.computer.service;

import com.kantboot.engine.computer.domain.entity.EngineComputerProcess;
import com.kantboot.engine.computer.domain.entity.EngineComputerProcessThread;

import java.util.List;

public interface IEngineComputerProcessService {

    /**
     * 获取list
     */
    List<EngineComputerProcess> getList();

    EngineComputerProcess getByPid(int pid);

    /**
     * 根据pid关闭进程
     */
    boolean killByPid(int pid);

    /**
     * 根据pid获取线程
     */
    List<EngineComputerProcessThread> getThreadsByPid(int pid);

}
