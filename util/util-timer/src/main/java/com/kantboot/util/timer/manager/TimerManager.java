package com.kantboot.util.timer.manager;

import com.kantboot.util.log.Logger;
import com.kantboot.util.log.domain.LoggerItem;
import com.kantboot.util.timer.domain.dto.TimerDTO;
import com.kantboot.util.timer.domain.dto.TimerOnEndDTO;
import com.kantboot.util.timer.domain.dto.TimerOnInProgressDTO;
import com.kantboot.util.timer.domain.dto.TimerOnStartDTO;
import com.kantboot.util.timer.init.TimerInit;
import com.kantboot.util.timer.instruction.TimerRecordInstruction;
import com.kantboot.util.timer.slot.TimerSlot;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 定时器管理器
 * 用于管理和执行定时任务
 *
 * @author 方某方
 */
@Component
@Slf4j
public class TimerManager {

    private final ScheduledExecutorService executorService;
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final Map<String, ReentrantLock> timerLocks = new ConcurrentHashMap<>();

    public TimerManager() {
        this.executorService = Executors.newScheduledThreadPool(10);
    }

    @Resource
    private TimerSlot timerSlot;

    /**
     * 启动所有定时器
     */
    public void startAllTimers() {
        Map<String, TimerDTO> allTimers = TimerInit.getAllTimers();
        log.info("Starting {} timer(s)...", allTimers.size());

        int successCount = 0;
        int failCount = 0;

        for (Map.Entry<String, TimerDTO> entry : allTimers.entrySet()) {
            try {
                startTimer(entry.getKey());
                successCount++;
            } catch (Exception e) {
                failCount++;
                System.err.println("Failed to start timer " + entry.getKey() + ": " + e.getMessage());
            }
        }

        log.info("Timer startup summary: {} successful, {} failed", successCount, failCount);
    }

    /**
     * 启动指定定时器
     */
    public void startTimer(String timerCode) {
        TimerDTO timer = TimerInit.getTimer(timerCode);
        if (timer == null) {
            System.err.println("Timer not found: " + timerCode);
            return;
        }

        if (timer.isRunning()) {
            log.info("Timer {} is already running", timerCode);
            return;
        }

        // 如果需要锁，创建锁对象
        if (timer.isLock()) {
            timerLocks.putIfAbsent(timerCode, new ReentrantLock());
        }

        ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(
                () -> executeTimer(timer),
                0,
                timer.getTime(),
                TimeUnit.MILLISECONDS
        );

        scheduledTasks.put(timerCode, scheduledFuture);
        TimerInit.updateTimerStatus(timerCode, true);
        log.info("Timer started: {} with interval {}ms", timerCode, timer.getTime());
    }

