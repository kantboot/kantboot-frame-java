# 框架内部依赖统一管理模块

## 概述
该模块用于统一管理`kantboot-frame-java`框架中除了`parent`和`dependencies`(此模块本身) 以外的Maven依赖管理。

非此框架内模块，勿引用。

## 引入依赖
```xml
<dependency>
    <groupId>com.kantboot</groupId>
    <artifactId>parent</artifactId>
    <version>${revision}</version>
</dependency>
```
> 外部项目，勿引入

## 各依赖说明
- **Lombok** (`org.projectlombok:lombok`)
  - 版本占位符: `${lombok.version}` (1.18.34)
  - Lombok 是一个用于简化Java代码的库，通过注解自动生成 getter、setter、构造器等代码，减少了样板代码的编写。它的作用域是 `provided`，意味着它只在编译时生效。

- **OkHttp3** (`com.squareup.okhttp3:okhttp`)
  - 版本占位符: `${okhttp3.version}` (4.12.0)
  - OkHttp 是一个高性能的HTTP客户端，用于发送和接收HTTP请求。它支持同步和异步操作，常用于处理网络请求。

- **SLF4J** (`org.slf4j:slf4j-api`)
  - 版本占位符: `${slf4j.version}` (2.0.16)
  - SLF4J 是一种日志抽象层，用于统一日志接口，可以与不同的日志实现（如 Logback、Log4j）配合使用。

- **javax.mail** (`javax.mail:javax.mail-api`)
  - 版本占位符: `${javax.mail.version}` (1.6.2)
  - 该依赖提供了 Java 邮件API，用于处理电子邮件的发送和接收功能。

- **com.sun.mail** (`com.sun.mail:javax.mail`)
  - 版本占位符: `${javax.mail.version}` (1.6.2)
  - 这是 `javax.mail` API 的实现，用于支持邮件的发送、接收等功能。

- **Hutool** (`cn.hutool:hutool-all`)
  - 版本占位符: `${hutool.version}` (5.8.24)
  - Hutool 是一个常用的 Java 工具库，提供了许多常用的工具类，如日期、文件、加密、反射、HTTP、JSON等。

- **Fastjson2** (`com.alibaba.fastjson2:fastjson2`)
  - 版本占位符: `${fastjson2.version}` (2.0.37)
  - Fastjson2 是阿里巴巴开源的 JSON 处理库，具有高效的序列化和反序列化能力。它还可以替换 Spring Boot 默认的 JSON 解析器。

- **Fastjson2 Extension** (`com.alibaba.fastjson2:fastjson2-extension`)
  - 版本占位符: `${fastjson2-extension.version}` (2.0.9.graal)
  - Fastjson2 的扩展模块，支持更多的序列化场景，并支持 GraalVM 原生镜像。

- **Reflections** (`org.reflections:reflections`)
  - 版本占位符: `${reflections.version}` (0.9.12)
  - Reflections 用于扫描 Java 类路径中的类，并分析注解、父类、接口等信息，通常用于构建框架时的注解扫描和类分析。

- **Yitter Id Generator** (`com.github.yitter:yitter-idgenerator`)
  - 版本占位符: `${yitter-idgenerator.version}` (1.0.6)
  - Yitter Id Generator 是一个用于生成全局唯一 ID 的工具，支持分布式环境下的 ID 生成。

- **MySQL Connector** (`mysql:mysql-connector-java`)
  - 版本占位符: `${mysql.version}` (8.0.33)
  - MySQL 数据库的 JDBC 连接器，用于 Java 应用程序与 MySQL 数据库之间的通信。

- **Druid** (`com.alibaba:druid`)
  - 版本占位符: `${druid.version}` (1.2.6)
  - Druid 是一个数据库连接池，具有高效的数据库连接管理和监控功能。

- **Spring Boot Starter** (`org.springframework.boot:spring-boot-starter`)
  - 版本占位符: `${spring-boot.version}`
  - Spring Boot 的基础启动器，用于启动 Spring Boot 应用，包含了所有基本的 Spring Boot 配置和依赖。

- **PostgreSQL** (`org.postgresql:postgresql`)
  - 版本占位符: `${postgresql.version}` (42.7.2)
  - PostgreSQL 数据库的 JDBC 驱动，供 Java 应用与 PostgreSQL 数据库通信使用。

- **Caffeine** (`com.github.ben-manes.caffeine:caffeine`)
  - 版本占位符: `${caffeine.version}` (2.8.5)
  - Caffeine 是一个高效的 Java 缓存库，提供内存缓存的实现，可以用于存储和快速访问数据。

- **ZXing** (`com.google.zxing:core`)
  - 版本占位符: `${zxing.version}` (3.5.1)
  - ZXing 是一个开源的二维码生成和解析库，支持生成和解析 QR 码和条形码。
