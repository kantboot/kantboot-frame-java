package com.kantboot.engine.computer.util;

import com.sun.jna.Structure;

import java.util.List;

public class NvmlMemory extends Structure {
    /**
     * 总显存，单位字节
     */
    public long total;
    /**
     * 空闲显存，单位字节
     */
    public long free;
    /**
     * 已用显存，单位字节
     */
    public long used;

    @Override
    protected List<String> getFieldOrder() {
        return List.of("total", "free", "used");
    }
}