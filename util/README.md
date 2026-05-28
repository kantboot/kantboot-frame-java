# KantBoot Util 工具模块集合

KantBoot框架的工具模块集合，提供各种常用功能的封装和扩展。

## 模块概览

| 模块名称 | 功能描述 | 文档链接 |
|---------|---------|---------|
| util-auth | 认证授权工具，提供JWT、OAuth2等支持 | [README](./util-auth/README.md) |
| util-base-control | 基础控制器封装，提供通用CRUD接口 | [README](./util-base-control/README.md) |
| util-cache | 缓存工具，支持多级缓存和自动刷新 | [README](./util-cache/README.md) |
| util-crypto | 加密解密工具，支持AES、RSA等算法 | [README](./util-crypto/README.md) |
| util-data-change | 数据变更记录和审计 | [README](./util-data-change/README.md) |
| util-event | 事件发布订阅机制 | [README](./util-event/README.md) |
| util-file | 文件操作工具，支持本地和云存储 | [README](./util-file/README.md) |
| util-http | HTTP客户端工具，简化请求发送 | [README](./util-http/README.md) |
| util-i18n | 国际化支持，多语言资源管理 | [README](./util-i18n/README.md) |
| util-jpa | JPA扩展，增强Repository功能 | [README](./util-jpa/README.md) |
| util-qrcode | 二维码生成和解析 | [README](./util-qrcode/README.md) |
| util-rest | REST接口工具和响应封装 | [README](./util-rest/README.md) |
| util-setting | 配置管理，支持动态更新 | [README](./util-setting/README.md) |

## 快速开始

### 引入全部工具模块
```xml
<dependency>
    <groupId>com.kantboot</groupId>
    <artifactId>util-all</artifactId>
    <version>${revision}</version>
</dependency>
```

### 按需引入单个模块
```xml
<dependency>
    <groupId>com.kantboot</groupId>
    <artifactId>util-{模块名}</artifactId>
    <version>${revision}</version>
</dependency>
```

## 通用配置

1. **日志级别**：
```properties
# 设置工具模块的日志级别
logging.level.com.kantboot.util=INFO
```

2. **版本管理**：
```properties
# 统一版本号管理
<properties>
    <revision>1.0.0-SNAPSHOT</revision>
</properties>
```

3. **异常处理**：
- 所有工具模块统一使用`KantbootException`作为基础异常
- 异常消息支持国际化

## 最佳实践

1. **模块选择**：
   - 按需引入，避免不必要的依赖
   - 优先使用util-all进行快速开发
   - 生产环境建议按需引入具体模块

2. **配置管理**：
   - 公共配置放在application.yml中
   - 模块特定配置使用模块前缀(如`i18n.`, `cache.`等)

3. **异常处理**：
   - 捕获`KantbootException`处理工具模块异常
   - 使用`@ControllerAdvice`统一处理异常

## 依赖管理

### 核心依赖
- Spring Boot Starter
- Lombok
- SLF4J

### 可选依赖
- Redis (util-cache)
- ZXing (util-qrcode)
- SnakeYAML (util-i18n)
- JPA (util-jpa)

## 版本
${revision}

## 开发建议

1. **新工具模块开发**：
   - 在util目录下创建新的子模块
   - 遵循现有模块结构
   - 提供完整的README文档

2. **贡献指南**：
   - 保持API简洁一致
   - 提供单元测试
   - 文档使用中英双语

3. **问题反馈**：
   - 通过GitHub Issues报告问题
   - 提供重现步骤和环境信息
