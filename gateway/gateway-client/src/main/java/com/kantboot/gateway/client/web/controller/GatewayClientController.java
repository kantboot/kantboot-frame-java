package com.kantboot.gateway.client.web.controller;

import com.kantboot.gateway.client.service.IGatewayClientService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/gateway-client-web")
public class GatewayClientController {

    @Resource
    private IGatewayClientService service;

    @RequestMapping("/closeClient")
    public void closeClient() {
        service.closeClient();
    }

}
