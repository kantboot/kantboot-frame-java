# Util-Timer 定时器工具模块

## 概述

util-timer 是一个基于注解的定时任务管理模块，类似于 util-event 模块，提供了简单易用的定时器功能。

## 主要功能

- 基于注解的定时器定义 (`@TimerOn`)
- 支持锁机制，防止任务重复执行
- 可配置的执行间隔
- 运行时管理（启动、停止、重启）
- RESTful API 管理接口
- 自动扫描和注册定时器

## 快速开始

### 1. 添加依赖

在您的 `pom.xml` 中添加：

```xml
<dependency>
    <groupId>com.kantboot</groupId>
    <artifactId>util-timer</artifactId>
    <version>${revision}</version>
</dependency>
```

### 2. 创建定时器

使用 `@TimerOn` 注解标记方法：

```java
@Component
public class MyTimerService {

    @TimerOn(
        value = "my-timer-001",           // 定时器编码（必填）
        name = "我的定时器",                // 定时器名称（可选）
        description = "这是一个示例定时器",   // 定时器描述（可选）
        time = 5000L,                    // 执行间隔：5秒（可选，默认1000ms）
        lock = true,                     // 是否使用锁（可选，默认true）
        lockMaxWaitTime = 1000L,         // 锁最大等待时间（可选，默认0=不限制）
        lockMinContinueTime = 2000L      // 锁执行后最小持续时间（可选，默认0=不限制）
    )
    public void myScheduledTask() {
        System.out.println("定时器执行中: " + new Date());
        // 您的业务逻辑
    }
}
```

### 3. 启动应用

**🚀 自动启动：** 当Spring Boot应用启动完成后，所有定时器会自动启动并开始执行！

您会在控制台看到类似的启动日志：

```
=== Timer Initialization Started ===
Initializing timer listeners...
Scanning package: com.example
Found 3 classes in package com.example
Registered timer listener: my-timer-001 -> MyTimerService.myScheduledTask - 5000ms
Timer initialization completed successfully!
Found 1 timer(s) ready for execution
=====================================

=== Application Ready - Starting Timers ===
Starting 1 timer(s)...
Timer started: my-timer-001 with interval 5000ms
Timer startup summary: 1 successful, 0 failed
All timers started after application ready!
===========================================

Timer my-timer-001 executed successfully at 2025-07-30 15:30:00
Timer my-timer-001 executed successfully at 2025-07-30 15:30:05
```

### 3. 注解参数说明

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|-----|------|------|-------|------|
| value | String | 是 | - | 定时器唯一编码 |
| name | String | 否 | "" | 定时器名称 |
| description | String | 否 | "" | 定时器描述 |
| time | long | 否 | 1000L | 执行间隔（毫秒） |
| lock | boolean | 否 | true | 是否使用锁机制 |
| lockMaxWaitTime | long | 否 | 0L | 锁最大等待时间（毫秒，0=不限制） |
| lockMinContinueTime | long | 否 | 0L | 锁执行完毕后最小持续时间（毫秒） |

### 4. 管理定时器

#### 通过代码管理

```java
@Autowired
private ITimerService timerService;

// 启动定时器
timerService.startTimer("my-timer-001");

// 停止定时器
timerService.stopTimer("my-timer-001");

// 重启定时器
timerService.restartTimer("my-timer-001");

// 检查状态
boolean isRunning = timerService.isTimerRunning("my-timer-001");

// 获取所有定时器
List<TimerDTO> allTimers = timerService.getAll();
```

#### 通过RESTful API管理

| API | 方法 | 说明 |
|-----|------|------|
| `/timer/admin/getAll` | GET | 获取所有定时器 |
| `/timer/admin/getByCode?code=xxx` | GET | 根据编码获取定时器 |
| `/timer/admin/start?code=xxx` | POST | 启动定时器 |
| `/timer/admin/stop?code=xxx` | POST | 停止定时器 |
| `/timer/admin/restart?code=xxx` | POST | 重启定时器 |
| `/timer/admin/startAll` | POST | 启动所有定时器 |
| `/timer/admin/stopAll` | POST | 停止所有定时器 |
| `/timer/admin/status?code=xxx` | GET | 获取定时器状态 |

## 高级特性

### 锁机制

当 `lock = true` 时：
- 同一定时器的多次执行会使用锁来避免并发问题
- `lockMaxWaitTime` 控制获取锁的最大等待时间
- `lockMinContinueTime` 确保执行完成后锁保持一定时间

### 自动启动

**🎯 零配置启动：** 应用启动后，所有定时器会自动启动。系统使用`ApplicationReadyEvent`监听器确保在Spring上下文完全初始化后才启动定时器，保证执行的稳定性。

**启动流程：**
1. 应用启动 → 扫描@TimerOn注解 → 注册定时器
2. Spring上下文就绪 → 自动启动所有定时器
3. 定时器开始按配置间隔执行

您可以通过API控制单个定时器的启停，无需重启应用。

### 执行状态监控

每个定时器都会记录：
- 是否正在运行
- 下次执行时间
- 执行次数统计
- 最后执行时间

## 与 util-event 的对比

| 特性 | util-event | util-timer |
|-----|------------|------------|
| 触发方式 | 事件驱动 | 时间驱动 |
| 主要注解 | `@EventOn` | `@TimerOn` |
| 执行时机 | 手动触发 | 自动定时执行 |
| 并发控制 | 支持多监听器 | 支持锁机制 |
| 管理接口 | 查询接口 | 完整CRUD接口 |

## 注意事项

1. 定时器方法必须在Spring管理的Bean中
2. 定时器编码必须唯一
3. 建议合理设置执行间隔，避免系统资源浪费
4. 使用锁机制时要考虑业务逻辑的执行时间
5. 长时间运行的任务建议使用异步处理

## 示例项目

查看 `test-application` 模块中的示例代码，了解完整的使用方法。
