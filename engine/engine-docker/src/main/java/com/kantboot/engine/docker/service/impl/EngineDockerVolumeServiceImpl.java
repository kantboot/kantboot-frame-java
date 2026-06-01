package com.kantboot.engine.docker.service.impl;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.command.InspectVolumeResponse;
import com.github.dockerjava.api.model.PruneType;
import com.kantboot.engine.docker.domain.entity.EngineDockerVolume;
import com.kantboot.engine.docker.service.IEngineDockerVolumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EngineDockerVolumeServiceImpl implements IEngineDockerVolumeService {

    @Resource
    private DockerClient dockerClient;

    @Override
    public List<EngineDockerVolume> list() {
        var response = dockerClient.listVolumesCmd().exec();
        if (response.getVolumes() == null) {
            return new ArrayList<>();
        }
        return response.getVolumes().stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public EngineDockerVolume inspect(String name) {
        var volume = dockerClient.inspectVolumeCmd(name).exec();
        return convert(volume);
    }

    @Override
    public EngineDockerVolume create(String name, String driver, Map<String, String> driverOpts, Map<String, String> labels) {
        var response = dockerClient.createVolumeCmd()
                .withName(name)
                .withDriver(driver)
                .withDriverOpts(driverOpts)
                .withLabels(labels)
                .exec();
        return convert(response);
    }

    @Override
    public void remove(String name, boolean force) {
        dockerClient.removeVolumeCmd(name).exec();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> prune() {
        var result = dockerClient.pruneCmd(PruneType.VOLUMES).exec();
        Object deleted = result.getRawValues().get("VolumesDeleted");
        if (deleted instanceof List) {
            return (List<String>) deleted;
        }
        return new ArrayList<>();
    }

    private EngineDockerVolume convert(InspectVolumeResponse volume) {
        EngineDockerVolume result = new EngineDockerVolume();
        result.setName(volume.getName());
        result.setDriver(volume.getDriver());
        result.setMountpoint(volume.getMountpoint());
        result.setLabels(volume.getLabels());
        result.setOptions(volume.getOptions());
        result.setScope("local");
        return result;
    }

    private EngineDockerVolume convert(CreateVolumeResponse volume) {
        EngineDockerVolume result = new EngineDockerVolume();
        result.setName(volume.getName());
        result.setDriver(volume.getDriver());
        result.setMountpoint(volume.getMountpoint());
        result.setLabels(volume.getLabels());
        result.setScope("local");
        return result;
    }

}
