package com.kantboot.tool.ip.service.impl;

import com.kantboot.tool.ip.dao.repository.ToolIpRepository;
import com.kantboot.tool.ip.domain.entity.ToolIp;
import com.kantboot.tool.ip.service.IToolIpService;
import com.kantboot.tool.ip.slot.ToolIpSlot;
import com.kantboot.tool.ip.util.IpUtil;
import com.kantboot.util.http.HttpRequestHeaderUtil;
import com.kantboot.util.rest.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ToolIpServiceImpl
    implements IToolIpService {

    @Resource
    private ToolIpRepository repository;

    @Resource
    private ToolIpSlot slot;

    @Resource
    private HttpRequestHeaderUtil httpRequestHeaderUtil;

    @Override
    public ToolIp getByIp(String ip) {
        if(!IpUtil.isValidIp(ip)){
            throw BaseException.of("invalidIp", "无效的IP地址", "zh_CN");
        }

        ToolIp firstByIp = repository.findFirstByIp(ip);
        if (firstByIp != null) {
            return firstByIp;
        }

        ToolIp byIp = slot.getByIp(ip);
        byIp.setIp(ip);
        if (byIp == null) {
            throw BaseException.of("ipNotFound", "未找到IP信息", "zh_CN");
        }
        return repository.save(byIp);
    }

    @Override
    public ToolIp getByIpSelf() {
        return getByIp(httpRequestHeaderUtil.getIp());
    }

}
