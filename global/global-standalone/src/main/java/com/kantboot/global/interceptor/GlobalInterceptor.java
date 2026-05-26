package com.kantboot.global.interceptor;

import com.alibaba.fastjson2.JSON;
import com.kantboot.global.util.GlobalWebControllerLogUtil;
import com.kantboot.util.http.HttpRequestHeaderUtil;
import com.kantboot.util.rest.exception.BaseException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.math.BigDecimal;


@Slf4j
@Component
public class GlobalInterceptor implements HandlerInterceptor {

    @Resource
    private GlobalInterceptorUtil globalInterceptorUtil;

    @Resource
    private GlobalWebControllerLogUtil globalWebControllerLogUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        globalInterceptorUtil.getLocation();
        // 开始时间
        long startTime = System.nanoTime();
        byte flag = globalInterceptorUtil.checkPass(request, response, handler);
        // 结束时间
        long endTime = System.nanoTime();
        // 计算耗时，单位为毫秒
        long duration = (endTime - startTime);
        log.info("请求URI: {}，处理时间: {} ms", request.getRequestURI(), new BigDecimal(duration).divide(new BigDecimal(1_000_000)));
        if(flag==1){
            return true;
        }
        if(flag==2){
            return false;
        }
        // 提示没有权限
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(JSON.toJSONString(new BaseException().exceptionHandler(
                BaseException.of("noPermission", "没有权限", "zh_CN")
        )));
        globalWebControllerLogUtil.addWebControllerLog(null,"noPermission");
        return false;
    }


}












