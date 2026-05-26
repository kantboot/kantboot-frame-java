package com.kantboot.util.resolver.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

/**
 * 该类用于配置FastJson，提供了一些参数和行为的配置选项，方便开发者对JSON数据进行处理。
 * 这里列出了几个主要的配置项：
 * 1. 设置时区为GMT标准时间，以统一时间格式。
 * 2. 自定义FastJsonConfig，设置字符集为UTF-8，并指定日期格式为"millis"。
 * 3. 配置序列化和反序列化的行为，例如写入空值以及将空字符串视作null。
 * 4. 使用FastJsonHttpMessageConverter替换Spring MVC默认的HttpMessageConverter，以提高JSON序列化和反序列化速度。
 */
@Component
@Configuration
public class KantbootJsonConfig implements WebMvcConfigurer {

    /**
     * 在初始化时设置时区为GMT标准时间，以统一时间格式。
     */
    @PostConstruct
    public void setTimeZone(){
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        TimeZone.setDefault(timeZone);
    }

    /**
     * 自定义FastJson配置
     * @return FastJsonConfig对象
     */
    @Bean
    public FastJsonConfig fastJsonConfig() {
        FastJsonConfig config = new FastJsonConfig();
        config.setCharset(StandardCharsets.UTF_8);
        config.setDateFormat("millis");

        // 配置序列化的行为
        config.setWriterFeatures(
                JSONWriter.Feature.WriteNulls,
                JSONWriter.Feature.WriteLongAsString
        );
        // 配置反序列化的行为，将空字符串视作null
        config.setReaderFeatures(JSONReader.Feature.FieldBased, JSONReader.Feature.SupportArrayToBean);

        return config;
    }

    /**
     *  使用 FastJsonHttpMessageConverter 来替换 Spring MVC 默认的 HttpMessageConverter
     *  以提高 @RestController @ResponseBody @RequestBody 注解的 JSON序列化和反序列化速度。
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        converter.setFastJsonConfig(fastJsonConfig());
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        converters.addFirst(converter);
        JSON.parseObject("", Object.class);
    }
}
