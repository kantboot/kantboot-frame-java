package com.kantboot.engine.computer.service.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kantboot.engine.computer.domain.entity.EngineComputerProcess;
import com.kantboot.engine.computer.domain.entity.EngineComputerProcessThread;
import com.kantboot.engine.computer.service.IEngineComputerProcessService;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OSThread;
import oshi.software.os.OperatingSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class EngineComputerProcessServiceImpl
        implements IEngineComputerProcessService {


    private final static Cache<String, Object> CACHE = Caffeine.newBuilder()
            .expireAfterWrite(3, TimeUnit.SECONDS) // 写入后3秒过期
            .maximumSize(1000)
            .build();


    @Override
    public List<EngineComputerProcess> getList() {
        Object cacheObject = CACHE.getIfPresent("EngineComputerProcess:getList");
        if (cacheObject != null) {
            return (List<EngineComputerProcess>) CACHE.getIfPresent("EngineComputerProcess:getList");
        }

        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();

        // 获取所有进程（按 CPU 使用率排序）
        List<OSProcess> processes = os.getProcesses();

        List<EngineComputerProcess> result = new ArrayList<>();
        for (OSProcess p : processes) {
            EngineComputerProcess process = new EngineComputerProcess();
            process.setPid(p.getProcessID());
            process.setName(p.getName());
            process.setUser(p.getUser());
            process.setState(p.getState().name());
            process.setUserTime(p.getUserTime());
            process.setStartTime(p.getStartTime());
            process.setResidentSetSize(p.getResidentSetSize());
            process.setVirtualSize(p.getVirtualSize());
            process.setThreadCount(p.getThreadCount());

            result.add(process);
        }

        // 将结果存入缓存
        CACHE.put("EngineComputerProcess:getList", result);

        return result;
    }

    @Override
    public EngineComputerProcess getByPid(int pid) {
        Object cacheObject = CACHE.getIfPresent("EngineComputerProcess:getByPid:" + pid);
        if (cacheObject != null) {
            return (EngineComputerProcess) cacheObject;
        }

        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();

        // 获取指定 PID 的进程
        OSProcess p = os.getProcess(pid);
        if (p == null) {
            return null;
        }

        EngineComputerProcess process = new EngineComputerProcess();
        process.setPid(p.getProcessID());
        process.setName(p.getName());
        process.setUser(p.getUser());
        process.setState(p.getState().name());
        process.setUserTime(p.getUserTime());
        process.setStartTime(p.getStartTime());
        process.setCommandLine(p.getCommandLine());
        process.setResidentSetSize(p.getResidentSetSize());
        process.setVirtualSize(p.getVirtualSize());
        process.setKernelTime(p.getKernelTime());
        process.setThreadCount(p.getThreadCount());

        //        process.setCpuUsage();

        // 将结果存入缓存
        CACHE.put("EngineComputerProcess:getByPid:" + pid, process);

        return process;
    }

    @Override
    public boolean killByPid(int pid) {
        String os = System.getProperty("os.name").toLowerCase();
        String command;

        if (os.contains("win")) {
            command = "taskkill /F /PID " + pid;
        } else {
            command = "kill -9 " + pid;
        }

        try {
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<EngineComputerProcessThread> getThreadsByPid(int pid) {
        Cache<String, Object> cache = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
        Object cacheObject = cache.getIfPresent("EngineComputerProcess:getThreadsByPid:" + pid);
        if (cacheObject != null) {
            return (List<EngineComputerProcessThread>) cacheObject;
        }

        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();

        // 获取指定 PID 的进程
        OSProcess p = os.getProcess(pid);
        if (p == null) {
            return new ArrayList<>();
        }

        // 获取线程详情
        List<OSThread> threadDetails = p.getThreadDetails();
        List<EngineComputerProcessThread> threads = new ArrayList<>();
        for (OSThread threadDetail : threadDetails) {
            EngineComputerProcessThread thread = new EngineComputerProcessThread();
            thread.setThreadId(threadDetail.getThreadId());
            thread.setName(threadDetail.getName());
            thread.setContextSwitches(threadDetail.getContextSwitches());
            thread.setKernelTime(threadDetail.getKernelTime());
            thread.setUserTime(threadDetail.getUserTime());
            thread.setState(threadDetail.getState().name());
            thread.setThreadCpuLoadCumulative(threadDetail.getThreadCpuLoadCumulative());
            thread.setPriority(threadDetail.getPriority());
            thread.setStartTime(threadDetail.getStartTime());
            thread.setUpTime(threadDetail.getUpTime());
            thread.setOwningProcessId(threadDetail.getOwningProcessId());
            thread.setStartMemoryAddress(threadDetail.getStartMemoryAddress());
            thread.setMajorFaults(threadDetail.getMajorFaults());
            thread.setMinorFaults(threadDetail.getMinorFaults());
            threads.add(thread);
        }

        // 将结果存入缓存
        cache.put("EngineComputerProcess:getThreadsByPid:" + pid, threads);

        return threads;
    }
}
