package com.kantboot.engine.docker.service.impl;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.PruneType;
import com.kantboot.engine.docker.domain.entity.EngineDockerInfo;
import com.kantboot.engine.docker.domain.entity.EngineDockerSystemUsage;
import com.kantboot.engine.docker.service.IEngineDockerSystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class EngineDockerSystemServiceImpl implements IEngineDockerSystemService {

    @Resource
    private DockerClient dockerClient;

    @Override
    public EngineDockerInfo getInfo() {
        var info = dockerClient.infoCmd().exec();
        EngineDockerInfo result = new EngineDockerInfo();
        result.setId(info.getId());
        result.setContainers(info.getContainers());
        result.setContainersRunning(info.getContainersRunning());
        result.setContainersPaused(info.getContainersPaused());
        result.setContainersStopped(info.getContainersStopped());
        result.setImages(info.getImages());
        result.setDriver(info.getDriver());
        if (info.getDriverStatuses() != null) {
            result.setDriverStatus(info.getDriverStatuses().stream()
                    .filter(l -> l != null && l.size() >= 2)
                    .collect(java.util.stream.Collectors.toMap(
                            l -> l.get(0),
                            l -> l.get(1),
                            (a, b) -> a
                    )));
        }
        result.setDockerRootDir(info.getDockerRootDir());
        result.setDebug(info.getDebug());
        result.setNfd(info.getNFd());
        result.setNgoroutines(info.getNGoroutines());
        result.setSystemTime(info.getSystemTime());
        result.setLoggingDriver(info.getLoggingDriver());
        result.setCgroupDriver(info.getCGroupDriver());
        result.setCgroupVersion(info.getCGroupVersion());
        result.setKernelVersion(info.getKernelVersion());
        result.setOperatingSystem(info.getOperatingSystem());
        result.setOsType(info.getOsType());
        result.setArchitecture(info.getArchitecture());
        result.setNcpu(info.getNCPU());
        result.setMemTotal(info.getMemTotal());
        result.setIndexServerAddress(info.getIndexServerAddress());
        if (info.getRegistryConfig() != null) {
            result.setRegistryConfig(java.util.Map.of(
                    "indexConfigs", info.getRegistryConfig().getIndexConfigs()
            ));
        }
        if (info.getLabels() != null) {
            result.setLabels(java.util.Arrays.stream(info.getLabels())
                    .filter(s -> s != null && s.contains("="))
                    .map(s -> s.split("=", 2))
                    .collect(java.util.stream.Collectors.toMap(
                            arr -> arr[0],
                            arr -> arr[1],
                            (a, b) -> a
                    )));
        }
        result.setExperimentalBuild(info.getExperimentalBuild());
        result.setServerVersion(info.getServerVersion());
        if (info.getRuntimes() != null) {
            result.setRuntimes(new ArrayList<>(info.getRuntimes().keySet()));
        }
        result.setSwapLimit(info.getSwapLimit());
        result.setMemoryLimit(info.getMemoryLimit());
        return result;
    }

    @Override
    public EngineDockerSystemUsage getUsage() {
        // docker-java 3.4.2 未提供 systemDfCmd，返回空对象占位
        return new EngineDockerSystemUsage();
    }

    @Override
    public String getVersion() {
        var version = dockerClient.versionCmd().exec();
        return version.getVersion();
    }

    @Override
    @SuppressWarnings("unchecked")
    public String pruneAll() {
        StringBuilder sb = new StringBuilder();
        try {
            var containerResult = dockerClient.pruneCmd(PruneType.CONTAINERS).exec();
            Object containersDeleted = containerResult.getRawValues().get("ContainersDeleted");
            int count = containersDeleted instanceof List ? ((List<?>) containersDeleted).size() : 0;
            sb.append("Containers pruned: ").append(count).append("\n");
        } catch (Exception e) {
            sb.append("Containers prune failed: ").append(e.getMessage()).append("\n");
        }
        try {
            var networkResult = dockerClient.pruneCmd(PruneType.NETWORKS).exec();
            Object networksDeleted = networkResult.getRawValues().get("NetworksDeleted");
            int count = networksDeleted instanceof List ? ((List<?>) networksDeleted).size() : 0;
            sb.append("Networks pruned: ").append(count).append("\n");
        } catch (Exception e) {
            sb.append("Networks prune failed: ").append(e.getMessage()).append("\n");
        }
        try {
            var imageResult = dockerClient.pruneCmd(PruneType.IMAGES).exec();
            Object imagesDeleted = imageResult.getRawValues().get("ImagesDeleted");
            int count = imagesDeleted instanceof List ? ((List<?>) imagesDeleted).size() : 0;
            sb.append("Images pruned: ").append(count).append("\n");
        } catch (Exception e) {
            sb.append("Images prune failed: ").append(e.getMessage()).append("\n");
        }
        try {
            var volumeResult = dockerClient.pruneCmd(PruneType.VOLUMES).exec();
            Object volumesDeleted = volumeResult.getRawValues().get("VolumesDeleted");
            int count = volumesDeleted instanceof List ? ((List<?>) volumesDeleted).size() : 0;
            sb.append("Volumes pruned: ").append(count).append("\n");
        } catch (Exception e) {
            sb.append("Volumes prune failed: ").append(e.getMessage()).append("\n");
        }
        return sb.toString();
    }

}
