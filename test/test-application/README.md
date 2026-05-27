# Test Application 测试启动模块

测试专用的Spring Boot启动模块，提供统一的测试环境和配置。

非此框架内模块，勿引用。

## 功能特性

- **统一测试入口**：提供标准的TestApplication启动类
- **测试配置预加载**：自动加载测试专用配置
- **Mock环境支持**：集成常用测试Mock组件
- **测试工具集成**：包含常用测试工具类

## 快速开始

### 引入Maven依赖
```xml
<dependency>
    <groupId>com.kantboot</groupId>
    <artifactId>test-application</artifactId>
    <version>${revision}</version>
    <scope>test</scope>
</dependency>
```

### 基本使用方式
```java
@SpringBootTest(classes = TestApplication.class)
public class MyServiceTest {
    
    @Autowired
    private MyService myService;

    @Test
    public void testServiceMethod() {
        // 测试代码
    }
}
```

## 核心组件

### TestApplication
- **功能**：测试专用的Spring Boot启动类
- **特性**：
  - 自动扫描测试组件
  - 预加载测试配置
  - 禁用不必要的生产环境组件

### 测试配置
默认加载`src/test/resources/application-test.yml`中的配置，优先级高于主配置。

## 配置说明

1. **基础配置**：
```yaml
# application-test.yml
test:
  environment: mock
  database:
    replace: true  # 使用内存数据库替代生产数据库
```

2. **Mock服务配置**：
```yaml
mock:
  services:
    external-api:
      enabled: true
      response-delay: 100ms
```

3. **日志级别**：
```properties
# 测试环境日志配置
logging.level.root=WARN
logging.level.com.kantboot=DEBUG
```

## 最佳实践

1. **测试类组织**：
   - 保持测试类与主代码相同的包结构
   - 使用`@TestComponent`标注测试专用组件
   - 避免在测试类中使用`@SpringBootApplication`

2. **测试隔离**：
   - 每个测试方法应独立运行
   - 使用`@DirtiesContext`重置应用上下文
   - 考虑使用`@TestPropertySource`覆盖特定配置

3. **性能优化**：
   - 重用应用上下文减少启动时间
   - 使用`@MockBean`替代真实外部服务
   - 考虑使用`@DataJpaTest`等切片测试

## 依赖

- Spring Boot Test
- JUnit 5
- Mockito
- Testcontainers (可选)

## 版本
${revision}

## 示例项目

参考`src/test/java/com/kantboot/test`下的示例测试类。
