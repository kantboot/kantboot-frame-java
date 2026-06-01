# Version 0.0.3 Code Review

本文档记录当前框架在生产兼容前提下需要处理的问题。目标是保持现有用法不变，只修复内部缺陷、资源泄漏、稳定性风险和明显的实现错误。

## 修复原则

- 不修改现有 public 方法签名
- 不修改现有 REST 路径
- 不修改现有注解名称和注解参数含义
- 不修改默认配置键
- 不修改现有返回对象字段
- 只修复内部实现缺陷和运行时稳定性问题

## 严重问题清单

### 1. gateway-client 在关闭流程中会直接退出宿主 JVM

**位置**

- `gateway/gateway-client/src/main/java/com/kantboot/gateway/client/service/impl/GatewayClientServiceImpl.java`

**问题**

- `closeClient()` 中调用了 `SpringApplication.exit(...)` 之后又调用 `System.exit(...)`
- 如果该模块运行在共享 JVM 或被其他项目集成，关闭动作会直接结束整个进程

**影响**

- 可能导致宿主应用被误杀
- 可能导致未完成的业务请求、事务和后台任务被强制中断

**修复方式**

- 保留 `closeClient()` 方法和现有调用方式不变
- 将退出动作限制在当前独立进程模式下执行
- 在嵌入式场景下改为仅发送关闭通知，不主动调用 `System.exit(...)`
- 如果必须保留退出能力，则增加内部运行模式判断，默认不终止宿主 JVM

### 2. engine-database 获取数据库元数据时没有关闭连接

**位置**

- `engine/engine-database/src/main/java/com/kantboot/engine/database/service/impl/EngineDatabaseServiceImpl.java`

**问题**

- `getInfo()` 中通过 `dataSource.getConnection()` 获取连接后，没有显式关闭
- 每次调用都可能占用一个连接直到连接池回收

**影响**

- 长时间运行后可能耗尽连接池
- 数据库访问会逐渐变慢，最终表现为接口不可用

**修复方式**

- 保持 `getInfo()` 返回结构不变
- 使用 `try-with-resources` 包裹数据库连接
- 在方法内部只读取元数据，不持有连接对象

### 3. engine-computer 的线程缓存实际上不生效

**位置**

- `engine/engine-computer/src/main/java/com/kantboot/engine/computer/service/service/EngineComputerProcessServiceImpl.java`

**问题**

- `getThreadsByPid()` 内部每次都会新建一个 `Caffeine` 实例
- 缓存对象是局部变量，方法结束后即失效

**影响**

- 线程详情查询会重复扫描进程线程表
- 高频调用时会增加系统开销

**修复方式**

- 保持 `getThreadsByPid(int pid)` 方法签名不变
- 将缓存提升为类级别的复用实例
- 保持现有缓存时长和返回结构不变

### 4. util-event 的事件派发对异常传播过宽

**位置**

- `util/util-event/src/main/java/com/kantboot/util/event/emit/EventEmit.java`

**问题**

- 同一个事件码下的某个监听方法抛出异常后，会直接终止当前派发流程
- 后续同事件码的监听方法不会继续执行

**影响**

- 一个监听器失败会拖住其他监听器
- 事件系统的可用性和隔离性不足

**修复方式**

- 保持 `to(String code, Object... values)` 方法签名不变
- 将单个监听器执行异常隔离到监听器级别
- 保留异常记录能力
- 不改变事件编码和监听方法的注册方式

### 5. util-event 的 bean 获取方式对代理场景不稳

**位置**

- `util/util-event/src/main/java/com/kantboot/util/event/emit/EventEmit.java`

**问题**

- 当前通过 `applicationContext.getBean(method.getDeclaringClass())` 获取 bean
- 在代理类、接口代理或特殊 Spring 配置下，按具体类取 bean 可能失败

**影响**

- 某些环境中事件监听器无法被正常调用
- 问题会表现为“代码存在但运行时找不到 bean”

**修复方式**

- 保持事件监听定义方式不变
- 优先使用 Spring 上下文中实际可用的 bean 获取方式
- 兼容代理类和原始类两种情况

### 6. util-event 的事件注册表是静态全局状态，重复初始化可能重复累加

**位置**

- `util/util-event/src/main/java/com/kantboot/util/event/init/EmitInit.java`

**问题**

- `EVENT_LIST` 和 `EVENT_MAP` 是静态集合
- 初始化过程没有在开始前清空旧数据
- 多次初始化或重复扫描时，事件数据可能重复累加

**影响**

- 事件元数据可能重复
- 事件执行可能出现重复注册风险

**修复方式**

- 保持现有注解和扫描入口不变
- 在初始化开始时清空旧的事件注册数据
- 对同一方法和同一事件码保持幂等注册

### 7. util-event 的事件元信息记录使用了错误的类名来源

**位置**

- `util/util-event/src/main/java/com/kantboot/util/event/emit/EventEmit.java`
- `util/util-event/src/main/java/com/kantboot/util/event/init/EmitInit.java`

**问题**

