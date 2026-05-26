package com.kantboot.test.starter.main;

import ua_parser.Client;
import ua_parser.Parser;

public class UAParserExample {
    public static void main(String[] args) {
       // 开始时间
        Long startTime = System.currentTimeMillis();
        String userAgentString = "Mozilla/5.0 (Linux; U; Android 15; zh-CN; 24117RK2CC Build/AQ3A.240829.003) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/123.0.6312.80 Quark/7.15.1.890 Mobile Safari/537.36";

        Parser uaParser = new Parser();
        Client client = uaParser.parse(userAgentString);
        Long endTime = System.currentTimeMillis();
        System.out.println("解析耗时: " + (endTime - startTime) + "ms");

        // 输出解析结果
        System.out.println("浏览器: " + client.userAgent.family); // Safari
        System.out.println("浏览器版本: " + client.userAgent.major); // 14
        System.out.println("操作系统: " + client.os.family); // iOS
        System.out.println("设备: " + client.device.family); // iPhone
        System.out.println("操作系统版本: " + client.os.major + "." + client.os.minor + "." + client.os.patch); // 14.0.1
        endTime = System.currentTimeMillis();
        System.out.println("解析耗时: " + (endTime - startTime) + "ms");

        // 判断是否是机器人（爬虫）
        // 注意：ua-parser 主要靠规则匹配，client.device.family 可能是 "Spider" 等，但并非专门字段
        // 更可靠的方式是检查 .userAgent.family 是否包含已知爬虫名称
        boolean isBot = "Spider".equals(client.device.family) || client.userAgent.family.contains("Bot");
        System.out.println("是否是爬虫: " + isBot);
    }
}