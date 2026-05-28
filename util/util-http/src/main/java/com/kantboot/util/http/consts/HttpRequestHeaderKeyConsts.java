package com.kantboot.util.http.consts;

/**
 * HTTP请求头中使用的键的常量。
 * 这些键用于方便统一访问特定的请求头字段。
 * 包含了最常用的HTTP请求头字段，如token、authorization、语言代码等。
 * 通过定义这些常量来规范化请求头字段的使用，避免字符串硬编码。
 */
public class HttpRequestHeaderKeyConsts {

    /**
     * token字段
     * Token field
     */
    public final static String TOKEN = "token";

    /**
     * authorization字段
     * Authorization field
     */
    public final static String AUTHORIZATION = "authorization";

    /**
     * kantboot_memory字段
     * Kantboot memory field
     */
    public final static String KANTBOOT_MEMORY = "kantbootMemory";

    /**
     * 语言编码字段
     * Language code field
     * 从请求头中获取语言编码字段的值，如果没有语言编码字段则返回null
     * Retrieve the value of the language code field from the request header, return null if the language code field is not present
     */
    public final static String LANGUAGE_CODE = "languageCode";

    /**
     * 场景编码字段
     * Scene code field
     * 从请求头中获取场景编码字段的值，如果没有场景编码字段则返回null
     * Retrieve the value of the scene code field from the request header, return null if the scene code field is not present
     */
    public final static String SCENE_CODE = "sceneCode";

    /**
     * 地理位置编码字段
     */
    public final static String GEO = "geo";

}
