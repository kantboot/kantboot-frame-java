# Version 0.0.2 修复记录

本文档记录本次围绕 `util-event` 完成的兼容性修复。修复原则是不改变现有使用方式，不修改现有注解、方法签名、接口路径和返回字段，仅修复内部稳定性问题。

## 修复范围

- `util/util-event/src/main/java/com/kantboot/util/event/emit/EventEmit.java`
- `util/util-event/src/main/java/com/kantboot/util/event/init/EmitInit.java`

## 修复内容

### 1. 事件注册重复累加问题

**问题**

- `EmitInit` 使用静态集合保存事件注册信息
- 初始化时没有清空旧数据
- 重复初始化或重复扫描时，事件可能重复注册

**修复方式**

- 初始化开始时清空事件列表、事件映射、扫描类列表和已注册方法标识
- 增加方法级注册标识，避免同一个监听方法被重复加入事件映射
- 保持 `@EventOn` 的使用方式不变

### 2. 事件方法元信息记录错误

**问题**

- 原先 `methodWithParams` 使用 `method.getClass().getName()`
- 记录结果指向反射类本身，而不是业务监听方法所在类

**修复方式**

- 改为使用 `method.getDeclaringClass().getName()`
- 保持 `methodWithParams` 字段不变，只修正字段内容

### 3. Spring 代理场景下事件监听 Bean 获取不稳定

**问题**

- 原先直接通过 `applicationContext.getBean(method.getDeclaringClass())` 获取监听 Bean
- 在代理类、特殊 Bean 注册方式或 AOP 场景下可能无法获取到监听 Bean

**修复方式**

- 保留原有按声明类获取 Bean 的逻辑
- 获取失败时，根据 Spring Bean 的目标类继续查找兼容的 Bean
- 保持事件监听方法定义方式不变

### 4. 单个监听器异常会中断后续监听器

**问题**

- 原先某个监听方法抛异常后，会立即终止当前事件派发流程
- 同一个事件码下后续监听方法无法继续执行

**修复方式**

- 单个监听器异常时记录异常并继续执行后续监听器
- 所有监听器执行完成后，如果存在异常，再统一向调用方抛出
- 保持 `EventEmit.to(String code, Object... values)` 调用方式不变

### 5. 事件开始数据遇到 null 参数会异常

**问题**

- 原先使用 `List.of(values)` 记录事件开始数据
- 当事件参数中包含 `null` 时，`List.of` 会抛出异常

**修复方式**

- 改为复制参数到普通 `ArrayList`
- 允许事件参数中包含 `null`
- 不改变事件触发方式

### 6. 事件结束信息字段未完整填充

**问题**

- `EventOnEndDTO` 中已有 `duration`、`success`、`exceptionMessage` 字段
- 原先结束事件派发时没有填充这些字段

**修复方式**

- 补充事件执行耗时
- 补充事件是否成功
- 补充异常信息文本
- 保持 DTO 字段结构不变

### 7. 参数类型不匹配时的 null 防护

**问题**

- 参数类型不匹配日志中直接调用参数的 `getClass()`
- 当参数为 `null` 时，会触发二次异常

**修复方式**

- 参数为 `null` 时输出 `"null"`
- 类型转换方法增加 `null` 防护
- 保持参数传递规则不变

## 未改变的行为

- `@EventOn` 注解用法不变
- `@EventParam` 注解用法不变
- `EventEmit.to(...)` 调用方式不变
- 事件开始、进行中、结束三个 slot 回调仍然保留
- 事件监听方法参数注入规则不变

## 未纳入本次修复的问题

- 长期事件审计数据保留策略
- `UtilEventSlot` 自定义 Bean 与默认 Bean 的覆盖方式
- 管理端事件列表中 `Method` 字段的序列化风险
- README 示例与真实 API 的一致性修正
