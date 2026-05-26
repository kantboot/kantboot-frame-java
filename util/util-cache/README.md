# util-cache（缓存工具模块）

缓存工具模块，提供缓存操作和分布式锁功能。

## 功能特性

- 基本缓存操作
  - 键值对设置/获取
  - 批量删除
  - 前缀匹配删除
  - 过期时间设置

- 分布式锁
  - 支持带超时的锁
  - 支持无超时的锁
  - 解锁功能

- 键管理
  - 键存在检查
  - 键类型查询
  - 键前缀匹配查询

- 自动配置
  - 自定义缓存键生成器
  - 自动启用Spring缓存

## 快速开始

### 引入Maven依赖
```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>util-cache</artifactId>
    <version>${revision}</version>
</dependency>
```

### 基本配置

确保在项目中配置了Redis连接信息，然后在需要使用的类中注入CacheUtil：

```java
@Resource
private CacheUtil cacheUtil;
```

### 基本使用示例

```java
// 设置缓存
cacheUtil.set("user:1", "{\"name\":\"John\",\"age\":30}");

// 获取缓存
String userJson = cacheUtil.get("user:1");

// 设置带过期时间的缓存(30分钟)
cacheUtil.setEx("temp:data", "value", 30, TimeUnit.MINUTES);

// 使用分布式锁
if(cacheUtil.lock("order:lock:123", 10, TimeUnit.SECONDS)) {
    try {
        // 执行业务逻辑
    } finally {
        cacheUtil.unlock("order:lock:123");
    }
}
```

## API说明

### CacheUtil

#### 缓存操作
- `set(String key, String value)`: 设置键值
- `get(String key)`: 获取值
- `setEx(String key, String value, long timeout, TimeUnit unit)`: 设置带过期时间的键值
- `delete(String key)`: 删除键
- `deleteByPrefix(String prefix)`: 删除指定前缀的所有键
- `hasKey(String key)`: 检查键是否存在

#### 分布式锁
- `lock(String key, long timeout, TimeUnit unit)`: 获取带超时的锁
- `lock(String key)`: 获取无超时的锁
- `unlock(String key)`: 释放锁

#### 键管理
- `keysByPrefix(String prefix)`: 获取指定前缀的所有键
- `type(String key)`: 获取键的类型
- `expire(String key, long timeout, TimeUnit unit)`: 设置键的过期时间

### RedisConfig

- 自动配置Redis缓存
- 自定义键生成策略：类名+方法名+参数

## 配置说明

1. 确保application.properties/yml中配置了Redis连接信息：
```properties
spring.redis.host=127.0.0.1
spring.redis.port=6379
spring.redis.password=
```

2. 如需自定义键生成策略，可重写RedisConfig中的keyGenerator方法

## 注意事项

1. 分布式锁使用建议：
   - 总是设置合理的超时时间
   - 在finally块中释放锁
   - 避免锁的持有时间过长

2. 缓存使用建议：
   - 为缓存设置合理的过期时间
   - 使用明确的前缀命名键
   - 批量操作时注意性能影响

3. 性能考虑：
   - 避免频繁的大批量键查询
   - 对于热点数据可考虑本地缓存+Redis缓存的多级缓存策略

## 依赖

- Spring Data Redis
- Spring Cache
- Jedis/Lettuce

## 版本

${revision}
