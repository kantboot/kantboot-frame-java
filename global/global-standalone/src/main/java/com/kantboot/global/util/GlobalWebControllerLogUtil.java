package com.kantboot.global.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.kantboot.global.dao.repository.GlobalWebControllerLogRepository;
import com.kantboot.global.domain.entity.GlobalWebControllerLog;
import com.kantboot.user.account.domain.vo.LoginVO;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.util.http.HttpRequestHeaderUtil;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import ua_parser.Client;
import ua_parser.Parser;

import java.io.BufferedReader;
import java.io.IOException;

@Component
public class GlobalWebControllerLogUtil {

    @Resource
    private HttpRequestHeaderUtil httpRequestHeaderUtil;

    @Resource
    private HttpServletRequest httpServletRequest;

    @Resource
    private GlobalWebControllerLogRepository globalWebLogRepository;

    @Resource
    private IUserAccountService userAccountService;

    public void addWebControllerLog(Object body,String interceptType) {
        String requestURI = httpServletRequest.getRequestURI();
        String token = httpRequestHeaderUtil.getToken();
        String ip = httpRequestHeaderUtil.getIp();
        String userAgent = httpRequestHeaderUtil.getUserAgent();

        GlobalWebControllerLog globalWebLog = new GlobalWebControllerLog();
        // 获取请求URI
        globalWebLog.setRequestUri(requestURI);
        globalWebLog.setRequestMethod(httpServletRequest.getMethod());
        globalWebLog.setRequestParams(httpServletRequest.getQueryString());
        globalWebLog.setRequestContentType(httpServletRequest.getContentType());
        globalWebLog.setRequestIp(ip);
        globalWebLog.setRequestUserAgent(userAgent);
        globalWebLog.setRequestToken(token);
        globalWebLog.setRequestLanguageCode(httpRequestHeaderUtil.getLanguageCode());
        globalWebLog.setRequestMethod(httpServletRequest.getMethod());
        Long selfId = userAccountService.getSelfIdNoThrow();
        globalWebLog.setUserAccountIdOfRequest(selfId);
        globalWebLog.setIsLogin(selfId!=null);
        globalWebLog.setIsIntercepted(false);
        if(StrUtil.isNotEmpty(interceptType)){
            globalWebLog.setIsIntercepted(true);
            globalWebLog.setInterceptType(interceptType);
        }


        boolean isRestResult = body instanceof RestResult<?>;
        if(isRestResult){
            BufferedReader reader = null;
            // 获取body请求参数
            try {
                reader = httpServletRequest.getReader();
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                globalWebLog.setRequestBody(sb.toString());
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Thread.ofVirtual()
                .name("save-global-web-log-thread")
                .start(() -> {
                    globalWebLog.setRequestBrowser(getBrowser(userAgent));
                    globalWebLog.setRequestBrowserVersion(getBrowserVersion(userAgent));
                    globalWebLog.setRequestOs(getOs(userAgent));
                    globalWebLog.setRequestOsVersion(getOsVersion(userAgent));
                    if(isRestResult&& body!=null){
                        if(body instanceof RestResult<?> restResult){
                            Object data = restResult.getData();
                            if(data instanceof LoginVO){
                                LoginVO loginVO = (LoginVO) data;
                                globalWebLog.setUserAccountIdOfRequest(loginVO.getUserAccount().getId());
                            }
                        }
                        globalWebLog.setResult(JSON.toJSONString(body, "millis"));
                    }
                    globalWebLogRepository.save(globalWebLog);
                });
    }


    /**
     * 获取对应的浏览器
     */
    public String getBrowser(String userAgent) {
        if(userAgent == null || userAgent.isEmpty()) {
            return null;
        }
        Parser uaParser = new Parser();
        Client client = uaParser.parse(userAgent);
        if(client != null && client.userAgent != null) {
            return client.userAgent.family;
        }
        return null;
    }

    /**
     * 获取浏览器版本
     */
    public String getBrowserVersion(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return null;
        }
        Parser uaParser = new Parser();
        Client client = uaParser.parse(userAgent);
        if (client != null && client.userAgent != null) {
            StringBuilder version = new StringBuilder();
            if (client.userAgent.major != null) {
                version.append(client.userAgent.major);
                if (client.userAgent.minor != null) {
                    version.append(".").append(client.userAgent.minor);
                    if (client.userAgent.patch != null) {
                        version.append(".").append(client.userAgent.patch);
                    }
                }
                return version.toString();
            }
        }
        return null;
    }

    /**
     * 获取操作系统
     */
    public String getOs(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return null;
        }
        Parser uaParser = new Parser();
        Client client = uaParser.parse(userAgent);
        if (client != null && client.os != null) {
            return client.os.family;
        }
        return null;
    }

    /**
     * 获取操作系统版本
     */
    public String getOsVersion(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return null;
        }
        Parser uaParser = new Parser();
        Client client = uaParser.parse(userAgent);
        if (client != null && client.os != null) {
            StringBuilder version = new StringBuilder();
            if (client.os.major != null) {
                version.append(client.os.major);
                if (client.os.minor != null) {
                    version.append(".").append(client.os.minor);
                    if (client.os.patch != null) {
                        version.append(".").append(client.os.patch);
                    }
                }
                return version.toString();
            }
        }
        return null;
    }

}
