package com.kantboot.util.resolver.resolver;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义参数解析器
 */
@Configuration
public class ConfigArgumentResolvers {
    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    public ConfigArgumentResolvers(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        this.requestMappingHandlerAdapter = requestMappingHandlerAdapter;
    }

    /**
     * springBoot启动的时候执行
     */
    @PostConstruct
    private void addArgumentResolvers() {
        // 获取到框架定义好的参数解析集合
        List<HandlerMethodArgumentResolver> argumentResolvers =
                requestMappingHandlerAdapter.getArgumentResolvers();
        KantbootHandlerMethodArgumentResolver myHandlerMethodArgumentResolver = getMyHandlerMethodArgumentResolver(argumentResolvers);
        // ha.getArgumentResolvers()获取到的是不可变的集合,所以我们需要新建一个集合来放置参数解析器
        List<HandlerMethodArgumentResolver> myArgumentResolvers =
                new ArrayList<>(argumentResolvers.size() + 1);
        //这里有一个注意点就是自定义的处理器需要放在RequestParamMethodArgumentResolver前面
        //为什么呢?因为如果放在它后面的话,那么它已经处理掉了,就到不了我们自己定义的策略里面去了
        //所以直接把自定义的策略放在第一个,稳妥!
        // 将自定义的解析器，放置在第一个； 并保留原来的解析器
        myArgumentResolvers.add(myHandlerMethodArgumentResolver);
        myArgumentResolvers.addAll(argumentResolvers);
        //再把新集合设置进去
        requestMappingHandlerAdapter.setArgumentResolvers(myArgumentResolvers);
    }

    /**
     * 获取MyHandlerMethodArgumentResolver实例
     */
    private KantbootHandlerMethodArgumentResolver getMyHandlerMethodArgumentResolver(
            List<HandlerMethodArgumentResolver> argumentResolversList) {
        // 原本处理RequestParam的类
        RequestParamMethodArgumentResolver requestParamMethodArgumentResolver = null;

        if (argumentResolversList == null) {
            throw new RuntimeException("argumentResolverList must not be null!");
        }
        for (HandlerMethodArgumentResolver argumentResolver : argumentResolversList) {
            if (requestParamMethodArgumentResolver != null) {
                break;
            }
            if (argumentResolver instanceof RequestParamMethodArgumentResolver) {
                // 因为在我们自己策略里面是还需要用到这个原本的类的,所以需要得到这个对象实例
                requestParamMethodArgumentResolver = (RequestParamMethodArgumentResolver) argumentResolver;
            }
        }
        if (requestParamMethodArgumentResolver == null) {
            throw new RuntimeException("RequestParamMethodArgumentResolver not be null!");
        }
        //实例化自定义参数解析器
        return new KantbootHandlerMethodArgumentResolver(requestParamMethodArgumentResolver);
    }
}