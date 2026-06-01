package com.kantboot.engine.docker.service.impl;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerPort;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.kantboot.engine.docker.domain.entity.EngineDockerContainer;
import com.kantboot.engine.docker.service.IEngineDockerContainerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EngineDockerContainerServiceImpl implements IEngineDockerContainerService {

    @Resource
    private DockerClient dockerClient;

    @Override
    public List<EngineDockerContainer> list(boolean all) {
        List<Container> containers = dockerClient.listContainersCmd().withShowAll(all).exec();
        return containers.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public EngineDockerContainer inspect(String id) {
        var inspect = dockerClient.inspectContainerCmd(id).exec();
        EngineDockerContainer container = new EngineDockerContainer();
        container.setId(inspect.getId());
        container.setNames(Arrays.asList(inspect.getName()));
        container.setImage(inspect.getConfig().getImage());
        container.setImageId(inspect.getImageId());
        container.setCommand(inspect.getConfig().getCmd() != null ? String.join(" ", inspect.getConfig().getCmd()) : "");
        container.setCreated(inspect.getCreated() != null ? java.time.Instant.parse(inspect.getCreated()).getEpochSecond() : null);
        container.setState(inspect.getState().getStatus());
        container.setStatus(inspect.getState().getStatus());
        container.setLabels(inspect.getConfig().getLabels());
        container.setSizeRw(inspect.getSizeRw() != null ? inspect.getSizeRw().longValue() : null);
        container.setSizeRootFs(inspect.getSizeRootFs() != null ? inspect.getSizeRootFs().longValue() : null);
        if (inspect.getHostConfig() != null) {
            container.setHostConfig(inspect.getHostConfig().getNetworkMode());
        }
        return container;
    }

    @Override
    public void start(String id) {
        dockerClient.startContainerCmd(id).exec();
    }

    @Override
    public void stop(String id) {
        dockerClient.stopContainerCmd(id).exec();
    }

    @Override
    public void restart(String id) {
        dockerClient.restartContainerCmd(id).exec();
    }

    @Override
    public void pause(String id) {
        dockerClient.pauseContainerCmd(id).exec();
    }

    @Override
    public void unpause(String id) {
        dockerClient.unpauseContainerCmd(id).exec();
    }

    @Override
    public void remove(String id, boolean force, boolean removeVolumes) {
        dockerClient.removeContainerCmd(id)
                .withForce(force)
                .withRemoveVolumes(removeVolumes)
                .exec();
    }

    @Override
    public String logs(String id, int tail) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            var cmd = dockerClient.logContainerCmd(id)
                    .withStdOut(true)
                    .withStdErr(true)
                    .withTimestamps(false);
            if (tail > 0) {
                cmd.withTail(tail);
            } else {
                cmd.withTailAll();
            }
            cmd.exec(new LogContainerResultCallback() {
                @Override
                public void onNext(Frame item) {
                    try {
                        outputStream.write(item.getPayload());
                    } catch (Exception e) {
                        log.error("Write log error", e);
                    }
                }
            }).awaitCompletion();
            return outputStream.toString(StandardCharsets.UTF_8);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("获取日志被中断", e);
        }
    }

    @Override
    public String exec(String id, String[] command) {
        try {
            ExecCreateCmdResponse execCreate = dockerClient.execCreateCmd(id)
                    .withCmd(command)
                    .withAttachStdout(true)
                    .withAttachStderr(true)
                    .exec();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            dockerClient.execStartCmd(execCreate.getId()).exec(
                    new com.github.dockerjava.core.command.ExecStartResultCallback(outputStream, outputStream)
            ).awaitCompletion();
            return outputStream.toString(StandardCharsets.UTF_8);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("执行命令被中断", e);
        }
    }

    private EngineDockerContainer convert(Container container) {
        EngineDockerContainer result = new EngineDockerContainer();
        result.setId(container.getId());
        result.setNames(container.getNames() != null ? Arrays.asList(container.getNames()) : new ArrayList<>());
        result.setImage(container.getImage());
        result.setImageId(container.getImageId());
        result.setCommand(container.getCommand());
        result.setCreated(container.getCreated());
        result.setState(container.getState());
        result.setStatus(container.getStatus());
        result.setLabels(container.getLabels());
        result.setSizeRw(container.getSizeRw());
        result.setSizeRootFs(container.getSizeRootFs());
        result.setPorts(convertPorts(container.getPorts()));
        return result;
    }

    private List<EngineDockerContainer.Port> convertPorts(ContainerPort[] ports) {
        if (ports == null) return new ArrayList<>();
        List<EngineDockerContainer.Port> list = new ArrayList<>();
        for (ContainerPort port : ports) {
            EngineDockerContainer.Port p = new EngineDockerContainer.Port();
            p.setIp(port.getIp());
            p.setPrivatePort(port.getPrivatePort());
            p.setPublicPort(port.getPublicPort());
            p.setType(port.getType());
            list.add(p);
        }
        return list;
    }

}
