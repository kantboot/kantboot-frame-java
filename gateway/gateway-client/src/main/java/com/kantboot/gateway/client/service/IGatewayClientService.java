package com.kantboot.gateway.client.service;

public interface IGatewayClientService {

    /**
     * 向网关服务端发送请求
     */
     void sendRequestToGatewayServer(String requestUrl, Object requestBody);

    /**
     * 告知网关服务端关闭网关客户端服务
     */
    void closeClientNotificationToGatewayServer();

    /**
     * 告知网关开启服务
     */
    void openClientNotificationToGatewayServer();

    void sendHeartbeatToGatewayServer();

    /**
     * 关闭网关客户端服务
     */
    void closeClient();

}
