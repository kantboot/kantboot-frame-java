package com.kantboot.test.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class DatabaseInfoController {

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping("/test")
    public String getDatabaseInfo() {
        return "Database information is not available in this test controller.";
    }


}