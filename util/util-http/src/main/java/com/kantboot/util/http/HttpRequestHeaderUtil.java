package com.kantboot.util.http;

import com.kantboot.util.http.consts.HttpRequestHeaderKeyConsts;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.stereotype.Component;
import ua_parser.Client;
import ua_parser.Parser;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP请求头工具类
 * HTTP Request Header Utility Class
 * 提供从请求头中获取各种信息的方法
 * Provides methods to retrieve various information from the request headers
 */
@Component
@Data
public class HttpRequestHeaderUtil {

    @Resource
    private HttpServletRequest request;

    /**
     * 用于设备型号获取的map
     * Map for retrieving device models
     */
    private final Map<String, String> deviceMap;

    public HttpRequestHeaderUtil(HttpServletRequest request) {
        this.request = request;
        // 初始化设备型号map
        // Initialize device model map
        deviceMap = new HashMap<>();
        deviceMap.put("Android", "Build");
        deviceMap.put("iPhone", "CPU");
        deviceMap.put("iPad", "CPU");
        deviceMap.put("iPod", "CPU");
        deviceMap.put("Windows Phone", "ARM");
        deviceMap.put("MQQBrowser", "ARM");
        deviceMap.put("Windows CE", "ARM");
        deviceMap.put("Windows NT", "Windows NT");
        deviceMap.put("Mac OS X", "Mac OS X");
        deviceMap.put("Linux", "Linux");
        deviceMap.put("X11", "X11");
        deviceMap.put("Chrome OS", "CrOS");
    }

    /**
     * 获取token
     * Retrieve token
     * 从请求头中获取token字段的值，如果没有token字段则返回null
     * Retrieve the value of the token field from the request header, return null if the token field is not present
     *
     * @return token
     */
    public String getToken() {
        return request.getHeader(HttpRequestHeaderKeyConsts.TOKEN);
    }

    /**
     * 获取authorization
     * Retrieve authorization
     * 从请求头中获取authorization字段的值，如果没有authorization字段则返回null
     * Retrieve the value of the authorization field from the request header, return null if the authorization field is not present
     *
     * @return authorization
     */
    public String getAuthorization() {
        return request.getHeader(HttpRequestHeaderKeyConsts.AUTHORIZATION);
    }

    /**
     * 获取kantboot_memory
     * Retrieve kantboot_memory
     * 从请求头中获取kantboot_memory字段的值，如果没有kantboot_memory字段则返回null
     * Retrieve the value of the kantboot_memory field from the request header, return null if the kantboot_memory field is not present
     *
     * @return kantboot_memory
     */
    public String getKantbootMemory() {
        return request.getHeader(HttpRequestHeaderKeyConsts.KANTBOOT_MEMORY);
    }

