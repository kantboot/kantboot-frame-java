package com.kantboot.engine.computer.service.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.kantboot.engine.computer.domain.entity.EngineComputerProcess;
import com.kantboot.engine.computer.domain.entity.EngineComputerRootDirectory;
import com.kantboot.engine.computer.service.IEngineComputerProcessService;
import com.kantboot.engine.computer.service.IEngineComputerService;
import com.kantboot.test.application.TestApplication;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OSThread;
import oshi.software.os.OperatingSystem;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = TestApplication.class)
public class TestEngineComputerService {

    @Resource
    private IEngineComputerService service;

    @Resource
    private IEngineComputerProcessService processService;

    @Test
    public void testGetRootDirectories() {
        List<EngineComputerRootDirectory> rootDirectories = service.getRootDirectories();
        System.out.println(JSON.toJSONString(rootDirectories));
    }

    @Test
    public void testGetPhysicalMemory() {
        System.out.println(JSON.toJSONString(service.getPhysicalMemory()));
    }

    @Test
    public void testGetCpu() {
        System.out.println(JSON.toJSONString(service.getCpu()));
    }

    @Test
    public void testGetInfo() {
        System.out.println(JSON.toJSONString(service.getInfo()));
    }

    @Test
    public void testGetGpus() {
        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();
        System.out.println(JSON.toJSONString(service.getGpus()));
    }

    @Test
    public void testGetSystem() {
        // 开始事件
        long startTime = System.currentTimeMillis();

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
            process.setCpuUsage(p.getProcessCpuLoadCumulative());
            process.setUserTime(p.getUserTime());
            process.setStartTime(p.getStartTime());
            process.setVirtualSize(p.getVirtualSize());
            process.setResidentSetSize(p.getResidentSetSize());
            //            process.setCommandLine(p.getCommandLine());

//            process.setVirtualSize(p.getVirtualSize());
//            process.setResidentSetSize(p.getResidentSetSize());
//            process.setKernelTime(p.getKernelTime());
//            process.setThreadCount(p.getThreadCount());
            // 获取线程详情 p.getThreadDetails()
            result.add(process);
        }
        // 结束事件
        long endTime = System.currentTimeMillis();
        System.err.println(JSON.toJSONString(result, JSONWriter.Feature.PrettyFormat));
        System.err.println("Total processes: " + result.size());
        System.err.println("Time taken: " + (endTime - startTime) + " ms");
    }

    // 根据pid获取
    @Test
    public void testGetProcessByPid() {
        int pid = 956;
        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();

        // 获取指定 PID 的进程
        OSProcess p = os.getProcess(pid);
        if (p == null) {
            System.err.println("Process with PID " + pid + " not found.");
            return;
        }

        // 开始时间
        long startTime = System.currentTimeMillis();
        List<OSThread> threadDetails = p.getThreadDetails();
        for (OSThread threadDetail : threadDetails) {
            System.err.println("Thread ID: " + threadDetail.getThreadId());
//            System.out.println("Thread User Time: " + threadDetail.getUserTime());
//            System.out.println("Thread Kernel Time: " + threadDetail.getKernelTime());
//
//            中文
//            System.out.println("线程ID: " + threadDetail.getThreadId());
//            System.out.println("线程用户时间: " + threadDetail.getUserTime());
//            System.out.println("线程内核时间: " + threadDetail.getKernelTime());
//            System.out.println("线程状态: " + threadDetail.getState());
//            System.out.println("线程优先级: " + threadDetail.getPriority());
//            System.out.println("线程名称: " + threadDetail.getName());
        }
        // 结束时间
        long endTime = System.currentTimeMillis();
        System.err.println("Time taken to get process details: " + (endTime - startTime) + " ms");
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
