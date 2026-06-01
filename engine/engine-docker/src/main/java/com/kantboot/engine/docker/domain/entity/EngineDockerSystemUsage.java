package com.kantboot.engine.docker.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class EngineDockerSystemUsage implements Serializable {

    /**
     * 层大小
     */
    private Long layersSize;

    /**
     * 镜像列表
     */
    private List<ImageUsage> images;

    /**
     * 容器列表
     */
    private List<ContainerUsage> containers;

    /**
     * 卷列表
     */
    private List<VolumeUsage> volumes;

    /**
     * 构建缓存
     */
    private List<BuildCache> buildCache;

    @Data
    public static class ImageUsage implements Serializable {
        private String id;
        private List<String> repoTags;
        private Long size;
        private Long sharedSize;
        private Long virtualSize;
        private String createdAt;
    }

    @Data
    public static class ContainerUsage implements Serializable {
        private String id;
        private List<String> names;
        private String image;
        private String imageID;
        private String created;
        private Long sizeRw;
        private Long sizeRootFs;
    }

    @Data
    public static class VolumeUsage implements Serializable {
        private String name;
        private String driver;
        private String mountpoint;
        private Long size;
        private String createdAt;
    }

    @Data
    public static class BuildCache implements Serializable {
        private String id;
        private String parent;
        private String type;
        private String description;
        private Boolean inUse;
        private Boolean shared;
        private Long size;
        private String createdAt;
        private String lastUsedAt;
        private Long usageCount;
    }

}
