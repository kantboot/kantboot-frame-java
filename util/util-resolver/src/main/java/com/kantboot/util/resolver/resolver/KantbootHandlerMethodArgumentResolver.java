package com.kantboot.util.resolver.resolver;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

/**
 * 自定义的参数解析器
 * <p>
 *     用于解析参数前有@RequestParam注解的参数
 *     注:此解析器只是为了解决@RequestParam注解的参数无法解析json字符串的问题
 *     例如:前端传递的参数为:{"name":"kant","age":18}
 *     此时,如果参数前有@RequestParam注解,那么就会报错，因为@RequestParam注解无法解析json字符串
 *     此时,就可以使用此解析器来解析json字符串
 *     只要启动类上加上注解:@EnableWebMvc，并扫描此解析器所在的包即可
 * </p>
 */
@Slf4j
public class KantbootHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {


    /**
     * 这个是处理@RequestParam注解的原本策略类
     */
    private final RequestParamMethodArgumentResolver requestParamMethodArgumentResolver;

    /**
     * 全参构造
     */
    public KantbootHandlerMethodArgumentResolver(RequestParamMethodArgumentResolver requestParamMethodArgumentResolver) {
        this.requestParamMethodArgumentResolver = requestParamMethodArgumentResolver;
    }

    /**
     * 流转字符串
     * @param bf 流
     * @return 字符串
     */
    private static String getRead(BufferedReader bf) {
        StringBuilder sb = new StringBuilder();
        try {
            char[] buff = new char[1024];
            int len;
            while ((len = bf.read(buff)) != -1) {
                sb.append(buff, 0, len);
            }
        } catch (IOException e) {
            log.error("流转字符串失败", e);
        }
        return sb.toString();
    }

    /**
     * 当参数前有@RequestParam注解时，会使用此 解析器
     * <p>
     * 注:此方法的返回值将决定:是否使用此解析器解析该参数
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //很明显,就是判断是否有这个注解
        return methodParameter.hasParameterAnnotation(RequestParam.class);
    }

    /**
     * 解析参数
     */
    @Override
    public Object resolveArgument(@NonNull MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
            NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory)
            throws Exception {
        final String applicationJson = "application/json";
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new RuntimeException(" request must not be null!");
        }
        //获取到内容类型
        String contentType = request.getContentType();
        //如果类型是属于json 那么则跑自己解析的方法
        if (null != contentType && contentType.contains(applicationJson)) {
            //获取参数名称
            String parameterName = methodParameter.getParameterName();
            //获取参数类型
            Class<?> parameterType = methodParameter.getParameterType();
            //因为json数据是放在流里面,所以要去读取流,
            //但是ServletRequest的getReader()和getInputStream()两个方法只能被调用一次，而且不能两个都调用。
            //所以这里是需要写个自定义的HttpServletRequestWrapper,主要功能就是需要重复读取流数据
            String read = getRead(request.getReader());
            //转换json
            JSONObject jsonObject = JSON.parseObject(read);
            Object o1;
            if (jsonObject == null) {
                //这里有一个可能性就是比如get请求,参数是拼接在URL后面,但是如果我们还是去读流里面的数据就会读取不到
                Map<String, String[]> parameterMap = request.getParameterMap();
                o1 = parameterMap.get(parameterName);
            } else {
                o1 = jsonObject.get(parameterName);
            }
            Object arg = null;
            //如果已经获取到了值的话那么再做类型转换
            if (o1 != null) {
                assert parameterName != null;
                WebDataBinder binder = webDataBinderFactory.createBinder(nativeWebRequest, null, parameterName);
                arg = binder.convertIfNecessary(o1, parameterType, methodParameter);
            }
            return arg;
        }
        //否则跑原本的策略类.
        return requestParamMethodArgumentResolver.resolveArgument(methodParameter,
                modelAndViewContainer, nativeWebRequest, webDataBinderFactory);
    }
}
