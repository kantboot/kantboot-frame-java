package com.kantboot.gateway.client.timer;

import com.kantboot.gateway.client.service.IGatewayClientService;
import com.kantboot.util.timer.annotation.TimerOn;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class GatewayClientHeartbeatTimer {

    @Resource
    private IGatewayClientService gatewayClientService;

    @TimerOn(code="GatewayClientHeartbeatTimer",
    name = "网关客户端心跳定时器",
            description = "向网关服务端发送心跳请求",
            time = 5000L)
    public void heartbeat() {
        gatewayClientService.sendHeartbeatToGatewayServer();
    }

}
