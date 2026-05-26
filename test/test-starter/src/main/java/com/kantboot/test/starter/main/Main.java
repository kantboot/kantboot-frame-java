package com.kantboot.test.starter.main;

import com.alibaba.fastjson2.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
////   获取H:/cped/icons下的所有svg文件，并保存成file.json，字段有code、value，code是文件名不包含后缀，value是文件的内容
//        String pathList = getTextFileValue("H:/cpde/1.json");
//        // 去除所有空格
//        pathList = pathList.replaceAll("\\s+", "");
//        List<Map<String,String>> list = new ArrayList<>();
//        List<String> strings = JSON.parseArray(pathList, String.class);
//        for (String string : strings) {
//            Map<String, String> map = new java.util.HashMap<>();
//            String value = getTextFileValue(string);
//            // 将 <svg viewBox="0 0 24 24" fill="currentColor" xmlns="http://www.w3.org/2000/svg"> 转换为 <svg>
//            value = value.replaceAll("<svg[^>]*>", "<svg>");
//            map.put("code", "remixicon-"+getFileName(string));
//            map.put("content", value);
//            String group = getGroup(string);
//            // 首字母小写
//            group = group.substring(0, 1).toLowerCase() + group.substring(1);
//            map.put("groupCode", group);
//            list.add(map);
//        }
//        String pathList = getTextFileValue("H:/cpde/1.json");
//        // 将list转换为json字符串,并保存到H:/cped/icons/file.json
//        String jsonString = JSON.toJSONString(list);
//        saveTextFileValue("H:/cpde/file.json", jsonString);


        String pathList = getTextFileValue("H:/cpde/1.json");
        // 去除所有空格
        pathList = pathList.replaceAll("\\s+", "");
        List<Map<String,String>> list = new ArrayList<>();
        List<String> strings = JSON.parseArray(pathList, String.class);
        for (String string : strings) {
            Map<String, String> map = new java.util.HashMap<>();
            String value = getTextFileValue(string);
            // 将 <svg viewBox="0 0 24 24" fill="currentColor" xmlns="http://www.w3.org/2000/svg"> 转换为 <svg>
            value = value.replaceAll("<svg[^>]*>", "<svg>");
            map.put("code", "simpleicons-"+getFileName(string));
            map.put("content", value);
            map.put("groupCode", "logos");
            list.add(map);
        }
        String jsonString = JSON.toJSONString(list);
        saveTextFileValue("H:/cpde/file.json", jsonString);

    }

    // 获取文本文件的值
    public static String getTextFileValue(String filePath) {
        try {
            return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filePath)));
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 保存文本文件的值
    public static void saveTextFileValue(String filePath, String content) {
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get(filePath), content.getBytes());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    // 获取文件名
    public static String getFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));
    }

    // 获取分组，分组是文件的上一层路径
    public static String getGroup(String filePath) {
        return filePath.substring("H:/cpde/icons".length()+1, filePath.lastIndexOf("/"));
    }


}
