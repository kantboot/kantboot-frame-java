package com.kantboot.thirdparty.baidu.translate.util;


import org.junit.jupiter.api.Test;

public class TestBaiduTranslateUtil {

    @Test
    public void testTranslate() {
        String appid = "20230305001586859";
        String key = "9LCopmD6f497X_0HzHIf";
        String q = "hola, ¿cómo estás?";
        String from = null;
        String to = "en";

        String result = BaiduTranslateUtil.translate(q, from, to, appid, key);
        System.out.println(result);
    }

    @Test
    public void detectLanguage() {
        String appid = "20230305001586859";
        String key = "9LCopmD6f497X_0HzHIf";
        String q = "hola";

        String result = BaiduTranslateUtil.detectLanguage(q, appid, key);
        System.out.println(result);
    }

}
