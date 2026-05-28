package com.kantboot.gateway.server.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProxyUtil {

    public static void proxy(HttpExchange exchange, String targetHost) {
        URI targetUri = URI.create(targetHost);

        HttpURLConnection targetConnection = null;
        try {
            // 准备目标连接
            targetConnection = prepareTargetConnection(exchange,targetUri);

            // 转发请求体
            forwardRequestBody(exchange, targetConnection);

            // 获取响应状态码
            int responseCode = targetConnection.getResponseCode();

            // 转发响应头
            forwardResponseHeaders(exchange, targetConnection, responseCode);

            // 转发响应体
            forwardResponseBody(exchange, targetConnection);
        } catch (IOException e) {
            try {
                sendErrorResponse(exchange, "Error processing request: " + e.getMessage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            if (targetConnection != null) {
                targetConnection.disconnect();
            }
        }
    }

    private static HttpURLConnection prepareTargetConnection(HttpExchange exchange,URI targetUri) throws IOException {
        // 构建目标URL
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();
        URI target = targetUri.resolve(path + (query != null ? "?" + query : ""));

        HttpURLConnection conn = (HttpURLConnection) target.toURL().openConnection();

        // 设置请求方法
        conn.setRequestMethod(exchange.getRequestMethod());

        // 复制请求头
        for (Map.Entry<String, List<String>> header : exchange.getRequestHeaders().entrySet()) {
            String key = header.getKey();
            if (shouldForwardRequestHeader(key)) {
                for (String value : header.getValue()) {
                    conn.addRequestProperty(key, value);
                }
            }
        }

        // 设置超时
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(10000);
        conn.setDoInput(true);

        return conn;
    }

    private static void forwardRequestBody(HttpExchange exchange, HttpURLConnection target) throws IOException {
        if (hasRequestBody(exchange)) {
            target.setDoOutput(true);
            try (OutputStream os = target.getOutputStream();
                 InputStream is = exchange.getRequestBody()) {
                is.transferTo(os);
            }
        }
    }

    private static void forwardResponseHeaders(HttpExchange exchange, HttpURLConnection target, int status) throws IOException {
        // 设置响应状态码
        exchange.sendResponseHeaders(status, 0);

        // 复制响应头
        Map<String, List<String>> headers = target.getHeaderFields();
        for (Map.Entry<String, List<String>> header : headers.entrySet()) {
            String key = header.getKey();
            if (key != null && shouldForwardResponseHeader(key)) {
                for (String value : header.getValue()) {
                    exchange.getResponseHeaders().add(key, value);
                }
            }
        }
    }

    private static void forwardResponseBody(HttpExchange exchange, HttpURLConnection target) throws IOException {
        try (OutputStream os = exchange.getResponseBody();
             InputStream is = getTargetInputStream(target)) {
            is.transferTo(os);
        }
    }

    private static InputStream getTargetInputStream(HttpURLConnection target) throws IOException {
        return (target.getResponseCode() >= 400) ?
                target.getErrorStream() :
                target.getInputStream();
    }

    private static boolean hasRequestBody(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        return "POST".equalsIgnoreCase(method) ||
                "PUT".equalsIgnoreCase(method) ||
                "PATCH".equalsIgnoreCase(method);
    }

    private static boolean shouldForwardRequestHeader(String key) {
        // 不过滤这些敏感头
        String[] skipHeaders = {"Host", "Content-Length", "Connection"};
        return !Arrays.asList(skipHeaders).contains(key);
    }

    private static boolean shouldForwardResponseHeader(String key) {
        // 过滤掉代理相关的响应头
        String[] skipHeaders = {"Transfer-Encoding", "Keep-Alive", "Connection"};
        return !Arrays.asList(skipHeaders).contains(key);
    }

    private static void sendErrorResponse(HttpExchange exchange, String message) throws IOException {
        exchange.sendResponseHeaders(502, message.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(message.getBytes());
        }
    }

}