    /**
     * 获取IP地址
     * Retrieve IP address
     * 从请求头中获取IP地址，如果没有IP地址则返回null
     * Retrieve the IP address from the request header, return null if the IP address is not present
     *
     * @return IP地址
     */
    public String getIp() {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 从请求头中获取User-Agent
     * Retrieve User-Agent from request header
     *
     * @return User-Agent
     */
    public String getUserAgent() {
        return request.getHeader("User-Agent");
    }

    /**
     * 从User-Agent中获取设备信息
     * Retrieve device information from User-Agent
     *
     * @return 设备信息，如果没有找到则返回null
     * Device information, return null if not found
     */
    public String getDevice() {
        String userAgent = getUserAgent();
        if (userAgent == null || userAgent.isEmpty()) {
            return null;
        }

        for (Map.Entry<String, String> entry : deviceMap.entrySet()) {
            String deviceKeyword = entry.getKey();
            String deviceName = entry.getValue();
            if (userAgent.contains(deviceKeyword) && userAgent.contains(deviceName)) {
                return deviceName;
            }
        }
        return null;
    }

    /**
     * 获取请求头中的语言编码
     * Retrieve language code from request header
     *
     * @return 语言编码，如果没有则返回默认值-"en"
     * Language code, return default value "en" if not present
     */
    public String getLanguageCode() {
        String header = request.getHeader(HttpRequestHeaderKeyConsts.LANGUAGE_CODE);
        // 根据Accept-Language头部进行判断
        if(header == null || header.isEmpty()) {
            header = request.getHeader("Accept-Language");
        }
        if(header!=null && !header.isEmpty()) {
            String[] languages = header.split(",");
            if (languages.length > 0) {
                return languages[0].trim().replace("-", "_");
            }
        }
        if (header == null || header.isEmpty()) {
            return "en";
        }
        return header;
    }

    /**
     * 获取请求头中的场景编码
     * Retrieve scene code from request header
     *
     * @return 场景编码
     * Scene code
     */
    public String getSceneCode() {
        return request.getHeader(HttpRequestHeaderKeyConsts.SCENE_CODE);
    }

    /**
     * 获取请求头中的userId
     * Retrieve userId from request header
     *
     * @return userId
     */
    public Long getUserId() {
        String userId = request.getHeader("userId");
        if (userId == null || userId.isEmpty()) {
            return null;
        }
        return Long.valueOf(userId);
    }

    /**
     * 获取请求头中的projectCode
     * Retrieve projectCode from request header
     *
     * @return projectCode
     */
    public String getProjectCode() {
        return request.getHeader("projectCode");
    }

    /**
     * 获取对应的浏览器
     */
    public String getBrowser() {
        String userAgent = getUserAgent();
        if(userAgent == null || userAgent.isEmpty()) {
            return null;
        }
        Parser uaParser = new Parser();
        Client client = uaParser.parse(userAgent);
        if(client != null && client.userAgent != null) {
            return client.userAgent.family;
        }
        return null;
    }

    /**
     * 获取浏览器版本
     */
    public String getBrowserVersion() {
        String userAgent = getUserAgent();
        if (userAgent == null || userAgent.isEmpty()) {
            return null;
        }
        Parser uaParser = new Parser();
        Client client = uaParser.parse(userAgent);
        if (client != null && client.userAgent != null) {
            StringBuilder version = new StringBuilder();
            if (client.userAgent.major != null) {
                version.append(client.userAgent.major);
                if (client.userAgent.minor != null) {
                    version.append(".").append(client.userAgent.minor);
                    if (client.userAgent.patch != null) {
                        version.append(".").append(client.userAgent.patch);
                    }
                }
                return version.toString();
            }
        }
        return null;
    }

    /**
     * 获取操作系统
     */
    public String getOs() {
        String userAgent = getUserAgent();
        if (userAgent == null || userAgent.isEmpty()) {
            return null;
        }
        Parser uaParser = new Parser();
        Client client = uaParser.parse(userAgent);
        if (client != null && client.os != null) {
            return client.os.family;
        }
        return null;
    }

    /**
     * 获取操作系统版本
     */
    public String getOsVersion() {
        String userAgent = getUserAgent();
        if (userAgent == null || userAgent.isEmpty()) {
            return null;
        }
        Parser uaParser = new Parser();
        Client client = uaParser.parse(userAgent);
        if (client != null && client.os != null) {
            StringBuilder version = new StringBuilder();
            if (client.os.major != null) {
                version.append(client.os.major);
                if (client.os.minor != null) {
                    version.append(".").append(client.os.minor);
                    if (client.os.patch != null) {
                        version.append(".").append(client.os.patch);
                    }
                }
                return version.toString();
            }
        }
        return null;
    }

    /**
     * 获取请求头中的geo
     */
    public String getGeo() {
        return request.getHeader(HttpRequestHeaderKeyConsts.GEO);
    }

    /**
     * 获取经度
     */
    public BigDecimal getLongitude() {
        String geo = getGeo();
        if (geo == null || geo.isEmpty() || !geo.contains(",")) {
            return null;
        }
        String[] parts = geo.split(",");
        if (parts.length < 2) {
            return null;
        }
        try {
            return new BigDecimal(parts[0].trim());
        } catch (NumberFormatException e) {
            return null; // 如果转换失败，返回null
        }
    }

    /**
     * 获取纬度
     */
    public BigDecimal getLatitude() {
        String geo = getGeo();
        if (geo == null || geo.isEmpty() || !geo.contains(",")) {
            return null;
        }
        String[] parts = geo.split(",");
        if (parts.length < 2) {
            return null;
        }
        try {
            return new BigDecimal(parts[1].trim());
        } catch (NumberFormatException e) {
            return null; // 如果转换失败，返回null
        }
    }

    public boolean getAdminCheckEnabled() {
        String adminCheck = request.getHeader("adminCheck");
        return "true".equals(adminCheck);
    }



}
