package com.kantboot.engine.computer.service.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kantboot.engine.computer.domain.entity.EngineComputerProcess;
import com.kantboot.engine.computer.domain.entity.EngineComputerProcessThread;
import com.kantboot.engine.computer.service.IEngineComputerProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OSThread;
import oshi.software.os.OperatingSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 计算机进程信息服务实现
 * <p>
 * 提供进程列表查询、进程详情、线程详情、进程终止等功能。
 * 使用 OSHI 库获取进程信息，CPU 使用率需要采样间隔计算。
 */
@Service
@Slf4j
public class EngineComputerProcessServiceImpl implements IEngineComputerProcessService {

    /**
     * 进程信息缓存
     * <p>
     * 进程状态变化较快，设置 3 秒过期，避免频繁扫描系统进程表。
     */
    private static final Cache<String, Object> CACHE = Caffeine.newBuilder()
            .expireAfterWrite(3, TimeUnit.SECONDS)
            .maximumSize(1000)
            .build();

    /**
     * CPU 使用率采样间隔（毫秒）
     * <p>
     * OSHI 的 getProcessCpuLoadBetweenTicks 需要两次采样间隔，
     * 间隔 1 秒可以获得较准确的结果。
     */
    private static final long CPU_SAMPLE_INTERVAL_MS = 1000L;

    /**
     * 进程终止命令超时时间（毫秒）
     * <p>
     * kill / taskkill 命令如果卡住，超过此时间强制返回失败。
     */
    private static final long KILL_TIMEOUT_MS = 5000L;

    @Override
    public List<EngineComputerProcess> getList() {
        Object cacheObject = CACHE.getIfPresent("EngineComputerProcess:getList");
        if (cacheObject != null) {
            return (List<EngineComputerProcess>) cacheObject;
        }

        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();
        List<OSProcess> processes = os.getProcesses();

        List<EngineComputerProcess> result = new ArrayList<>();
        for (OSProcess p : processes) {
            EngineComputerProcess process = convert(p);
            result.add(process);
        }

        CACHE.put("EngineComputerProcess:getList", result);
        return result;
    }

    @Override
    public EngineComputerProcess getByPid(int pid) {
        String cacheKey = "EngineComputerProcess:getByPid:" + pid;
        Object cacheObject = CACHE.getIfPresent(cacheKey);
        if (cacheObject != null) {
            return (EngineComputerProcess) cacheObject;
        }

        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();
        OSProcess p = os.getProcess(pid);
        if (p == null) {
            log.warn("未找到 PID 为 {} 的进程", pid);
            return null;
        }

        EngineComputerProcess process = convertWithCpu(p);
        process.setCommandLine(p.getCommandLine());
        process.setKernelTime(p.getKernelTime());

        CACHE.put(cacheKey, process);
        return process;
    }

    @Override
    public boolean killByPid(int pid) {
        String osName = System.getProperty("os.name").toLowerCase();

        // 先尝试优雅终止（SIGTERM / taskkill 不加 /F）
        boolean graceful = gracefulKill(pid, osName);
        if (graceful) {
            log.info("进程 {} 已优雅终止", pid);
            return true;
        }

        // 优雅终止失败，强制终止（SIGKILL / taskkill /F）
        log.warn("进程 {} 优雅终止失败，尝试强制终止", pid);
        return forceKill(pid, osName);
    }

    /**
     * 优雅终止进程
     * <p>
     * Linux: 发送 SIGTERM (kill -15)，让进程有机会清理资源后退出。
     * Windows: 使用 taskkill 不加 /F 参数，请求进程正常退出。
     *
     * @param pid    进程 ID
     * @param osName 操作系统名称（小写）
     * @return 是否成功
     */
    private boolean gracefulKill(int pid, String osName) {
        String command = osName.contains("win")
                ? "taskkill /PID " + pid
                : "kill -15 " + pid;
        return executeKillCommand(command);
    }

