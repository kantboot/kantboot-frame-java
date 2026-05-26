package com.kantboot.gateway.client.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@Data
public class GatewayClientInfo {

    /**
     * 网关客户端的id
     */
    private String clientId;

    private Integer serverPort;

    private Integer serverPort0;

    private Integer serverPort1;

    private String gatewayServerHost;

    private String gatewayClientForwardPath;


}
