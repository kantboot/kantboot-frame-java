# util-setting（配置工具模块）

配置工具模块，提供基于注解的配置管理功能，支持配置项和配置组的定义与管理。

## 功能特性

- **注解驱动**：使用`@Setting`和`@SettingGroup`注解简化配置管理
- **结构化配置**：支持配置项分组管理
- **多语言支持**：内置多语言配置支持
- **默认值设置**：支持为配置项设置默认值
- **类型安全**：编译时检查配置项定义

## 快速开始

### 引入Maven依赖
```xml
<dependency>
    <groupId>com.kantboot</groupId>
    <artifactId>util-setting</artifactId>
    <version>${revision}</version>
</dependency>
```

### 基本使用示例

1. **定义配置组**：
```java
@SettingGroup(
    code = "appConfig",
    name = "应用配置",
    description = "应用相关的基础配置"
)
public class AppConfig {
    // 配置项定义...
}
```

2. **定义配置项**：
```java
@Setting(
    code = "timeout",
    name = "请求超时时间",
    description = "API请求超时时间(毫秒)",
    defaultValue = "5000"
)
private Integer timeout;
```

3. **使用配置项**：
```java
// 通过配置服务获取配置值
@Resource
private SettingService settingService;

public void someMethod() {
    Integer timeout = settingService.get("app.config.timeout", Integer.class);
    // 使用配置值...
}
```

## API说明

### 核心注解

#### @SettingGroup
- **用途**：标记配置组类
- **参数**：
  - `code`: 配置组编码(必填)
  - `name`: 配置组名称
  - `description`: 配置组描述
  - `sourceLanguageCode`: 源语言编码(默认zh_CN)

#### @Setting
- **用途**：标记配置项字段
- **参数**：
  - `code`: 配置项编码(必填)
  - `name`: 配置项名称
  - `description`: 配置项描述
  - `defaultValue`: 默认值
  - `sourceLanguageCode`: 源语言编码(默认zh_CN)

### 核心服务

#### SettingService
- **方法**：
  - `get(String code, Class<T> type)`: 获取配置值
  - `set(String code, Object value)`: 设置配置值
  - `getGroup(String groupCode)`: 获取配置组所有配置项

## 配置说明

1. **配置存储**：
   - 默认使用数据库存储配置
   - 支持自定义存储实现

2. **缓存策略**：
   - 配置值默认缓存
   - 支持自定义缓存策略

## 最佳实践

1. **命名规范**：
   - 使用`模块.功能`格式命名配置组，如`appConfig`
   - 使用`配置组.配置项`格式命名配置项，如`appConfig.timeout`

2. **配置设计**：
   - 相关配置项应放在同一配置组中
   - 为配置项添加清晰的描述信息
   - 为关键配置项设置合理的默认值

3. **性能考虑**：
   - 频繁访问的配置应考虑缓存
   - 批量获取配置时使用`getGroup`方法

## 依赖

- Spring Framework (Core, Context)
- Lombok
- 数据库驱动(根据实际存储选择)

## 版本

${revision}
