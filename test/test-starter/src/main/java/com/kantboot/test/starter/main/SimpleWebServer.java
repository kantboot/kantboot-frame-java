package com.kantboot.test.starter.main;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SimpleWebServer {

    public static void main(String[] args) throws IOException {
        // 创建服务器实例，监听 8080 端口
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        // 设置路由和处理器
        server.createContext("/", new RootHandler());

        // 启动服务器
        server.start();
        System.out.println("Server started on port 8080");
    }

    // 处理根路径请求
    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // 监听域名
            String host = exchange.getRequestHeaders().getFirst("Host");
            // 反向代理到10099端口
            exchange.sendResponseHeaders(200, 0);
        }
    }


}