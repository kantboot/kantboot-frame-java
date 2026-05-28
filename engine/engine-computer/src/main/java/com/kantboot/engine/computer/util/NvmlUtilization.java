package com.kantboot.engine.computer.util;

import com.sun.jna.Structure;

import java.util.List;

// 定义结构体
public class NvmlUtilization extends Structure {
    /**
     * GPU利用率，单位百分比
     */
    public int gpu;
    /**
     * 显存利用率，单位百分比
     */
    public int memory;

    // 必须实现此方法，返回字段名称列表（顺序与C结构体一致）
    @Override
    protected List<String> getFieldOrder() {
        return List.of("gpu", "memory");
    }
}
