package com.kantboot.test.starter.main;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PortScanner {
    public static void main(String[] args) {
        String host = "localhost"; // 扫描本机
        List<Integer> openPorts = new ArrayList<>();
        
        // 扫描端口范围
        for (int port = 1; port <= 65535; port++) {
            try (Socket socket = new Socket()) {
                socket.connect(new java.net.InetSocketAddress(host, port), 100); // 100ms超时
                openPorts.add(port);
                System.out.println("端口 " + port + " 开放");
            } catch (Exception ignored) {
                // 连接失败表示端口关闭
            }
        }
        
        System.out.println("\n开放端口列表: " + openPorts);
    }
}