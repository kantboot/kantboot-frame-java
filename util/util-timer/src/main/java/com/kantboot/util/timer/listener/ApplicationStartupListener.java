package com.kantboot.util.timer.listener;

import com.kantboot.util.timer.manager.TimerManager;
import jakarta.annotation.Resource;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 应用启动完成监听器
 * 当Spring Boot应用完全启动后自动启动所有定时器
 *
 * @author 方某方
 */
@Component
public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    @Resource
    private TimerManager timerManager;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("=== Application Ready - Starting Timers ===");
        try {
            // 应用完全启动后启动定时器
            timerManager.startAllTimers();
            System.out.println("All timers started after application ready!");
        } catch (Exception e) {
            System.err.println("Failed to start timers after application ready: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("===========================================");
    }
}
