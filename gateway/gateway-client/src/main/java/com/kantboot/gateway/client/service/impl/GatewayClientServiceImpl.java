package com.kantboot.gateway.client.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.kantboot.gateway.client.config.GatewayClientInfo;
import com.kantboot.gateway.client.config.GatewayClientPort;
import com.kantboot.gateway.client.service.IGatewayClientService;
import com.kantboot.util.http.HttpSendUtil;
import com.kantboot.util.http.domain.config.HttpSendConfig;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GatewayClientServiceImpl
        implements IGatewayClientService {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private GatewayClientInfo gatewayClientInfo;

    @Override
    public void sendRequestToGatewayServer(String requestUrl, Object requestBody) {
        String url = "http://" + gatewayClientInfo.getGatewayServerHost() + requestUrl;
        HttpSendUtil.send(
                new HttpSendConfig()
                        .setUrl(url)
                        .setMethod("GET")
                        .setContentType("application/json")
                        .setBody(requestBody)
        );
//        System.err.println(url);
//        RestTemplate restTemplate = new RestTemplate();
//        try {
//            restTemplate.postForObject(url, requestBody, Void.class);
//            log.info("Request sent to gateway server successfully: {}", url);
//        } catch (Exception e) {
//            log.error("Failed to send request to gateway server: {}", url, e);
//        }
    }

    @Override
    public void closeClientNotificationToGatewayServer() {
        sendRequestToGatewayServer("/gateway-server-web/closeClient?clientId="+gatewayClientInfo.getClientId(), null);
    }

    @Override
    public void openClientNotificationToGatewayServer() {
        sendRequestToGatewayServer("/gateway-server-web/openClient?clientId="+gatewayClientInfo.getClientId()+"&port="+ GatewayClientPort.PORT+"&clientForwardPath="+gatewayClientInfo.getGatewayClientForwardPath(), null);
        log.info("Client connection opened with ID: {}", gatewayClientInfo.getClientId());
    }

    @Override
    public void sendHeartbeatToGatewayServer() {
        sendRequestToGatewayServer("/gateway-server-web/heartbeat?clientId="+gatewayClientInfo.getClientId()+"&clientForwardPath="+gatewayClientInfo.getGatewayClientForwardPath(), null);
        log.info("Heartbeat sent to gateway server for client ID: {}", gatewayClientInfo.getClientId());
    }

    @SneakyThrows
    @Override
    public void closeClient() {
        ThreadUtil.execute(()->{
            System.err.println("---- Closing client connection ----");
            log.info("Closing client connection...");
            closeClientNotificationToGatewayServer();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                int exitCode = SpringApplication.exit(applicationContext, () -> 0);
                System.exit(exitCode);
                log.info("Client connection close request initiated.");
                return;
            }
            // 关闭当前的springboot服务
            int exitCode = SpringApplication.exit(applicationContext, () -> 0);
            System.exit(exitCode);
            log.info("Client connection close request initiated.");
        });
    }

}
