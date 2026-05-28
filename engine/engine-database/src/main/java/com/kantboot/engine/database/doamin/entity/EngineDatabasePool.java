package com.kantboot.engine.database.doamin.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class EngineDatabasePool implements Serializable {

//    Map<String, Object> poolInfo = new HashMap<>();
//        poolInfo.put("poolType", "HikariCP");
//        poolInfo.put("maximumPoolSize", dataSource.getMaximumPoolSize());
//        poolInfo.put("minimumIdle", dataSource.getMinimumIdle());
//        poolInfo.put("activeConnections", dataSource.getHikariPoolMXBean().getActiveConnections());
//        poolInfo.put("idleConnections", dataSource.getHikariPoolMXBean().getIdleConnections());
//        poolInfo.put("threadsAwaitingConnection", dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
//        poolInfo.put("totalConnections", dataSource.getHikariPoolMXBean().getTotalConnections());
//        poolInfo.put("configChanges", configChanges.get());
//        poolInfo.put("connectionTimeout", dataSource.getConnectionTimeout());
//        poolInfo.put("idleTimeout", dataSource.getIdleTimeout());
//        poolInfo.put("maxLifetime", dataSource.getMaxLifetime());
//        poolInfo.put("autoCommit", dataSource.isAutoCommit());
//        return poolInfo;

    /**
     * 连接池类型
     */
    private String poolType;

    /**
     * 最大连接数
     */
    private int maximumPoolSize;

    /**
     * 最小空闲连接数
     */
    private int minimumIdle;

    /**
     * 活动连接数
     */
    private int activeConnections;

    /**
     * 空闲连接数
     */
    private int idleConnections;

    /**
     * 等待连接的线程数
     */
    private int threadsAwaitingConnection;

    /**
     * 总连接数
     */
    private int totalConnections;

    /**
     * 配置更改次数
     */
    private long configChanges;

    /**
     * 连接超时时间（毫秒）
     */
    private long connectionTimeout;

    /**
     * 空闲超时时间（毫秒）
     */
    private long idleTimeout;

    /**
     * 最大生命周期（毫秒）
     */
    private long maxLifetime;

    /**
     * 是否自动提交
     */
    private boolean autoCommit;




}