- `methodWithParams` 使用了 `method.getClass().getName()`，而不是声明该方法的业务类名
- 记录到事件日志中的方法来源不正确

**影响**

- 排查事件时看到的元信息不准确
- 审计和日志分析会被误导

**修复方式**

- 保持事件日志结构不变
- 将方法来源改为声明类名
- 不修改现有事件码和参数定义

## 中等风险问题

### 8. engine-computer 的 GPU、CPU 信息存在采样和缓存混用

**位置**

- `engine/engine-computer/src/main/java/com/kantboot/engine/computer/service/service/EngineComputerService.java`

**问题**

- `getCpu()`、`getByPid()` 采用采样计算
- `getGpus()`、`getInfo()` 使用缓存
- 不同接口的刷新语义不一致

**影响**

- 前端展示时容易把缓存值、采样均值和实时值混为一谈

**修复方式**

- 保持接口返回结构不变
- 在内部统一标注数据语义
- 对缓存时长和采样耗时进行明确区分

### 9. util-event 的扫描范围和扫描结果缺少去重边界

**位置**

- `util/util-event/src/main/java/com/kantboot/util/event/init/EmitInit.java`

**问题**

- 扫描包列表来源于 `ScanInit`
- 扫描结果写入全局静态集合时，缺少完整去重边界

**影响**

- 在复杂启动流程中，可能出现重复扫描和重复登记

**修复方式**

- 保持扫描注解不变
- 对扫描结果按方法签名做幂等登记
- 对事件列表输出做稳定去重

## util-event 补充审查

### 10. README 示例与真实 API 不一致

**位置**

- `util/util-event/README.md`
- `util/util-event/src/main/java/com/kantboot/util/event/annotation/EventOn.java`

**问题**

- 文档示例使用 `@EventOn("user.created")`
- 真实注解定义要求使用 `@EventOn(code = "...")`

**影响**

- 接入方可能按文档写出无法编译或无法生效的代码
- 文档会误导新项目集成

**修复方式**

- 保持注解定义不变
- 统一更新文档示例为真实 API
- 不修改对外使用方式，只修正文档表述

### 11. EventOnEndDTO 字段契约不完整

**位置**

- `util/util-event/src/main/java/com/kantboot/util/event/domain/dto/EventOnEndDTO.java`
- `util/util-event/src/main/java/com/kantboot/util/event/emit/EventEmit.java`

**问题**

- DTO 中定义了 `duration`、`success`、`exceptionMessage`
- 事件结束派发时未完整填充这些字段

**影响**

- 结束事件看起来像有完整审计数据，实际并不完整
- 统计、排障、审计可能误判

**修复方式**

- 保持 DTO 字段不变
- 在结束事件派发时补齐字段值
- 不改事件数据结构，只补全已有契约

### 12. EventParam 的注解目标和实际扫描方式不一致

**位置**

- `util/util-event/src/main/java/com/kantboot/util/event/annotation/EventParam.java`
- `util/util-event/src/main/java/com/kantboot/util/event/init/EmitInit.java`

**问题**

- 注解允许标注在 `METHOD` 和 `PARAMETER`
- 实际扫描逻辑只读取参数级注解

**影响**

- 方法级标注写了也不会生效
- 接入方容易误解这个注解的支持范围

**修复方式**

- 保持注解名称和使用方式不变
- 只保留实际生效的用法说明
- 或在实现里补齐方法级标注的处理，但不改变现有参数级行为

### 13. util-event 的静态注册状态仍然对重复初始化敏感

**位置**

- `util/util-event/src/main/java/com/kantboot/util/event/init/EmitInit.java`

**问题**

- 事件注册表依赖静态集合
- 在重复上下文、热加载或特殊装配场景里，状态仍然对初始化过程敏感

**影响**

- 事件元数据可能在复杂生命周期里出现偏差
- 日志和注册结果不够稳

**修复方式**

- 保持外部用法不变
- 继续强化注册幂等性
- 让初始化过程可重复执行且结果一致

### 14. 事件派发异常语义仍然偏重

**位置**

- `util/util-event/src/main/java/com/kantboot/util/event/emit/EventEmit.java`

**问题**

- 单个监听器异常会被记录并在最后统一抛出
- 调用方会收到异常，即使其他监听器已经成功执行

**影响**

- 调用方需要明确接受“部分成功但整体抛错”的语义
- 事件投递结果对业务方的感知不够直接

**修复方式**

- 保持 `to(String code, Object... values)` 的调用方式不变
- 明确并固定异常语义
- 若保留当前语义，则在文档中写清楚“部分监听失败仍会抛出异常”

## 需要保留的行为

- `util-event` 的 `@EventOn`、`@EventScan` 用法不变
- `engine-computer` 的现有查询接口不变
- `engine-database` 的接口返回对象不变
- `gateway-client` 的现有请求路径不变

## 结论

当前框架的主要风险集中在三类：

1. 进程级退出风险
2. 资源泄漏风险
3. 事件派发和注册的不稳定风险

这些问题都可以在不改变外部用法的前提下修复。
