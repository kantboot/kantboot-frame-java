package com.kantboot.engine.database.service.impl;

import com.kantboot.engine.database.doamin.entity.EngineDatabase;
import com.kantboot.engine.database.doamin.entity.EngineDatabasePool;
import com.kantboot.engine.database.service.IEngineDatabaseService;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Service
public class EngineDatabaseServiceImpl
    implements IEngineDatabaseService {

    @Resource
    private DataSource dataSource;

    @Override
    public EngineDatabase getInfo() {
//        DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
//        info.put("databaseProductName", metaData.getDatabaseProductName());
//        info.put("databaseProductVersion", metaData.getDatabaseProductVersion());
//        info.put("driverName", metaData.getDriverName());
//        info.put("driverVersion", metaData.getDriverVersion());
//        info.put("url", metaData.getURL());
//        info.put("username", metaData.getUserName());
//        boolean readOnly = metaData.isReadOnly();
//        info.put("readOnly", String.valueOf(readOnly));

        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            // 创建 EngineDatabase 实例并设置属性
            EngineDatabase engineDatabase = new EngineDatabase();
            engineDatabase.setDatabaseProductName(metaData.getDatabaseProductName());
            engineDatabase.setDatabaseProductVersion(metaData.getDatabaseProductVersion());
            engineDatabase.setDriverName(metaData.getDriverName());
            engineDatabase.setDriverVersion(metaData.getDriverVersion());
            engineDatabase.setUrl(metaData.getURL());
            engineDatabase.setUsername(metaData.getUserName());
            engineDatabase.setReadOnly(metaData.isReadOnly());
            // 返回 EngineDatabase 实例
            return engineDatabase;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public EngineDatabasePool getPoolInfo() {
        HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
        EngineDatabasePool engineDatabasePool = new EngineDatabasePool();
        engineDatabasePool.setPoolType("HikariCP");
        engineDatabasePool.setMaximumPoolSize(hikariDataSource.getMaximumPoolSize());
        engineDatabasePool.setMinimumIdle(hikariDataSource.getMinimumIdle());
        engineDatabasePool.setActiveConnections(hikariDataSource.getHikariPoolMXBean().getActiveConnections());
        engineDatabasePool.setIdleConnections(hikariDataSource.getHikariPoolMXBean().getIdleConnections());
        engineDatabasePool.setThreadsAwaitingConnection(hikariDataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        engineDatabasePool.setTotalConnections(hikariDataSource.getHikariPoolMXBean().getTotalConnections());
        engineDatabasePool.setConnectionTimeout(hikariDataSource.getConnectionTimeout());
        engineDatabasePool.setIdleTimeout(hikariDataSource.getIdleTimeout());
        engineDatabasePool.setMaxLifetime(hikariDataSource.getMaxLifetime());
        engineDatabasePool.setAutoCommit(hikariDataSource.isAutoCommit());

        return engineDatabasePool;
    }
}
