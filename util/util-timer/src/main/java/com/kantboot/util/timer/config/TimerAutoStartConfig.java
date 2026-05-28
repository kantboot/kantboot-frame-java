package com.kantboot.util.timer.config;

import com.kantboot.util.timer.manager.TimerManager;
import jakarta.annotation.Priority;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * 定时器自动启动配置
 * 注意：已禁用，现在使用ApplicationStartupListener进行启动
 * 此配置类用于在应用启动后自动启动所有定时器。
 *
 * @author 方某方
 */
@Configuration
@DependsOn("timerInit")
public class TimerAutoStartConfig {

    @Resource
    private TimerManager timerManager;

    /**
     * 应用启动后自动启动所有定时器
     * 已禁用 - 使用ApplicationStartupListener代替
     */
    // @PostConstruct
    @Priority(10) // 设置较低优先级，确保在TimerInit之后执行
    public void autoStartTimers() {
        System.out.println("=== Timer Auto Start Configuration (DISABLED) ===");
        System.out.println("Timer startup is now handled by ApplicationStartupListener");
        System.out.println("================================================");
    }
}
