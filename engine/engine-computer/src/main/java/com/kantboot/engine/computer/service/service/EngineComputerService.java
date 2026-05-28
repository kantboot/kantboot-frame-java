package com.kantboot.engine.computer.service.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kantboot.engine.computer.domain.entity.*;
import com.kantboot.engine.computer.service.IEngineComputerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
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

@Service
@Slf4j
public class EngineComputerService
    implements IEngineComputerService {

    @Override
    public EngineComputerSystem getSystemInfo() {
        Cache<String, EngineComputerSystem> cache = Caffeine.newBuilder()
                .expireAfterWrite(100, TimeUnit.DAYS)
                .maximumSize(1000)
                .build();
        // 尝试从缓存中获取系统信息
        EngineComputerSystem cachedSystemInfo = cache.getIfPresent("EngineComputer:systemInfo");
        if (cachedSystemInfo != null) {
            return cachedSystemInfo;
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
        cache.put("EngineComputer:systemInfo", system);
        return system;
    }

    @Override
    public List<EngineComputerRootDirectory> getRootDirectories() {
        List<EngineComputerRootDirectory> list = new ArrayList<>();
        // 获取所有硬盘
        Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
        for (Path root : rootDirectories) {
            // 获取容量
            try {
                File file = root.toFile();
                // 名称
                String name = root.toString();
                // 总空间
                long totalSpace = file.getTotalSpace();
                // 可用空间
                long freeSpace = file.getFreeSpace();
                // 可用空间（不包括系统保留空间）
                long usableSpace = file.getUsableSpace();
                // 已用
                long usedSpace = totalSpace - usableSpace;

                EngineComputerRootDirectory dir = new EngineComputerRootDirectory();
                dir.setName(name);
                dir.setTotalSpace(totalSpace);
                dir.setFreeSpace(freeSpace);
                dir.setUsableSpace(usableSpace);
                dir.setUsedSpace(usedSpace);

                list.add(dir);
            } catch (Exception e) {
                log.error("Error retrieving space for {}: {}", root, e.getMessage());
            }
        }
        return list;
    }

    @Override
    public EngineComputerPhysicalMemory getPhysicalMemory() {
        com.sun.management.OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean)
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
        oshi.hardware.CentralProcessor processor = hal.getProcessor();

        // 获取CPU使用率
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        try {
            Thread.sleep(1000); // 等待1秒获取差值
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        double cpuUsage = processor.getSystemCpuLoadBetweenTicks(prevTicks);

        EngineComputerCpu cpu = new EngineComputerCpu();
        cpu.setVendor(processor.getProcessorIdentifier().getVendor());
        cpu.setName(processor.getProcessorIdentifier().getName());
        cpu.setPhysicalPackageCount(processor.getPhysicalPackageCount());
        cpu.setPhysicalProcessorCount(processor.getPhysicalProcessorCount());
        cpu.setLogicalProcessorCount(processor.getLogicalProcessorCount());
        cpu.setUsage(cpuUsage);
        return cpu;
    }

    @Override
    public List<EngineComputerGpu> getGpus() {
        // 开始时间
        long startTime = System.currentTimeMillis();

        Cache<String, Object> cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .maximumSize(1000)
                .build();

        Object gpusCache = cache.getIfPresent("EngineComputer:gpus");
        if (gpusCache != null) {
            log.info("从缓存中获取GPU信息");
            return (List<EngineComputerGpu>) gpusCache;
        }

        List<EngineComputerGpu> gpus = new ArrayList<>();

        try {
            // TODO 暂时只获取英伟达
            Process process = Runtime.getRuntime().exec(
                    "nvidia-smi --query-gpu=name,temperature.gpu,fan.speed,utilization.gpu,memory.total,memory.used,power.draw --format=csv,noheader,nounits"
            );

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",\\s*");
                    if (data.length >= 7) {
                        EngineComputerGpu gpu = new EngineComputerGpu();
                        gpu.setName(parseString(data[0]));
                        gpu.setTemperature(parseInt(data[1]));
                        gpu.setFanSpeed(parseDouble(data[2])/100.0);
                        gpu.setGpuUsage(parseDouble(data[3])/100.0);
                        gpu.setVramTotal(parseLong(data[4])* 1024 * 1024);
                        gpu.setVramUsed(parseLong(data[5])* 1024 * 1024);
                        gpu.setPowerDraw(parseDouble(data[6]));
                        gpus.add(gpu);
                    }
                }
            }
        } catch (IOException e) {
            return gpus;
        } catch (Exception e) {
            log.error("Error retrieving GPU information: {}", e.getMessage());
            // 如果执行命令失败，返回空列表
            return gpus;
        }

        // 将获取到的GPU信息存入缓存
        cache.put("EngineComputer:gpus", gpus);
        // 结束时间
        long endTime = System.currentTimeMillis();
        log.info("获取GPU信息耗时: {} ms", (endTime - startTime));
        return gpus;
    }

    // 辅助方法：安全解析字段
    private String parseString(String value) {
        return value.trim().equals("[N/A]") ? "N/A" : value.trim();
    }

    private int parseInt(String value) {
        return value.trim().equals("[N/A]") ? 0 : Integer.parseInt(value.trim());
    }

    private double parseDouble(String value) {
        return value.trim().equals("[N/A]") ? 0.0 : Double.parseDouble(value.trim());
    }

    private long parseLong(String value) {
        return value.trim().equals("[N/A]") ? 0L : Long.parseLong(value.trim());
    }
    @Override
    public EngineComputer getInfo() {
        Cache<String, Object> cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .maximumSize(1000)
                .build();
        Object infoCache = cache.getIfPresent("EngineComputer:info");
        if (infoCache != null) {
            log.info("从缓存中获取计算机信息");
            return (EngineComputer) infoCache;
        }
        EngineComputer computer = new EngineComputer();
        computer.setSystem(getSystemInfo());
        computer.setRootDirectories(getRootDirectories());
        computer.setPhysicalMemory(getPhysicalMemory());
        computer.setCpu(getCpu());
        computer.setGpus(getGpus());

        // 将获取到的计算机信息存入缓存
        cache.put("EngineComputer:info", computer);

        return computer;
    }
}
