package com.kantboot.test.starter;


import com.kantboot.util.event.annotation.EventOn;
import com.kantboot.util.event.annotation.EventParam;
import com.kantboot.util.log.Logger;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class TestCom {


    @SneakyThrows
    @EventOn(code="testEvent",name = "测试事件", description = "这是一个测试事件")
    public void testEvent(Logger logger,@EventParam(name = "名称") String name) {
        // 开始时间
        long startTime = System.nanoTime();
        logger.info("测试1={}", name);
        logger.warn("这是一个警告");
        System.out.println("测试事件执行了");
        logger.error("这是一个错误");

        // 结束时间
        long endTime = System.nanoTime();
        logger.info("testEvent: 方法执行时间={}ms", (endTime - startTime)/1000000.0);
        Thread.sleep(3000);

        throw new RuntimeException("test");

    }

}
