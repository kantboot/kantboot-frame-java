package com.kantboot.engine.database.service;

import com.kantboot.engine.database.doamin.entity.EngineDatabase;
import com.kantboot.engine.database.doamin.entity.EngineDatabasePool;

public interface IEngineDatabaseService {

    /**
     * 获取数据库信息
     * Get database information
     *
     * @return 数据库信息
     */
    EngineDatabase getInfo();

    /**
     * 获取数据库连接池信息
     * Get database connection pool information
     *
     * @return 数据库连接池信息
     */
    EngineDatabasePool getPoolInfo();


}