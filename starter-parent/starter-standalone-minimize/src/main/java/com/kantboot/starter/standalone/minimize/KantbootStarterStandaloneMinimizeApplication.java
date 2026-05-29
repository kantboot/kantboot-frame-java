package com.kantboot.starter.standalone.minimize;

import com.kantboot.util.scan.annotation.KantbootScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAspectJAutoProxy
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
@EnableCaching
@EnableJpaAuditing
@EntityScan(basePackages = {"com.kantboot"})
@EnableJpaRepositories(basePackages = {"com.kantboot"})
@ServletComponentScan(basePackages = {"com.kantboot"})
@SpringBootApplication(scanBasePackages = {"com.kantboot"},exclude = {GsonAutoConfiguration.class, CacheAutoConfiguration.class})
@KantbootScan("com.kantboot")
@EnableRedisRepositories(basePackages = {"com.kantboot"})
public class KantbootStarterStandaloneMinimizeApplication {

    public static void main(String[] args) {
        SpringApplication.run(KantbootStarterStandaloneMinimizeApplication.class, args);
    }

}
