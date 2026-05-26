package com.kantboot.util.http;

import com.kantboot.util.http.domain.config.HttpSendConfig;
import org.junit.Test;

public class TestHttpSendUtil {

    @Test
    public void testSendGet() {
        HttpSendConfig dto = new HttpSendConfig()
                .setUrl("https://www.ipplus360.com/getLocation")
                .setMethod("GET")
                .setContentType("application/x-www-form-urlencoded");
        String send = HttpSendUtil.send(dto);
        System.out.print(send);
    }

}