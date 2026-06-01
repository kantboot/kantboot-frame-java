package com.kantboot.engine.docker.service.impl;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.api.model.PruneType;
import com.kantboot.engine.docker.domain.entity.EngineDockerNetwork;
import com.kantboot.engine.docker.service.IEngineDockerNetworkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EngineDockerNetworkServiceImpl implements IEngineDockerNetworkService {

    @Resource
    private DockerClient dockerClient;

    @Override
    public List<EngineDockerNetwork> list() {
        List<Network> networks = dockerClient.listNetworksCmd().exec();
        return networks.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public EngineDockerNetwork inspect(String id) {
        Network network = dockerClient.inspectNetworkCmd().withNetworkId(id).exec();
        return convert(network);
    }

    @Override
    public EngineDockerNetwork create(String name, String driver, Map<String, String> options) {
        var response = dockerClient.createNetworkCmd()
                .withName(name)
                .withDriver(driver)
                .withOptions(options)
                .exec();
        // 创建成功后查询详情返回
        return inspect(response.getId());
    }

    @Override
    public void remove(String id) {
        dockerClient.removeNetworkCmd(id).exec();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> prune() {
        var result = dockerClient.pruneCmd(PruneType.NETWORKS).exec();
        Object deleted = result.getRawValues().get("NetworksDeleted");
        if (deleted instanceof List) {
            return (List<String>) deleted;
        }
        return new ArrayList<>();
    }

    private EngineDockerNetwork convert(Network network) {
        EngineDockerNetwork result = new EngineDockerNetwork();
        result.setName(network.getName());
        result.setId(network.getId());
        result.setCreated(network.getCreated() != null ? network.getCreated().toString() : null);
        result.setScope(network.getScope());
        result.setDriver(network.getDriver());
        result.setEnableIPv6(network.getEnableIPv6());
        result.setInternal(network.getInternal());
        result.setAttachable(network.isAttachable());
        result.setIngress(false);
        result.setOptions(network.getOptions() != null ? new java.util.HashMap<>(network.getOptions()) : null);
        result.setLabels(network.getLabels());
        if (network.getIpam() != null) {
            java.util.Map<String, Object> ipamMap = new java.util.HashMap<>();
            ipamMap.put("driver", network.getIpam().getDriver());
            ipamMap.put("config", network.getIpam().getConfig());
            result.setIpam(ipamMap);
        }
        if (network.getContainers() != null) {
            result.setContainers(network.getContainers().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> (Object) e.getValue().getName()
                    )));
        }
        return result;
    }

}
