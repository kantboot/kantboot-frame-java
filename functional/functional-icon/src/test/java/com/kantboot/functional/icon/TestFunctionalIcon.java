package com.kantboot.functional.icon;

import com.alibaba.fastjson2.JSON;
import com.kantboot.functional.icon.domain.entity.FunctionalIcon;
import com.kantboot.functional.icon.init.FunctionalIconInit;
import com.kantboot.test.application.TestApplication;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = TestApplication.class)
public class TestFunctionalIcon {

    @Resource
    private FunctionalIconInit functionalIconInit;

    @Test
    public void testFunctionalIconInit() {
        List<FunctionalIcon> functionalIcons = functionalIconInit.getFunctionalIcons("functional-icon/heroicons.json");
        System.out.println(JSON.toJSONString(functionalIcons));
    }

}