    /**
     * 强制终止进程
     * <p>
     * Linux: 发送 SIGKILL (kill -9)，进程无法捕获，立即终止。
     * Windows: 使用 taskkill /F 强制终止。
     * <p>
     * 注意：强制终止可能导致数据丢失或资源未释放，应作为最后手段。
     *
     * @param pid    进程 ID
     * @param osName 操作系统名称（小写）
     * @return 是否成功
     */
    private boolean forceKill(int pid, String osName) {
        String command = osName.contains("win")
                ? "taskkill /F /PID " + pid
                : "kill -9 " + pid;
        return executeKillCommand(command);
    }

    /**
     * 执行终止命令，带超时控制
     * <p>
     * 使用 Process.waitFor(long, TimeUnit) 防止命令无限阻塞。
     *
     * @param command 终止命令
     * @return 命令正常退出且退出码为 0 返回 true
     */
    private boolean executeKillCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            boolean finished = process.waitFor(KILL_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            if (!finished) {
                log.error("终止命令超时: {}", command);
                process.destroyForcibly();
                return false;
            }
            int exitCode = process.exitValue();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            log.error("执行终止命令失败 [{}]: {}", command, e.getMessage());
            return false;
        }
    }

    @Override
    public List<EngineComputerProcessThread> getThreadsByPid(int pid) {
        Cache<String, Object> cache = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
        String cacheKey = "EngineComputerProcess:getThreadsByPid:" + pid;
        Object cacheObject = cache.getIfPresent(cacheKey);
        if (cacheObject != null) {
            return (List<EngineComputerProcessThread>) cacheObject;
        }

        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();
        OSProcess p = os.getProcess(pid);
        if (p == null) {
            log.warn("未找到 PID 为 {} 的进程，无法获取线程", pid);
            return new ArrayList<>();
        }

        List<OSThread> threadDetails = p.getThreadDetails();
        if (threadDetails == null || threadDetails.isEmpty()) {
            log.warn("进程 {} 没有线程信息", pid);
            return new ArrayList<>();
        }

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

        cache.put(cacheKey, threads);
        return threads;
    }

    // ==================== 转换方法 ====================

    /**
     * 将 OSHI 的 OSProcess 转换为 EngineComputerProcess（基础信息）
     * <p>
     * 不包含 CPU 使用率，因为 CPU 使用率需要采样间隔，
     * 批量转换时逐个采样会导致总耗时过长。
     */
    private EngineComputerProcess convert(OSProcess p) {
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
        // CPU 使用率需要采样，列表查询不计算
        process.setCpuUsage(-1.0);
        return process;
    }

    /**
     * 将 OSHI 的 OSProcess 转换为 EngineComputerProcess（含 CPU 使用率）
     * <p>
     * 通过采样间隔计算进程的 CPU 使用率，
     * 此方法会阻塞约 1 秒，仅用于单个进程详情查询。
     *
     * @param p OSHI 进程对象
     * @return 包含 CPU 使用率的进程信息
     */
    private EngineComputerProcess convertWithCpu(OSProcess p) {
        EngineComputerProcess process = convert(p);

        // 采样计算进程 CPU 使用率
        long[] prevKernelTicks = p.getKernelTicks();
        long[] prevUserTicks = p.getUserTicks();
        try {
            Thread.sleep(CPU_SAMPLE_INTERVAL_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("进程 CPU 采样被中断，PID: {}", p.getProcessID());
            process.setCpuUsage(-1.0);
            return process;
        }

        // OSHI 3.x 通过 getProcessCpuLoadBetweenTicks 计算
        // 注意：需要先更新进程信息（updateAttributes）才能获取新的 tick 值
        p.updateAttributes();
        double cpuUsage = p.getProcessCpuLoadBetweenTicks(prevKernelTicks, prevUserTicks);
        if (cpuUsage < 0) {
            log.warn("进程 {} CPU 采样返回无效值: {}", p.getProcessID(), cpuUsage);
            process.setCpuUsage(-1.0);
        } else {
            process.setCpuUsage(cpuUsage);
        }
        return process;
    }

}
