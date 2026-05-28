package com.kantboot.global.adapter;

import com.alibaba.fastjson2.JSON;
import com.kantboot.global.dao.repository.GlobalWebControllerLogRepository;
import com.kantboot.global.domain.entity.GlobalWebControllerLog;
import com.kantboot.global.util.GlobalWebControllerLogUtil;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.util.http.HttpRequestHeaderUtil;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import ua_parser.Client;
import ua_parser.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@ControllerAdvice
public class GlobalResponseBodyAdviceAdapter implements ResponseBodyAdvice<Object> {

    @Resource
    private GlobalWebControllerLogUtil globalWebLogUtil;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 开始时间，纳秒
        long startTime = System.nanoTime();

        globalWebLogUtil.addWebControllerLog(body,null);

        // 结束时间，纳秒
        long endTime = System.nanoTime();
        long duration = endTime - startTime; // 持续时间，纳秒
        log.info("响应速度: " + new BigDecimal(duration).divide(new BigDecimal(1_000_000)) + " ms");


        return body;
    }





}