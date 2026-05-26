package com.kantboot.gateway.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Configuration
@Component
public class GatewayClientInfoConfig {

    @Value("${kantboot.server-port:8080}")
    private Integer serverPort;

    @Value("${kantboot.server-ports[0]:-1}")
    private Integer serverPort0;

    @Value("${kantboot.server-ports[1]:-1}")
    private Integer serverPort1;

    @Value("${kantboot.gateway-server-host}")
    private String gatewayServerHost;

    @Value("${gateway-client-forward-path:/}")
    private String gatewayClientForwardPath;

    @Bean
    public GatewayClientInfo gatewayClientInfo() {
        GatewayClientInfo info = new GatewayClientInfo();
        info.setClientId(UUID.randomUUID().toString());
        info.setServerPort(serverPort);
        info.setServerPort0(serverPort0);
        info.setServerPort1(serverPort1);
        info.setGatewayServerHost(gatewayServerHost);
        info.setGatewayClientForwardPath(gatewayClientForwardPath);
        return info;
    }

}
