# System Setting 系统配置管理模块

系统级别的配置管理模块，提供统一的配置存储、访问和管理功能。

## 功能特性

- **集中管理**：统一存储系统所有配置项
- **动态更新**：运行时修改配置，无需重启
- **分组管理**：支持配置项分组和分类
- **版本控制**：记录配置变更历史
- **权限控制**：基于角色的配置访问权限
- **多环境支持**：区分开发、测试、生产环境配置

## 快速开始

### 引入依赖
```xml
<dependency>
    <groupId>com.kantboot</groupId>
    <artifactId>system-setting</artifactId>
    <version>${revision}</version>
</dependency>
```

### 基本使用

1. **获取配置项**：
```java
@Autowired
private SystemSettingService settingService;

// 获取配置值
String value = settingService.getValue("system.notification.email");
```

2. **更新配置项**：
```java
// 更新配置值
settingService.updateValue("system.notification.email", "admin@example.com");
```

3. **监听配置变更**：
```java
@SettingListener("system.notification.email")
public void onEmailChange(String newValue) {
    // 处理配置变更
}
```

## 核心组件

### SystemSettingService
- **功能**：配置管理核心服务
- **主要方法**：
  - `getValue(String key)`: 获取配置值
  - `updateValue(String key, String value)`: 更新配置值
  - `getGroup(String group)`: 获取分组配置

### SettingRepository
- **功能**：配置数据访问接口
- **特性**：
  - 支持JPA和Redis二级缓存
  - 自动记录操作日志

### SettingListener
- **功能**：配置变更监听器
- **使用方式**：
  - 使用`@SettingListener`注解方法
  - 支持通配符匹配多个配置项

## 配置说明

### 数据库表结构
```sql
CREATE TABLE sys_setting (
    id VARCHAR(32) PRIMARY KEY,
    setting_key VARCHAR(100) NOT NULL,
    setting_value TEXT,
    setting_group VARCHAR(50),
    description VARCHAR(200),
    create_time DATETIME,
    update_time DATETIME,
    UNIQUE KEY (setting_key)
);
```

### 应用配置
```yaml
system:
  setting:
    cache-enabled: true  # 是否启用缓存
    cache-timeout: 300   # 缓存超时时间(秒)
    audit-enabled: true  # 是否记录操作审计
```

## 最佳实践

1. **配置项命名**：
   - 使用点分格式：`系统模块.功能.配置项`
   - 示例：`system.notification.email`

2. **敏感配置**：
   - 对密码等敏感配置进行加密存储
   - 使用`@SensitiveSetting`标记敏感字段

3. **配置分组**：
   - 按功能模块分组配置
   - 使用`setting_group`字段进行分类

4. **变更管理**：
   - 重要配置变更应走审批流程
   - 定期备份配置数据

## 依赖

- Spring Data JPA
- Spring Cache
- Lombok
- Jackson

## 版本
${revision}

## 注意事项

1. 生产环境建议启用配置审计功能
2. 高频访问的配置建议启用缓存
3. 集群环境下需确保配置同步
