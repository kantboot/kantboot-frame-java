package com.kantboot.gateway.client.config;

import com.kantboot.gateway.client.service.IGatewayClientService;
import com.kantboot.gateway.client.util.GatewayClientUtil;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@Data
public class GatewayClientConfig {

    @Resource
    private GatewayClientInfo gatewayClientInfo;

    @Resource
    private IGatewayClientService gatewayClientService;


    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> portCustomizer() {

        Integer serverPort = gatewayClientInfo.getServerPort();
        Integer serverPort0 = gatewayClientInfo.getServerPort0();
        Integer serverPort1 = gatewayClientInfo.getServerPort1();

        // 如果serverPort0是-1，则使用默认端口
        if (serverPort0 == -1) {
            return factory -> factory.setPort(serverPort);
        }
        // 如果serverPort0不是-1，则使用serverPort0
        if (serverPort1 == -1) {
            return factory -> factory.setPort(serverPort0);
        }
        int oldPort = -1;
        int newPort = serverPort0;

        // 检测serverPort是否被占用
        try {
            new java.net.ServerSocket(newPort).close();
            oldPort = serverPort1;
            newPort = serverPort0;
        } catch (java.io.IOException e) {
            oldPort = serverPort0;
            newPort = serverPort1;
        }

        GatewayClientPort.PORT = newPort;

        gatewayClientService.openClientNotificationToGatewayServer();
        // 向网关客户端发送关闭请求，其中已经告知服务端关闭了该旧端口
        GatewayClientUtil.sendCloseClientRequest(oldPort);

        int finalNewPort = newPort;
        return factory -> factory.setPort(finalNewPort);
    }

}
