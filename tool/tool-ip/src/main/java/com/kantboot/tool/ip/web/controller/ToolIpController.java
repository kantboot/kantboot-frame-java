package com.kantboot.tool.ip.web.controller;

import com.kantboot.tool.ip.service.IToolIpService;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tool-ip-web/ip")
public class ToolIpController {

    @Resource
    private IToolIpService service;

    @RequestMapping("/getByIp")
    public RestResult<?> getByIp(@RequestParam("ip") String ip) {
        return RestResult.success(service.getByIp(ip), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/getByIpSelf")
    public RestResult<?> getByIpSelf() {
        return RestResult.success(service.getByIpSelf(), CommonSuccessStateConsts.GET_SUCCESS);
    }


}