    /**
     * 停止指定定时器
     */
    public void stopTimer(String timerCode) {
        ScheduledFuture<?> scheduledFuture = scheduledTasks.get(timerCode);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            scheduledTasks.remove(timerCode);
            TimerInit.updateTimerStatus(timerCode, false);
            log.info("Timer stopped: {}", timerCode);
        }
    }

    /**
     * 停止所有定时器
     */
    public void stopAllTimers() {
        for (String timerCode : scheduledTasks.keySet()) {
            stopTimer(timerCode);
        }
    }

    /**
     * 执行定时器任务
     */
    private void executeTimer(TimerDTO timer) {
        String timerCode = timer.getCode();
        long startTime = System.currentTimeMillis();

        try {
            if (timer.isLock()) {
                ReentrantLock lock = timerLocks.get(timerCode);
                if (lock == null) {
                    // 理论上不该发生，兜底避免 NPE 让定时器死掉
                    timerLocks.putIfAbsent(timerCode, new ReentrantLock());
                    lock = timerLocks.get(timerCode);
                }

                // 拿不到就跳过，绝不排队
                boolean acquired = (timer.getLockMaxWaitTime() > 0)
                        ? lock.tryLock(timer.getLockMaxWaitTime(), TimeUnit.MILLISECONDS)
                        : lock.tryLock();

                if (!acquired) return;

                try {
                    TimerInit.updateTimerExecuteInfo(timerCode, startTime);
                    executeTimerMethod(timer);
                } finally {
                    // lockMinContinueTime 你要就补上
                    lock.unlock();
                }
            } else {
                TimerInit.updateTimerExecuteInfo(timerCode, startTime);
                executeTimerMethod(timer);
            }
        } catch (Throwable t) {
            // 关键：不能让异常冒泡到 scheduler，否则后续不再执行
            log.error("Error executing timer {}:", timerCode, t);
        }
    }


    /**
     * 使用锁执行定时器任务
     */
    private void executeWithLock(TimerDTO timer, long startTime) throws Exception {
        String timerCode = timer.getCode();
        ReentrantLock lock = timerLocks.get(timerCode);

        if (lock == null) {
            System.err.println("Lock not found for timer: " + timerCode);
            return;
        }

        boolean acquired = false;
        try {
            // 尝试获取锁
            if (timer.getLockMaxWaitTime() > 0) {
                acquired = lock.tryLock(timer.getLockMaxWaitTime(), TimeUnit.MILLISECONDS);
            } else {
                acquired = lock.tryLock();
            }

            if (acquired) {
                long lockAcquiredTime = System.currentTimeMillis();

                // 执行定时器方法
                executeTimerMethod(timer);

                long executionFinishedTime = System.currentTimeMillis();
                long executionDuration = executionFinishedTime - lockAcquiredTime;

                // 检查是否需要额外等待
                if (timer.getLockMinContinueTime() > 0 && executionDuration < timer.getLockMinContinueTime()) {
                    long additionalWaitTime = timer.getLockMinContinueTime() - executionDuration;
                    Thread.sleep(additionalWaitTime);
                }

                TimerInit.updateTimerExecuteInfo(timerCode, startTime);
            } else {
                log.info("Timer {} could not acquire lock, skipping execution", timerCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Timer " + timerCode + " interrupted while waiting for lock");
        } finally {
            if (acquired) {
                lock.unlock();
            }
        }
    }

    /**
     * 执行定时器方法
     */
    private void executeTimerMethod(TimerDTO timer) throws Exception {
        Method method = timer.getMethod();
        Class<?> clazz = method.getDeclaringClass();

        ApplicationContext applicationContext = TimerInit.getApplicationContext();
        if (applicationContext == null) {
            throw new IllegalStateException("ApplicationContext not available");
        }

        Object bean = applicationContext.getBean(clazz);
        method.setAccessible(true);

        boolean isHasRecordInstructions = false;
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] == TimerRecordInstruction.class) {
                isHasRecordInstructions = true;
                break;
            }
        }

        String uuid = java.util.UUID.randomUUID().toString().replaceAll("-", "") + System.nanoTime();
        String code = timer.getCode();

        if (!isHasRecordInstructions) {
            // 执行不带 TimerRecordInstruction 参数的方法
            prepareInvokeParametersByNoHasRecordInstructions(bean, method, uuid, code);
            return;
        }

        prepareInvokeParametersByHasRecordInstructions(bean, method, uuid, code);

    }

    /**
     * 获取定时器状态
     */
    public boolean isTimerRunning(String timerCode) {
        return scheduledTasks.containsKey(timerCode) && !scheduledTasks.get(timerCode).isCancelled();
    }

    /**
     * 重启定时器
     */
    public void restartTimer(String timerCode) {
        stopTimer(timerCode);
        startTimer(timerCode);
    }

    /**
     * 销毁方法，关闭线程池
     */
    public void destroy() {
        stopAllTimers();
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 执行反射的方法（不带 TimerRecordInstruction 参数）
     */
    private void prepareInvokeParametersByNoHasRecordInstructions(Object bean, Method method, String uuid, String code) {
        // 开始时间
        Date startTime = new Date();
        boolean isExceptionEnd = false;
        Exception exception = null;

        // 创建 CountDownLatch 用于同步
        CountDownLatch startLatch = new CountDownLatch(1);
        Thread.ofVirtual()
                .name("timer-on-start-" + code + "-" + uuid)
                .start(() -> {
                    try {
                        timerSlot.onStart(new TimerOnStartDTO()
                                .setCode(code)
                                .setUuid(uuid)
                                .setGmtOnStart(startTime)
                        );
                    } finally {
                        // 确保无论成功与否都释放锁
                        startLatch.countDown();
                    }
                });

        Logger logger = new Logger() {
            @Override
            public void callback(LoggerItem loggerItem) {
                try {
                    // 等待 event-on-start 完成
                    startLatch.await();
                } catch (InterruptedException e) {
                    // 锁被中断
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
                Thread.ofVirtual()
                        .name("timer-on-progress-" + code + "-" + uuid + "-" + System.nanoTime())
                        .start(() -> {
                            timerSlot.onInProgress(new TimerOnInProgressDTO()
                                    .setGmtOn(new Date())
                                    .setCode(code)
                                    .setUuid(uuid)
                                    .setLoggerItem(loggerItem)
                            );
                        });
            }
        };

        try {
            // 判断第一个参数是否为 Logger 类型
            if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == Logger.class) {
                method.invoke(bean, logger);
            } else {
                method.invoke(bean);
            }
        } catch (Exception e) {
            isExceptionEnd = true;
            exception = e;
            throw new RuntimeException(e);
        } finally {
            Boolean finalIsExceptionEnd = isExceptionEnd;
            Exception finalException = exception;
            Thread.ofVirtual()
                    .name("timer-on-end-" + code + "-" + uuid + "-" + System.nanoTime())
                    .start(() -> {
                        timerSlot.onEnd(new TimerOnEndDTO()
                                .setIsExceptionEnd(finalIsExceptionEnd)
                                .setException(finalException)
                                .setCode(code)
                                .setUuid(uuid)
                                .setGmtOnStart(startTime)
                                .setGmtOnEnd(new Date())
                        );

                    });
        }
    }

    /**
     * 执行反射的方法（带 TimerRecordInstruction 参数）
     */
    private void prepareInvokeParametersByHasRecordInstructions(Object bean, Method method, String uuid, String code) {
        // 开始时间
        Date startTime = new Date();
        var ref = new Object() {
            boolean isExceptionEnd = false;
            Exception exception = null;
        };

        AtomicBoolean isStart = new AtomicBoolean(false);
        AtomicBoolean isEnd = new AtomicBoolean(false);
        // 创建 CountDownLatch 用于同步
        CountDownLatch startLatch = new CountDownLatch(1);


        TimerRecordInstruction timerRecordInstructions = new TimerRecordInstruction() {

            @Override
            public void start() {
                Thread.ofVirtual()
                        .name("timer-on-start-" + code + "-" + uuid)
                        .start(() -> {
                            try {
                                timerSlot.onStart(new TimerOnStartDTO()
                                        .setCode(code)
                                        .setUuid(uuid)
                                        .setGmtOnStart(startTime)
                                );
                                isStart.set(true);
                            } finally {
                                startLatch.countDown();
                            }
                        });
            }

            @Override
            public void end() {
                Thread.ofVirtual()
                        .name("timer-on-end-" + code + "-" + uuid + "-" + System.nanoTime())
                        .start(() -> {
                            timerSlot.onEnd(new TimerOnEndDTO()
                                    .setIsExceptionEnd(ref.isExceptionEnd)
                                    .setException(ref.exception)
                                    .setCode(code)
                                    .setUuid(uuid)
                                    .setGmtOnStart(startTime)
                                    .setGmtOnEnd(new Date())
                            );
                            isEnd.set(true);
                        });

            }

        };

        Logger logger = new Logger() {
            @Override
            public void callback(LoggerItem loggerItem) {
                try {
                    // 等待 event-on-start 完成
                    startLatch.await();
                } catch (InterruptedException e) {
                    // 锁被中断
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
                Thread.ofVirtual()
                        .name("timer-on-progress-" + code + "-" + uuid + "-" + System.nanoTime())
                        .start(() -> {
                            timerSlot.onInProgress(new TimerOnInProgressDTO()
                                    .setGmtOn(new Date())
                                    .setCode(code)
                                    .setUuid(uuid)
                                    .setLoggerItem(loggerItem)
                            );
                        });
            }
        };

        try {
            // 如果有只有一个参数
            if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == TimerRecordInstruction.class) {
                method.invoke(bean, timerRecordInstructions);
            }
            // 如果有两个参数，且第一个参数是 Logger 类型，第二个参数是timerRecordInstructions 类型
            else if (method.getParameterCount() == 2 && method.getParameterTypes()[0] == Logger.class
                    && method.getParameterTypes()[1] == TimerRecordInstruction.class) {
                method.invoke(bean, logger, timerRecordInstructions);
            }
            // 如果有2个参数，且第一个参数是 TimerRecordInstruction 类型,且另一个参数是 Logger 类型
            else if (method.getParameterCount() == 2 && method.getParameterTypes()[0] == TimerRecordInstruction.class
                    && method.getParameterTypes()[1] == Logger.class) {
                method.invoke(bean, timerRecordInstructions, logger);
            } else {
                throw new IllegalArgumentException("Method parameters do not match expected types");
            }
        } catch (Exception e) {
            ref.isExceptionEnd = true;
            ref.exception = e;
            // 当报错时强行执行
            timerRecordInstructions.end();
            throw new RuntimeException(e);
        } finally {
            // 如果调用了开始，就调用结束
            if (isStart.get() && !isEnd.get()) {
                timerRecordInstructions.end();
            }
        }


    }

}
