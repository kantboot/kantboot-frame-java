package com.kantboot.engine.docker.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Slf4j
@Configuration
@EnableConfigurationProperties(EngineDockerProperties.class)
public class EngineDockerClientConfig {

    @Bean
    @ConditionalOnMissingBean
    public DockerClientConfig dockerClientConfig(EngineDockerProperties properties) {
        DefaultDockerClientConfig.Builder builder = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(properties.getHost())
                .withDockerTlsVerify(properties.isTlsVerify())
                .withApiVersion(properties.getApiVersion());

        if (properties.getCertPath() != null && !properties.getCertPath().isEmpty()) {
            builder.withDockerCertPath(properties.getCertPath());
        }

        if (properties.getRegistry().getUsername() != null) {
            builder.withRegistryUsername(properties.getRegistry().getUsername())
                    .withRegistryPassword(properties.getRegistry().getPassword())
                    .withRegistryEmail(properties.getRegistry().getEmail())
                    .withRegistryUrl(properties.getRegistry().getUrl());
        }

        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public DockerHttpClient dockerHttpClient(DockerClientConfig config, EngineDockerProperties properties) {
        return new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofMillis(properties.getConnectTimeout()))
                .responseTimeout(Duration.ofMillis(properties.getReadTimeout()))
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public DockerClient dockerClient(DockerClientConfig config, DockerHttpClient httpClient) {
        log.info("DockerClient initialized, host: {}", config.getDockerHost());
        return DockerClientImpl.getInstance(config, httpClient);
    }

}
