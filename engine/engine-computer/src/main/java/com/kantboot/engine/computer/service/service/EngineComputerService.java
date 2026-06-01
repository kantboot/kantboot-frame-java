package com.kantboot.engine.computer.service.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kantboot.engine.computer.domain.entity.*;
import com.kantboot.engine.computer.service.IEngineComputerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.PhysicalMemory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 计算机信息服务实现
 * <p>
 * 提供本机硬件信息查询，包括系统、磁盘、内存、CPU、GPU 等。
 * 使用 OSHI 库获取底层信息，GPU 优先通过 nvidia-smi 获取详细数据，
 * 若不可用则退回到 OSHI 的 GraphicsCard 获取基础信息。
 */
@Service
@Slf4j
public class EngineComputerService implements IEngineComputerService {

    /**
     * 系统信息缓存
     * <p>
     * 系统信息（如操作系统版本、架构）在运行期间不会改变，
     * 因此使用长期缓存（100 天），避免重复创建 SystemInfo 对象。
     */
    private static final Cache<String, EngineComputerSystem> SYSTEM_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(100, TimeUnit.DAYS)
            .maximumSize(10)
            .build();

    /**
     * GPU 信息缓存
     * <p>
     * GPU 状态（温度、使用率、显存）变化较快，
     * 设置 10 秒过期，平衡实时性与性能。
     */
    private static final Cache<String, List<EngineComputerGpu>> GPU_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .maximumSize(10)
            .build();

    /**
     * 综合信息缓存
     * <p>
     * getInfo() 聚合了多个子查询，设置 10 秒过期。
     */
    private static final Cache<String, EngineComputer> INFO_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .maximumSize(10)
            .build();

    /**
     * CPU 使用率采样间隔（毫秒）
     * <p>
     * OSHI 需要通过两次采样计算 CPU 使用率，
     * 间隔 1 秒可以获得较准确的结果。
     */
    private static final long CPU_SAMPLE_INTERVAL_MS = 1000L;

    /**
     * CPU 使用率采样最大等待时间（毫秒）
     * <p>
     * 防止采样线程被意外中断后无限等待。
     */
    private static final long CPU_SAMPLE_TIMEOUT_MS = 2000L;

    @Override
    public EngineComputerSystem getSystemInfo() {
        // 先尝试从缓存获取，缓存 key 固定为 "system"
        EngineComputerSystem cached = SYSTEM_CACHE.getIfPresent("system");
        if (cached != null) {
            log.debug("系统信息命中缓存");
            return cached;
        }

        SystemInfo si = new SystemInfo();
        oshi.software.os.OperatingSystem os = si.getOperatingSystem();
        EngineComputerSystem system = new EngineComputerSystem();
        system.setFamily(os.getFamily());
        system.setManufacturer(os.getManufacturer());
        system.setVersion(os.getVersionInfo().getVersion());
        system.setVersionBuildNumber(os.getVersionInfo().getBuildNumber());
        system.setVersionCodeName(os.getVersionInfo().getCodeName());
        system.setBitness(os.getBitness());
        system.setBootTime(os.getSystemBootTime());
        system.setProcessCount(os.getProcessCount());

        SYSTEM_CACHE.put("system", system);
        return system;
    }

    @Override
    public List<EngineComputerRootDirectory> getRootDirectories() {
        List<EngineComputerRootDirectory> list = new ArrayList<>();
        Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
        for (Path root : rootDirectories) {
            try {
                File file = root.toFile();
                String name = root.toString();
                long totalSpace = file.getTotalSpace();
                long freeSpace = file.getFreeSpace();
                long usableSpace = file.getUsableSpace();
                // 真实的已用空间 = 总空间 - 空闲空间
                // 注意：不是 totalSpace - usableSpace，后者会把 root 保留空间也算进已用
                long usedSpace = totalSpace - freeSpace;

                EngineComputerRootDirectory dir = new EngineComputerRootDirectory();
                dir.setName(name);
                dir.setTotalSpace(totalSpace);
                dir.setFreeSpace(freeSpace);
                dir.setUsableSpace(usableSpace);
                dir.setUsedSpace(usedSpace);

                list.add(dir);
            } catch (Exception e) {
                log.error("获取磁盘 [{}] 空间信息失败: {}", root, e.getMessage());
            }
        }
        return list;
    }

