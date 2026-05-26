# KANTBOOT 框架

基于 Spring Boot 3 的多模块应用开发框架，提供认证、缓存、文件、国际化、用户体系、系统监控等开箱即用的能力，帮助团队快速搭建标准化的后端服务。

---

## 目录

- [技术栈](#技术栈)
- [模块结构](#模块结构)
- [快速开始](#快速开始)
- [版本管理](#版本管理)
- [依赖管理](#依赖管理)
- [开发规范](#开发规范)

---

## 技术栈

| 类别 | 技术 | 版本 |
|------|------|------|
| 基础框架 | Spring Boot | 3.4.3 |
| 语言 | Java | 21 |
| 构建工具 | Maven | 3.x |
| ORM | Spring Data JPA | — |
| 数据库 | MySQL / PostgreSQL | 8.0.33 / 42.7.2 |
| 连接池 | Druid | 1.2.6 |
| 缓存 | Caffeine / EhCache | 3.1.8 / — |
| RPC | Apache Dubbo | 3.3.3 |
| 工具库 | Hutool / Lombok / Fastjson2 | 5.8.26 / 1.18.34 / 2.0.37 |
| HTTP 客户端 | OkHttp3 | 4.12.0 |
| 二维码 | ZXing | 3.5.1 |
| 系统监控 | OSHI / JNA | 6.4.0 / 5.13.0 |
| 分布式锁 | ShedLock | 4.42.0 |

---

## 模块结构

```
kantboot-frame-java
├── dependencies/          # 全局 BOM，统一管理三方依赖版本
├── global/                # 框架级全局能力（自动配置、注解、基础组件）
├── util/                  # 通用工具模块
│   ├── util-auth/         #   认证与权限
│   ├── util-cache/        #   缓存封装
│   ├── util-file/         #   文件处理
│   ├── util-i18n/         #   国际化
│   ├── util-timer/        #   定时任务
│   └── util-all/          #   汇总包
├── system/                # 平台系统配置
│   ├── system-setting/    #   全局参数配置
│   ├── system-dict/       #   数据字典
│   └── system-all/
├── user/                  # 用户域
│   ├── user-account/      #   账号管理
│   ├── user-balance/      #   余额/积分
│   └── user-all/
├── functional/            # 通用业务功能
│   ├── functional-icon/   #   图标管理
│   ├── functional-email/  #   邮件发送
│   └── functional-all/
├── engine/                # 运行时监控引擎
│   ├── engine-jvm/        #   JVM 内存/线程监控
│   ├── engine-computer/   #   CPU / GPU / 磁盘监控
│   ├── engine-database/   #   数据库连接池监控
│   └── engine-all/
├── fp/                    # 场景化产品模块
│   ├── fp-carousel/       #   轮播图
│   ├── fp-comment/        #   评论
│   ├── fp-community/      #   社区/帖子
│   └── fp-all/
├── tool/                  # 工具服务
│   ├── tool-area/         #   行政区划
│   ├── tool-ip/           #   IP 归属地
│   └── tool-all/
├── thirdparty/            # 三方平台集成
│   ├── thirdparty-wechat/ #   微信
│   ├── thirdparty-github/ #   GitHub
│   └── thirdparty-all/
├── official-plugin/       # 官方插件
│   └── official-plugin-all/
├── gateway/               # 网关
│   ├── gateway-server/
│   └── gateway-client/
├── developer/             # 开发工具（事件日志、定时任务管理）
├── starter-parent/        # Starter 聚合，对外发布的标准引导包
└── in-project/            # 项目内测试与示例工程
```

---

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- MySQL 8.0+ 或 PostgreSQL 14+

### 引入框架

在你的业务项目 `pom.xml` 中继承 `starter-parent`：

```xml
<parent>
    <groupId>com.kantboot</groupId>
    <artifactId>starter-parent</artifactId>
    <version>0.0.1</version>
</parent>
```

或者按需引入单个模块（以 `util-cache` 为例）：

```xml
<dependency>
    <groupId>com.kantboot</groupId>
    <artifactId>util-cache</artifactId>
    <version>0.0.1</version>
</dependency>
```

### 启动类

在 Spring Boot 启动类上添加框架激活注解：

```java
@KantbootApplication
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

---

## 版本管理

框架内部统一使用 `${revision}` 占位符管理版本号，`${kantboot.version}` 与 `${revision}` 同源，值始终保持一致。

```xml
<!-- 根 pom.xml -->
<properties>
    <kantboot.version>0.0.1</kantboot.version>
    <revision>${kantboot.version}</revision>
</properties>
```

**规则：**
- 框架内所有子模块版本号必须使用 `${revision}`，禁止写死具体版本
- 发布新版本时，只需修改根 `pom.xml` 中的 `kantboot.version` 属性

---

## 依赖管理

所有三方依赖版本集中在 `dependencies` 模块（BOM）中维护，业务模块引用时无需声明版本号。

```xml
<!-- 在父 pom 中已通过 dependencyManagement 导入，子模块直接使用 -->
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <!-- 无需填写 version -->
</dependency>
```

新增三方依赖时，必须先在 `dependencies/pom.xml` 的 `<dependencyManagement>` 中注册版本，再在业务模块中引用。

---

## 开发规范

详细的编码规范、模块命名约定、配置策略与审查流程请参阅 [DEVELOPMENT_GUIDE.md](DEVELOPMENT_GUIDE.md)。

**核心约定速览：**

- 包名统一使用 `com.kantboot.<业务域>` 前缀
- 子模块命名格式：`<聚合前缀>-<能力>`，例如 `util-cache`、`engine-jvm`
- 每个聚合目录必须提供 `*-all` 汇总模块
- 敏感配置（数据库密码、密钥等）通过环境变量注入，禁止提交到版本库
- 新增模块前须通过命名评审，更新模块矩阵表格后方可提交代码
