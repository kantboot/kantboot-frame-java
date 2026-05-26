package com.kantboot.gateway.client.util;

import cn.hutool.core.thread.ThreadUtil;
import org.springframework.web.client.RestTemplate;

public class GatewayClientUtil {

    /**
     * 向本地网关客户端发送关闭请求
     */
    public static void sendCloseClientRequest(int port) {
        ThreadUtil.execute(()->{
            for (int i = 1; i <= 10; i++) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                RestTemplate restTemplate = new RestTemplate();
                String url = "http://localhost:" + port + "/gateway-client-web/closeClient";
                try {
                    restTemplate.getForObject(url, Void.class);
                    System.out.println("["+i+"]已向网关客户端发送关闭请求，端口：" + port);
                } catch (Exception e) {
                    System.out.println("["+i+"]向网关客户端发送关闭请求失败，端口：" + port + "，应该已经关闭");
                }
            }
        });
    }

}
