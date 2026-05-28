package com.kantboot.thirdparty.juhe.service;

import com.alibaba.fastjson2.JSON;
import com.kantboot.test.application.TestApplication;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest(classes = TestApplication.class)
public class TestThirdPartyJuheRequest {

    @Resource
    private IThirdpartyJuheRequestService service;

    @Test
    public void testExecuteRequest() {
        Object o = service.execute("exchangeRate", Map.of(
                "from", "USD",
                "to", "CNY"
        ));
        System.out.println(JSON.toJSONString(o));

    }
}
