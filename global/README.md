# global 全局处理模块

KantBoot框架的全局处理模块，提供框架运行所需的基础功能和全局配置。

> 此框架以外的项目，勿直接引入

## 模块定位

- **框架基石**：运行时的基础模块
- **全局功能**：提供框架级别的通用功能
- **核心配置**：包含框架运行所需的基础配置

## 核心功能

### 1. 基础配置
- 自动配置框架核心组件
- 加载`application-global.yml`全局配置
- 提供全局属性配置工具类

### 2. 全局异常处理
- 统一异常处理机制
- 全局异常拦截器
- 标准化错误响应格式

### 3. 通用工具
- 基础工具类集合
- 类型转换工具
- 对象拷贝工具
- 表达式解析工具

### 4. 核心注解
- `@KantbootApplication` 主启动注解
- `@GlobalComponent` 全局组件标识
- `@EnableKantboot` 功能启用注解

## 快速开始

### 引入依赖
```xml
<dependency>
    <groupId>com.kantboot</groupId>
    <artifactId>global</artifactId>
    <version>${revision}</version>
</dependency>
```
> 此框架以外的项目，勿直接引入


## 版本
${revision}

## 注意事项

1. 该模块是框架运行的最低要求，不应移除
2. 修改全局配置可能影响整个框架行为
3. 升级版本时需注意兼容性
