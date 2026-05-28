# util-event（事件工具模块）

事件工具模块，提供基于注解的事件监听与触发机制，支持跨模块事件通信。

## 功能特性

- **注解驱动**：使用`@EventOn`和`@EventScan`注解简化事件配置
- **灵活监听**：支持多方法监听同一事件
- **参数传递**：支持向监听方法传递任意参数
- **自动扫描**：自动扫描并注册事件监听方法
- **调试支持**：提供详细的事件注册日志

## 快速开始

### 引入Maven依赖
```xml
<dependency>
    <groupId>com.kantboot</groupId>
    <artifactId>util-event</artifactId>
    <version>${revision}</version>
</dependency>
```

### 基本使用示例

1. **定义事件监听方法**：
```java
@Service
public class MyEventListener {

    @EventOn("user.created")
    public void handleUserCreated(User user) {
        System.out.println("新用户创建: " + user.getName());
    }
}
```

2. **触发事件**：
```java
@RestController
public class UserController {

    @Autowired
    private EventEmit eventEmit;

    @PostMapping("/users")
    public void createUser(@RequestBody User user) {
        // 业务逻辑...
        eventEmit.to("user.created", user);
    }
}
```

3. **自定义扫描包路径**（可选）：
```java
@EventScan({"com.myapp.events", "com.other.events"})
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

## API说明

### 核心注解

#### @EventOn
- **用途**：标记方法为事件监听方法
- **参数**：
  - `value`: 事件编码（必填）

#### @EventScan
- **用途**：指定要扫描的包路径
- **参数**：
  - `value`: 包路径数组（可选，默认扫描`com.kantboot`）

### 核心类

#### EventEmit
- **方法**：
  - `to(String code, Object... value)`: 触发事件
    - `code`: 事件编码
    - `value`: 传递给监听方法的参数

#### EmitInit
- **功能**：初始化事件系统，自动扫描并注册监听方法
- **调试方法**：
  - `getRegisteredEventCodes()`: 获取所有注册的事件码
  - `getScannedClasses()`: 获取所有扫描的类

## 配置说明

1. **默认配置**：
   - 自动扫描`com.kantboot`包下的监听方法
   - 支持Spring Bean中的监听方法

2. **自定义配置**：
   - 在主应用类上添加`@EventScan`注解指定扫描包路径
   - 支持多个包路径

## 最佳实践

1. **事件命名规范**：
   - 使用`模块.动作`格式，如`order.created`
   - 保持事件名称清晰且唯一

2. **监听方法设计**：
   - 保持监听方法简洁
   - 避免在监听方法中执行耗时操作
   - 考虑使用异步处理

3. **异常处理**：
   - 监听方法应自行处理业务异常
   - 系统异常会被记录并抛出RuntimeException

## 依赖

- Spring Framework (AOP, Context)
- Reflections (类扫描)
- Lombok
- SLF4J (日志)
- Jakarta Annotations

## 版本

${revision}
