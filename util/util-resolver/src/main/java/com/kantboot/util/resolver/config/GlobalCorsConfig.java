package com.kantboot.util.resolver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置
 * Cross-domain configuration
 */
@Configuration
public class GlobalCorsConfig implements WebMvcConfigurer {


    /**
     * 跨域配置
     * Cross-domain configuration
     * 允许了所有的请求来源跨域访问，允许所有的请求头跨域，允许所有的请求方法跨域
     * Allowed all request sources to access cross-domain,
     * allowed all request headers to cross-domain,
     * allowed all request methods to cross-domain
     *
     * @param registry 跨域配置
     *                 Cross-domain configuration
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("token", "uid", "Content-Disposition")
                .allowCredentials(false)
                .maxAge(3600);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // 允许所有请求来源
        // Allow all request sources
        config.addAllowedOrigin("*");
        // 允许所有请求头
        // Allow all request headers
        config.addAllowedHeader("*");
        // 允许所有请求方法
        // Allow all request methods
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}

