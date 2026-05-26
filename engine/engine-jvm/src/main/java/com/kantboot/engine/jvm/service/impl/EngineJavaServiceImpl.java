package com.kantboot.engine.jvm.service.impl;

import com.kantboot.engine.jvm.domain.entity.EngineJava;
import com.kantboot.engine.jvm.service.IEngineJavaService;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EngineJavaServiceImpl
    implements IEngineJavaService {

    @Override
    public EngineJava getInfo() {
        Properties props = System.getProperties();
        // 获取Java版本信息
        String version = props.getProperty("java.version");
        // 获取Java运行时版本信息
        String runtimeVersion = props.getProperty("java.runtime.version");
        // 获取Java虚拟机版本信息
        String vmVersion = props.getProperty("java.vm.version");
        // 获取Java供应商信息
        String vendor = props.getProperty("java.vendor");
        // 获取Java安装目录
        String home = props.getProperty("java.home");
        // 获取Java类版本信息
        String classVersion = props.getProperty("java.class.version");

        EngineJava engineJava = new EngineJava();
        engineJava.setVersion(version);
        engineJava.setRuntimeVersion(runtimeVersion);
        engineJava.setVmVersion(vmVersion);
        engineJava.setVendor(vendor);
        engineJava.setHome(home);
        engineJava.setClassVersion(classVersion);
        return engineJava;
    }
}
