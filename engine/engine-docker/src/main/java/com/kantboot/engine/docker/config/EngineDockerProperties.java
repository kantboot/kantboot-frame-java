package com.kantboot.engine.docker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "kantboot.engine.docker")
public class EngineDockerProperties {

    /**
     * Docker 守护进程连接地址
     * 默认为本地 Unix Socket
     */
    private String host = "unix:///var/run/docker.sock";

    /**
     * 连接超时时间（毫秒）
     */
    private int connectTimeout = 5000;

    /**
     * 读取超时时间（毫秒）
     */
    private int readTimeout = 30000;

    /**
     * 是否启用 TLS
     */
    private boolean tlsVerify = false;

    /**
     * TLS 证书路径（启用 TLS 时必填）
     */
    private String certPath;

    /**
     * Docker API 版本
     */
    private String apiVersion = "";

    /**
     * 镜像仓库认证配置
     */
    private Registry registry = new Registry();

    @Data
    public static class Registry {
        /**
         * 仓库地址
         */
        private String url = "https://index.docker.io/v1/";

        /**
         * 用户名
         */
        private String username;

        /**
         * 密码
         */
        private String password;

        /**
         * 邮箱
         */
        private String email;
    }

}
