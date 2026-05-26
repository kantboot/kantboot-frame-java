package com.kantboot.tool.ip.slot;

import com.kantboot.tool.ip.domain.entity.ToolIp;
import com.kantboot.util.rest.exception.BaseException;
import org.springframework.stereotype.Component;

@Component
public class ToolIpSlot {

    public ToolIp getByIp(String ip) {
        // 提示工具ip模块未使用ip插件
        throw BaseException.of("toolIpSlotNotUseIpPlugin", "tool-ip模块未使用ip插件", "zh_CN");
    }

}
