package com.kantboot.engine.docker.service.impl;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PruneType;
import com.kantboot.engine.docker.domain.entity.EngineDockerImage;
import com.kantboot.engine.docker.service.IEngineDockerImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EngineDockerImageServiceImpl implements IEngineDockerImageService {

    @Resource
    private DockerClient dockerClient;

    @Override
    public List<EngineDockerImage> list() {
        List<Image> images = dockerClient.listImagesCmd().withShowAll(false).exec();
        return images.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public EngineDockerImage inspect(String id) {
        var inspect = dockerClient.inspectImageCmd(id).exec();
        EngineDockerImage image = new EngineDockerImage();
        image.setId(inspect.getId());
        image.setRepoTags(inspect.getRepoTags() != null ? inspect.getRepoTags() : new ArrayList<>());
        image.setRepoDigests(inspect.getRepoDigests() != null ? inspect.getRepoDigests() : new ArrayList<>());
        image.setCreated(inspect.getCreated() != null ? Long.parseLong(inspect.getCreated()) : null);
        image.setSize(inspect.getSize());
        image.setVirtualSize(inspect.getVirtualSize());
        image.setLabels(inspect.getConfig() != null && inspect.getConfig().getLabels() != null
                ? inspect.getConfig().getLabels() : null);
        image.setArchitecture(inspect.getArch());
        image.setOs(inspect.getOs());
        return image;
    }

    @Override
    public void remove(String id, boolean force, boolean noPrune) {
        dockerClient.removeImageCmd(id)
                .withForce(force)
                .withNoPrune(noPrune)
                .exec();
    }

    @Override
    public void pull(String repository, String tag) {
        try {
            dockerClient.pullImageCmd(repository)
                    .withTag(tag)
                    .exec(new com.github.dockerjava.api.command.PullImageResultCallback())
                    .awaitCompletion();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("拉取镜像被中断", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> prune() {
        var result = dockerClient.pruneCmd(PruneType.IMAGES).exec();
        Object deleted = result.getRawValues().get("ImagesDeleted");
        if (deleted instanceof List) {
            return (List<String>) deleted;
        }
        return new ArrayList<>();
    }

    private EngineDockerImage convert(Image image) {
        EngineDockerImage result = new EngineDockerImage();
        result.setId(image.getId());
        result.setRepoTags(image.getRepoTags() != null ? Arrays.asList(image.getRepoTags()) : new ArrayList<>());
        result.setRepoDigests(image.getRepoDigests() != null ? Arrays.asList(image.getRepoDigests()) : new ArrayList<>());
        result.setCreated(image.getCreated());
        result.setSize(image.getSize());
        result.setVirtualSize(image.getVirtualSize());
        result.setLabels(image.getLabels());
        result.setContainers(image.getContainers());
        return result;
    }

}
