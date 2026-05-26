package com.kantboot.test.starter.main;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentPortScanner {
    private static final String HOST = "localhost";
    private static final int TIMEOUT = 100; // 毫秒
    private static final int THREADS = 100; // 线程数
    
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        List<Integer> openPorts = new CopyOnWriteArrayList<>();
        
        // 提交扫描任务
        for (int port = 1; port <= 65535; port++) {
            final int currentPort = port;
            executor.submit(() -> {
                try (Socket socket = new Socket()) {
                    socket.connect(new java.net.InetSocketAddress(HOST, currentPort), TIMEOUT);
                    openPorts.add(currentPort);
                    System.out.println("端口 " + currentPort + " 开放");
                } catch (Exception ignored) {}
            });
        }
        
        // 关闭线程池并等待完成
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);
        
        System.out.println("\n开放端口列表: " + openPorts);
    }
}