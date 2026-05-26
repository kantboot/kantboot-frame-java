package com.kantboot.test.starter;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class DatabaseInfoService {

    private final DataSource dataSource;

    @Autowired
    public DatabaseInfoService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 获取数据库引擎信息
    public Object getDatabaseInfo() {
        Map<String, String> info = new HashMap<>();
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            info.put("databaseProductName", metaData.getDatabaseProductName());
            info.put("databaseProductVersion", metaData.getDatabaseProductVersion());
            info.put("driverName", metaData.getDriverName());
            info.put("driverVersion", metaData.getDriverVersion());
            info.put("url", metaData.getURL());
            info.put("username", metaData.getUserName());
            boolean readOnly = metaData.isReadOnly();
            info.put("readOnly", String.valueOf(readOnly));
        } catch (SQLException e) {
            log.error("获取数据库元数据失败", e);
            info.put("error", "获取数据库信息失败: " + e.getMessage());
        }
        return info;
    }

    // 获取当前连接数
    public PrintWriter getCurrentConnectionCount() {
        try {
            return dataSource.getLogWriter();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    // 获取连接池信息
    public Map<String, Object> getConnectionPoolInfo() {
        Map<String, Object> poolInfo = new HashMap<>();
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            poolInfo.put("poolType", "HikariCP");
            poolInfo.put("maximumPoolSize", hikariDataSource.getMaximumPoolSize());
            poolInfo.put("activeConnections", hikariDataSource.getHikariPoolMXBean().getActiveConnections());
            poolInfo.put("idleConnections", hikariDataSource.getHikariPoolMXBean().getIdleConnections());
            poolInfo.put("threadsAwaitingConnection", hikariDataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
            poolInfo.put("totalConnections", hikariDataSource.getHikariPoolMXBean().getTotalConnections());
        } else {
            poolInfo.put("poolType", "未知连接池");
            poolInfo.put("message", "当前使用的是: " + dataSource.getClass().getSimpleName());
        }
        return poolInfo;
    }

}