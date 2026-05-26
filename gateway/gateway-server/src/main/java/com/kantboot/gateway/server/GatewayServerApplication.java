package com.kantboot.gateway.server;

import com.alibaba.fastjson2.JSONObject;
import com.kantboot.gateway.server.util.ProxyUtil;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GatewayServerApplication {

    public static final ConcurrentHashMap<String, Object> DATA_MAP = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        // 创建服务器实例，监听端口 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        // 处理打开客户端连接的请求
        server.createContext("/gateway-server-web/openClient", new OpenClientHandler());
        // 处理关闭客户端连接的请求
        server.createContext("/gateway-server-web/closeClient", new CloseClientHandler());

        // 心跳
        server.createContext("/gateway-server-web/heartbeat", new HeartbeatHandler());

        server.createContext("/gateway-server-web/getAllClients", new GetAllClientsHandler());
        server.createContext("/", new ProxyHandler());

        // 设置线程池（null 表示使用默认）
        server.setExecutor(null);

        // 启动服务器
        server.start();
        System.out.println("Server started on port 8000");
    }

    // 根路径处理器
    static class ProxyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // 获取访问路径
            String path = exchange.getRequestURI().getPath();
            Collection<Object> values = DATA_MAP.values();
            // 根据路径为key生成新的map，还要获取一共有几个斜杠
            Map<String, List<Map<String, Object>>> clientMap = new HashMap<>();
            for (Object value : values) {
                Map<String, Object> clientData = (Map<String, Object>) value;
                String clientForwardPath = (String) clientData.get("clientForwardPath");
                if (clientForwardPath != null) {
                    List<Map<String, Object>> maps = clientMap.get(clientForwardPath);
                    if (maps == null) {
                        maps = new java.util.ArrayList<>();
                        maps.add(clientData);
                    } else {
                        maps.add(clientData);
                    }
                    clientMap.put(clientForwardPath, maps);
                }
            }
            System.err.println(JSONObject.toJSONString(clientMap));

            List<String> keys = new ArrayList<>();
            // 排序，通过谁的斜杠（/）多来排序
            for (String key : clientMap.keySet()) {
                keys.add(key);
            }
            keys.sort(Comparator.comparingInt(key -> key.length() - key.replace("/", "").length()));
            for (String key : keys) {
                if (path.startsWith(key)) {
                    List<Map<String, Object>> maps = clientMap.get(key);
                    // 随机数
                    int randomIndex = new Random().nextInt(maps.size());
                    Map<String, Object> clientData = maps.get(randomIndex);

                    String host = "http://" + clientData.get("clientIp") + ":" + clientData.get("clientPort") + path;
                    ProxyUtil.proxy(exchange, host);
                    return;
                }
            }
        }
    }

    // 打开客户端连接处理器
    static class OpenClientHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String clientId = getQueryParam(exchange, "clientId");
            if (clientId == null) {
                sendResponse(exchange, RestResult.error("clientIdIsEmpty", "clientId不能为空", "zh_CN"));
                return;
            }
            String port = getQueryParam(exchange, "port");
            if (port == null) {
                sendResponse(exchange, RestResult.error("portIsEmpty", "port不能为空", "zh_CN"));
                return;
            }
            String clientForwardPath = getQueryParam(exchange, "clientForwardPath");
            if (clientForwardPath == null) {
                sendResponse(exchange, RestResult.error("clientForwardPathIsEmpty", "clientForwardPath不能为空", "zh_CN"));
                return;
            }
            // 获取客户端IP
            String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();
            DATA_MAP.put(clientId, Map.of(
                    "clientId", clientId,
                    "clientIp", clientIp,
                    "clientPort", port,
                    "clientForwardPath", clientForwardPath,
                    "lastHeartbeat", System.currentTimeMillis()));
            sendResponse(exchange, RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS));
        }
    }

    // 关闭客户端连接处理器
    static class CloseClientHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String clientId = getQueryParam(exchange, "clientId");
            if (clientId != null) {
                // 关闭客户端连接
                DATA_MAP.remove(clientId);
            }
            sendResponse(exchange, RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS));
        }
    }

    // 获取所有客户端
    static class GetAllClientsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // 返回所有客户端信息
            sendResponse(exchange, RestResult.success(DATA_MAP.values(), CommonSuccessStateConsts.OPERATION_SUCCESS));
        }
    }

    // 心跳处理器
    static class HeartbeatHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String clientId = getQueryParam(exchange, "clientId");
            if (clientId == null) {
                sendResponse(exchange, RestResult.error("clientIdIsEmpty", "clientId不能为空", "zh_CN"));
                return;
            }
            // 更新心跳时间
            Map<String, Object> clientData = new HashMap<>();
            clientData.put("clientId", clientId);
            String port = getQueryParam(exchange, "port");
            if (port == null) {
                sendResponse(exchange, RestResult.error("portIsEmpty", "port不能为空", "zh_CN"));
                return;
            }
            String clientForwardPath = getQueryParam(exchange, "clientForwardPath");
            if (clientForwardPath == null) {
                sendResponse(exchange, RestResult.error("clientForwardPathIsEmpty", "clientForwardPath不能为空", "zh_CN"));
                return;
            }
            // 获取客户端IP
            String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();
            clientData.put("clientIp", clientIp);
            clientData.put("clientPort", port);
            clientData.put("clientForwardPath", clientForwardPath);
            clientData.put("lastHeartbeat", System.currentTimeMillis());
            DATA_MAP.put(clientId, clientData);
            sendResponse(exchange, RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS));
        }
    }


    // 通用响应方法
    private static void sendResponse(HttpExchange exchange, Object data) throws IOException {
        String response = JSONObject.toJSONString(data);
        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, response.length());

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    // 获取参数
    private static String getQueryParam(HttpExchange exchange, String key) {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.contains(key + "=")) {
            return query.split(key + "=")[1].split("&")[0];
        }
        return null;
    }

}
