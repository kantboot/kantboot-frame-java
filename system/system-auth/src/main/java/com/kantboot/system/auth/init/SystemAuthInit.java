package com.kantboot.system.auth.init;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.kantboot.system.auth.domain.entity.SysAuthUri;
import com.kantboot.system.auth.service.ISysAuthUriService;
import com.kantboot.util.auth.annotation.AuthInit;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SystemAuthInit {

    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Resource
    private ISysAuthUriService uriService;

    @PostConstruct
    public void init() {
        System.out.println("初始化：获取所有Spring Boot的URI及注解信息");

        // 获取所有注册的请求映射及其对应的处理方法
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        List<SysAuthUri> authUriList = new ArrayList<>();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();

            // 获取URI
            List<String> uris = extractUrisFromRequestMappingInfo(info);

            // 获取类和方法上的AuthManager注解
            AuthInit classAuth = handlerMethod.getBeanType().getAnnotation(AuthInit.class);
            AuthInit methodAuth = handlerMethod.getMethod().getAnnotation(AuthInit.class);

            // 合并属性
            String className = (classAuth != null && !classAuth.name().isEmpty()) ? classAuth.name() : "default";
            String methodName = (methodAuth != null && !methodAuth.name().isEmpty()) ? methodAuth.name() : "default";
            String combinedName = className + "-" + methodName;

            String description = methodAuth != null && !methodAuth.description().isEmpty()
                    ? methodAuth.description()
                    : (classAuth != null ? classAuth.description() : "");
            String languageCode = methodAuth != null && !methodAuth.sourceLanguageCode().isEmpty()
                    ? methodAuth.sourceLanguageCode()
                    : (classAuth != null ? classAuth.sourceLanguageCode() : "");

            // 获取方法上的noNeedLogin属性，如果没有则直接为false（默认为false）
            Boolean noNeedLogin = methodAuth != null && methodAuth.noNeedLogin();
            // 获取方法上的allPass属性，如果没有则直接为false（默认为false）
            Boolean allPass = methodAuth != null && methodAuth.allPass();

            // 为每个URI创建SysAuthUri对象
            for (String uri : uris) {
                if (StrUtil.isEmpty(uri)) continue;

                SysAuthUri sysAuthUri = new SysAuthUri();
                sysAuthUri.setUri(uri);
                sysAuthUri.setName(combinedName);
                sysAuthUri.setDescription(description);
                sysAuthUri.setSourceLanguageCode(languageCode);
                sysAuthUri.setNoNeedLogin(noNeedLogin);
                sysAuthUri.setAllPass(allPass);
                authUriList.add(sysAuthUri);
            }
        }

        // 异步保存到数据库
        ThreadUtil.execute(() -> {
            for (SysAuthUri authUri : authUriList) {
                SysAuthUri existing = uriService.getByUri(authUri.getUri());
                if (existing != null) {
//                    System.out.print("\033[36m");
//                    System.out.printf("Spring Boot的URI及注解信息, [已存在: %s]%n",authUri.getUri());
//                    System.out.print("\033[0m");
                } else {
                    uriService.save(authUri);
                    System.out.print("\033[32m");
                    System.out.printf("新增URI: %s [名称: %s]%n", authUri.getUri(), authUri.getName());
                    System.out.print("\033[0m");
                }
            }
        });
    }

    // 提取URI（兼容新旧Spring Boot版本）
    private List<String> extractUrisFromRequestMappingInfo(RequestMappingInfo info) {
        List<String> uris = new ArrayList<>();
        if (info.getPathPatternsCondition() != null) {
            info.getPathPatternsCondition().getPatterns()
                    .forEach(pattern -> uris.add(pattern.getPatternString()));
        } else if (info.getPatternsCondition() != null) {
            uris.addAll(info.getPatternsCondition().getPatterns());
        }
        return uris;
    }
}