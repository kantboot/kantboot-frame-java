# util-i18n（国际化工具模块）

国际化(i18n)工具模块，提供多语言资源管理和消息格式化功能，支持动态语言切换和参数化消息。

## 功能特性

- **多语言资源管理**：支持properties、YAML等格式的资源文件
- **消息格式化**：支持参数化消息和复数形式处理
- **动态语言切换**：运行时切换应用语言环境
- **默认语言回退**：当请求语言不可用时回退到默认语言
- **资源热加载**：开发模式下支持资源文件热更新

## 快速开始

### 引入Maven依赖
```xml
<dependency>
    <groupId>com.kantboot</groupId>
    <artifactId>util-i18n</artifactId>
    <version>${revision}</version>
</dependency>
```

### 基本使用示例

1. **配置语言资源文件**：
```
# messages.properties (默认)
welcome.message=Welcome, {0}!
button.submit=Submit

# messages_zh_CN.properties
welcome.message=欢迎, {0}!
button.submit=提交
```

2. **获取国际化消息**：
```java
// 获取当前语言环境的消息
String welcomeMsg = I18nUtil.getMessage("welcome.message", "John");

// 输出结果会根据当前语言环境变化
// 英文环境: "Welcome, John!"
// 中文环境: "欢迎, John!"
```

3. **切换语言环境**：
```java
// 设置当前线程的语言环境为中文(中国)
I18nUtil.setLocale(Locale.SIMPLIFIED_CHINESE);

// 获取当前语言环境
Locale currentLocale = I18nUtil.getCurrentLocale();
```

4. **参数化消息**：
```java
// 资源文件内容: order.status=Your order {0} is {1}
String message = I18nUtil.getMessage(
    "order.status", 
    "#12345", 
    "shipped"
);
// 英文输出: "Your order #12345 is shipped"
```

## API说明

### 核心类

#### I18nUtil
- **功能**：国际化工具类
- **主要方法**：
  - `getMessage(String code, Object... args)`: 获取当前语言环境的参数化消息
  - `getMessage(Locale locale, String code, Object... args)`: 获取指定语言环境的参数化消息
  - `setLocale(Locale locale)`: 设置当前线程的语言环境
  - `getCurrentLocale()`: 获取当前线程的语言环境
  - `getSupportedLocales()`: 获取支持的语言环境列表

#### MessageSource
- **功能**：消息资源接口
- **实现类**：
  - `ResourceBundleMessageSource`: 基于properties文件的实现
  - `YamlMessageSource`: 基于YAML文件的实现

## 配置说明

1. **资源文件位置**：
```properties
# 默认加载classpath下的messages.properties
i18n.basename=messages

# 自定义资源文件位置(多个用逗号分隔)
i18n.basename=messages,labels,errors
```

2. **默认语言**：
```properties
# 设置默认语言环境(当请求语言不可用时使用)
i18n.default-locale=en_US
```

3. **缓存配置**：
```properties
# 禁用资源缓存(开发环境建议)
i18n.cache-seconds=0

# 生产环境建议启用缓存
i18n.cache-seconds=3600
```

## 最佳实践

1. **资源文件组织**：
   - 按功能模块拆分资源文件(messages_*.properties, errors_*.properties等)
   - 保持键名一致性和描述性
   - 避免在代码中硬编码消息内容

2. **语言环境处理**：
   - 在Web应用中通过Accept-Language头或用户偏好设置语言
   - 使用ThreadLocal保存当前语言环境
   - 提供语言切换接口

3. **消息格式化**：
   - 使用参数化消息而非字符串拼接
   - 为复数形式提供不同语言的处理
   - 考虑日期、数字的本地化格式

## 依赖

- Spring Context (资源管理和消息源)
- SnakeYAML (YAML资源文件支持)
- SLF4J (日志记录)

## 版本

${revision}