    @Override
    public EngineComputerPhysicalMemory getPhysicalMemory() {
        com.sun.management.OperatingSystemMXBean osBean =
                (com.sun.management.OperatingSystemMXBean)
                        java.lang.management.ManagementFactory.getOperatingSystemMXBean();
        long totalPhysicalMemory = osBean.getTotalMemorySize();
        long freePhysicalMemory = osBean.getFreeMemorySize();
        long usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory;

        EngineComputerPhysicalMemory memory = new EngineComputerPhysicalMemory();
        memory.setTotalPhysicalMemory(totalPhysicalMemory);
        memory.setFreePhysicalMemory(freePhysicalMemory);
        memory.setUsedPhysicalMemory(usedPhysicalMemory);
        memory.setItems(getPhysicalMemoryItem());
        return memory;
    }

    @Override
    public List<EngineComputerPhysicalMemoryItem> getPhysicalMemoryItem() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        List<PhysicalMemory> memoryList = hal.getMemory().getPhysicalMemory();

        // 虚拟机或某些 ARM 设备可能无法获取物理内存条信息
        if (memoryList == null || memoryList.isEmpty()) {
            log.warn("无法获取物理内存条信息，当前环境可能为虚拟机或容器");
            return new ArrayList<>();
        }

        List<EngineComputerPhysicalMemoryItem> result = new ArrayList<>();
        for (PhysicalMemory memory : memoryList) {
            EngineComputerPhysicalMemoryItem item = new EngineComputerPhysicalMemoryItem();
            item.setManufacturer(memory.getManufacturer());
            item.setModel(memory.getMemoryType());
            item.setCapacity(memory.getCapacity()); // 字节单位
            item.setClockSpeed(memory.getClockSpeed()); // 频率（MHz）
            item.setBankLabel(memory.getBankLabel()); // 内存插槽标签（如 "DIMM 0"）
            result.add(item);
        }
        return result;
    }

    @Override
    public EngineComputerCpu getCpu() {
        oshi.SystemInfo si = new oshi.SystemInfo();
        oshi.hardware.HardwareAbstractionLayer hal = si.getHardware();
        CentralProcessor processor = hal.getProcessor();

        EngineComputerCpu cpu = new EngineComputerCpu();
        cpu.setVendor(processor.getProcessorIdentifier().getVendor());
        cpu.setName(processor.getProcessorIdentifier().getName());
        cpu.setPhysicalPackageCount(processor.getPhysicalPackageCount());
        cpu.setPhysicalProcessorCount(processor.getPhysicalProcessorCount());
        cpu.setLogicalProcessorCount(processor.getLogicalProcessorCount());

        // 采样计算 CPU 使用率
        double cpuUsage = sampleCpuUsage(processor);
        cpu.setUsage(cpuUsage);
        return cpu;
    }

    /**
     * 采样计算 CPU 使用率
     * <p>
     * OSHI 的 getSystemCpuLoadBetweenTicks 需要两次采样间隔才能计算差值。
     * 此方法会阻塞当前线程约 1 秒，如果采样失败返回 -1 作为标记。
     *
     * @param processor OSHI 的 CentralProcessor 实例
     * @return CPU 使用率（0.0 ~ 1.0），采样失败返回 -1.0
     */
    private double sampleCpuUsage(CentralProcessor processor) {
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        try {
            Thread.sleep(CPU_SAMPLE_INTERVAL_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("CPU 使用率采样被中断");
            return -1.0;
        }
        double cpuUsage = processor.getSystemCpuLoadBetweenTicks(prevTicks);
        // OSHI 在某些平台可能返回负数表示不可用
        if (cpuUsage < 0) {
            log.warn("CPU 使用率采样返回无效值: {}", cpuUsage);
            return -1.0;
        }
        return cpuUsage;
    }

    @Override
    public List<EngineComputerGpu> getGpus() {
        long startTime = System.currentTimeMillis();

        // 先尝试缓存
        List<EngineComputerGpu> cached = GPU_CACHE.getIfPresent("gpus");
        if (cached != null) {
            log.debug("GPU 信息命中缓存");
            return cached;
        }

        List<EngineComputerGpu> gpus = new ArrayList<>();

        // 第一优先级：nvidia-smi（NVIDIA 显卡，信息最丰富）
        boolean nvidiaSuccess = tryFetchNvidiaGpus(gpus);

        // 第二优先级：OSHI GraphicsCard（兜底，支持所有厂商但信息较少）
        if (!nvidiaSuccess || gpus.isEmpty()) {
            tryFetchOshiGpus(gpus);
        }

        GPU_CACHE.put("gpus", gpus);
        log.info("获取 GPU 信息耗时: {} ms, 数量: {}", System.currentTimeMillis() - startTime, gpus.size());
        return gpus;
    }

    /**
     * 通过 nvidia-smi 命令获取 NVIDIA GPU 详细信息
     * <p>
     * nvidia-smi 提供温度、风扇转速、使用率、显存、功耗等丰富信息，
     * 但仅当系统安装了 NVIDIA 驱动且存在 NVIDIA GPU 时才可用。
     *
     * @param gpus 结果收集列表
     * @return 是否成功获取到至少一个 GPU
     */
    private boolean tryFetchNvidiaGpus(List<EngineComputerGpu> gpus) {
        try {
            Process process = Runtime.getRuntime().exec(
                    "nvidia-smi --query-gpu=name,temperature.gpu,fan.speed,utilization.gpu,memory.total,memory.used,power.draw --format=csv,noheader,nounits"
            );

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",\\s*");
                    if (data.length >= 7) {
                        EngineComputerGpu gpu = new EngineComputerGpu();
                        gpu.setName(parseString(data[0]));
                        gpu.setVendor("NVIDIA");
                        gpu.setTemperature(parseInt(data[1]));
                        gpu.setFanSpeed(parseDouble(data[2]) / 100.0);
                        gpu.setGpuUsage(parseDouble(data[3]) / 100.0);
                        gpu.setVramTotal(parseLong(data[4]) * 1024L * 1024L);
                        gpu.setVramUsed(parseLong(data[5]) * 1024L * 1024L);
                        gpu.setPowerDraw(parseDouble(data[6]));
                        gpus.add(gpu);
                    }
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                log.warn("nvidia-smi 退出码非零: {}", exitCode);
                return false;
            }
            return !gpus.isEmpty();
        } catch (IOException e) {
            log.info("nvidia-smi 不可用（非 NVIDIA 环境或驱动未安装）: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("nvidia-smi 执行异常: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 通过 OSHI 获取显卡基础信息（兜底方案）
     * <p>
     * OSHI 的 GraphicsCard 可以获取所有厂商的显卡名称和显存，
     * 但无法获取温度、使用率、功耗等动态信息。
     * 当 nvidia-smi 不可用时，至少能让用户看到显卡列表。
     *
     * @param gpus 结果收集列表
     */
    private void tryFetchOshiGpus(List<EngineComputerGpu> gpus) {
        try {
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();
            List<GraphicsCard> cards = hal.getGraphicsCards();

            if (cards == null || cards.isEmpty()) {
                log.info("OSHI 未检测到显卡");
                return;
            }

            for (GraphicsCard card : cards) {
                EngineComputerGpu gpu = new EngineComputerGpu();
                gpu.setName(card.getName());
                // 从名称中推测厂商
                gpu.setVendor(guessVendor(card.getName()));
                gpu.setVramTotal(card.getVRam());
                // OSHI 无法提供动态信息，标记为未知
                gpu.setTemperature(-1);
                gpu.setFanSpeed(-1.0);
                gpu.setGpuUsage(-1.0);
                gpu.setPowerDraw(-1.0);
                gpus.add(gpu);
            }
        } catch (Exception e) {
            log.error("OSHI 获取显卡信息失败: {}", e.getMessage());
        }
    }

    /**
     * 根据显卡名称推测厂商
     *
     * @param name 显卡名称
     * @return 厂商名称，无法识别返回 "Unknown"
     */
    private String guessVendor(String name) {
        String upper = name.toUpperCase();
        if (upper.contains("NVIDIA") || upper.contains("GEFORCE") || upper.contains("RTX") || upper.contains("GTX")) {
            return "NVIDIA";
        }
        if (upper.contains("AMD") || upper.contains("RADEON") || upper.contains("ATI")) {
            return "AMD";
        }
        if (upper.contains("INTEL") || upper.contains("ARC") || upper.contains("IRIS") || upper.contains("UHD")) {
            return "Intel";
        }
        if (upper.contains("APPLE") || upper.contains("M1") || upper.contains("M2") || upper.contains("M3")) {
            return "Apple";
        }
        return "Unknown";
    }

    @Override
    public EngineComputer getInfo() {
        EngineComputer cached = INFO_CACHE.getIfPresent("info");
        if (cached != null) {
            log.debug("综合信息命中缓存");
            return cached;
        }

        EngineComputer computer = new EngineComputer();

        // 每个子项独立 try-catch，避免单点失败导致整体不可用
        computer.setSystem(safeGet(this::getSystemInfo, "系统信息"));
        computer.setRootDirectories(safeGet(this::getRootDirectories, "磁盘信息"));
        computer.setPhysicalMemory(safeGet(this::getPhysicalMemory, "内存信息"));
        computer.setCpu(safeGet(this::getCpu, "CPU信息"));
        computer.setGpus(safeGet(this::getGpus, "GPU信息"));

        INFO_CACHE.put("info", computer);
        return computer;
    }

    /**
     * 安全执行信息获取，单个子项失败不影响其他子项
     * <p>
     * getInfo() 聚合了多个底层查询，任何一个抛异常都不应该导致整个接口挂掉。
     * 此方法将异常捕获并记录日志，返回 null 让调用方自行处理。
     *
     * @param supplier 信息获取函数
     * @param desc     信息描述，用于日志
     * @param <T>      返回类型
     * @return 获取成功返回结果，失败返回 null
     */
    private <T> T safeGet(java.util.function.Supplier<T> supplier, String desc) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.error("获取{}失败: {}", desc, e.getMessage(), e);
            return null;
        }
    }

    // ==================== 辅助方法 ====================

    /**
     * 安全解析字符串字段，处理 nvidia-smi 返回的 [N/A]
     */
    private String parseString(String value) {
        String trimmed = value.trim();
        return "[N/A]".equals(trimmed) ? "N/A" : trimmed;
    }

    /**
     * 安全解析整数字段，处理 nvidia-smi 返回的 [N/A]
     */
    private int parseInt(String value) {
        String trimmed = value.trim();
        return "[N/A]".equals(trimmed) ? 0 : Integer.parseInt(trimmed);
    }

    /**
     * 安全解析双精度字段，处理 nvidia-smi 返回的 [N/A]
     */
    private double parseDouble(String value) {
        String trimmed = value.trim();
        return "[N/A]".equals(trimmed) ? 0.0 : Double.parseDouble(trimmed);
    }

    /**
     * 安全解析长整型字段，处理 nvidia-smi 返回的 [N/A]
     */
    private long parseLong(String value) {
        String trimmed = value.trim();
        return "[N/A]".equals(trimmed) ? 0L : Long.parseLong(trimmed);
    }

}
