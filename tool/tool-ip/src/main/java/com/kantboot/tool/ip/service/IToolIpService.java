package com.kantboot.tool.ip.service;

import com.kantboot.tool.ip.domain.entity.ToolIp;

public interface IToolIpService {

    /**
     * 根据ip地址获取ip信息
     * @param ip ip地址
     * @return ip信息
     */
    ToolIp getByIp(String ip);

    ToolIp getByIpSelf();

}
