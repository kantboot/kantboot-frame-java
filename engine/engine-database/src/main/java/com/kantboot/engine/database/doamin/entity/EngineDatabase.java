package com.kantboot.engine.database.doamin.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class EngineDatabase implements Serializable {

    /**
     * 数据库产品名称
     * Database product name
     */
    private String databaseProductName;

    /**
     * 数据库产品版本
     * Database product version
     */
    private String databaseProductVersion;

    /**
     * 驱动名称
     * Driver name
     */
    private String driverName;

    /**
     * 驱动版本
     * Driver version
     */
    private String driverVersion;

    /**
     * 数据库连接URL
     * Database connection URL
     */
    private String url;

    /**
     * 数据库用户名
     * Database username
     */
    private String username;

    /**
     * 数据库是否只读
     * Is the database read-only
     */
    private boolean readOnly;

}
