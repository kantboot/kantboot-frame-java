# AI 模块开发规划

## 模块结构图

```
kantboot-frame-java/
├── functional/
│   └── functional-ai/                          ← 核心模块
│       ├── domain/
│       │   ├── entity/
│       │   │   ├── FunctionalAiChatModel.java               # 底层技术配置（apiUrl/apiKey/modelCode）
│       │   │   ├── FunctionalAiChatRole.java                # AI 角色/人设
│       │   │   ├── FunctionalAiChatRoleType.java            # 角色分类
│       │   │   ├── FunctionalAiChatRoleLabel.java           # 角色标签
│       │   │   ├── FunctionalAiChatRoleLanguageSupport.java # 角色支持的语言
│       │   │   ├── FunctionalAiChatRolePresets.java         # 预设消息（多语言 system prompt + few-shot）
│       │   │   ├── FunctionalAiChatDialog.java              # 会话
│       │   │   └── FunctionalAiChatDialogMessage.java       # 消息
│       │   ├── dto/
│       │   │   ├── FunctionalAiChatDTO.java                 # 发送消息请求体
│       │   │   └── FunctionalAiChatRoleDTO.java             # 角色管理请求体
│       │   └── vo/
│       │       ├── FunctionalAiChatTransmissionVO.java      # 流式推送数据结构
│       │       └── FunctionalAiChatMessageAllVO.java        # 组装好的上下文消息（role + content）
│       ├── dao/
│       │   └── repository/
│       │       ├── FunctionalAiChatModelRepository.java
│       │       ├── FunctionalAiChatRoleRepository.java
│       │       ├── FunctionalAiChatRoleTypeRepository.java
│       │       ├── FunctionalAiChatRoleLabelRepository.java
│       │       ├── FunctionalAiChatRoleLanguageSupportRepository.java
│       │       ├── FunctionalAiChatRolePresetsRepository.java
│       │       ├── FunctionalAiChatDialogRepository.java
│       │       └── FunctionalAiChatDialogMessageRepository.java
│       ├── constants/
│       │   └── FunctionalAiDialogMessageStatusCodeConstants.java  # thinking / finish / error
│       ├── exception/
│       │   └── FunctionalAiException.java
│       ├── service/
│       │   ├── IFunctionalAiChatModelService.java
│       │   ├── IFunctionalAiChatRoleService.java
│       │   ├── IFunctionalAiChatDialogService.java
│       │   ├── IFunctionalAiChatDialogMessageService.java
│       │   └── impl/
│       │       ├── FunctionalAiChatModelServiceImpl.java
│       │       ├── FunctionalAiChatRoleServiceImpl.java
│       │       ├── FunctionalAiChatDialogServiceImpl.java       # 核心：发消息、流式推送
│       │       └── FunctionalAiChatDialogMessageServiceImpl.java
│       ├── slot/
│       │   └── FunctionalAiChatSlot.java                        # 扩展点，默认抛异常
│       ├── method/
│       │   └── FunctionalAiChatMethod.java                      # 抽象回调类
│       └── web/
│           └── controller/
│               ├── FunctionalAiChatModelController.java          # 模型配置管理（管理端）
│               ├── FunctionalAiChatRoleController.java           # 角色查询（用户端）
│               ├── FunctionalAiChatRoleControllerOfAdmin.java    # 角色管理（管理端）
│               ├── FunctionalAiChatDialogController.java         # 会话管理
│               ├── FunctionalAiChatDialogMessageController.java  # 消息查询
│               └── FunctionalAiChatProxyController.java          # 透传代理（OpenAI 兼容接口）
│
└── official-plugin/
    └── official-plugin-functional-ai/         ← OpenAI 兼容实现
        └── plugin/
            └── OfficialPluginFunctionalAiPlugin.java
```

---

## 数据模型

### FunctionalAiChatModel（底层技术配置）

> 可复用的 AI 接口配置，多个角色可共享同一个 Model。

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| name | String | 配置名称（如 "GPT-4o / OpenAI"） |
| apiUrl | String | OpenAI 兼容端点（如 https://api.openai.com） |
| requestHeaders | JSONB | 请求头键值对，兼容各厂商认证方式（见示例） |
| modelCode | String | 实际模型名（gpt-4o / deepseek-v3 / llama3） |
| temperature | Double | 温度参数 |
| maxTokens | Integer | 最大 token 数 |

**requestHeaders 示例：**

```jsonc
// OpenAI / Codex
{ "Authorization": "Bearer sk-..." }

// Anthropic Claude（订阅账号）
{ "x-api-key": "sk-ant-...", "anthropic-version": "2023-06-01" }

// Ollama（本地，无需认证）
{}

// Azure OpenAI
{ "api-key": "...", "api-version": "2024-02-01" }

// 火山引擎
{ "Authorization": "Bearer ..." }
```

> 发起 HTTP 请求时将 requestHeaders 全部附加到请求头，不在代码中硬编码任何认证字段。

---

### FunctionalAiChatRole（AI 角色/人设）

> 对应旧代码 `BusAiChatModel`，是用户看到、选择的 AI 角色。

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| modelId | Long | 关联底层模型配置 |
| typeId | Long | 角色分类 |
| name | String | 角色名称 |
| description | String | 简介 |
| introduction | String | 详细介绍 |
| categoryCode | String | 种类（person / book / ...） |
| genderCode | String | 性别（male / female / other） |
| gmtBirthday | Date | 生日 |
| age | Integer | 年龄（空则由生日计算） |
| fileIdOfAvatar | Long | 头像文件 ID |
| sourceLanguageCode | String | 角色原生语言 |
| priority | Integer | 排序优先级 |
| labels | List | 标签（OneToMany） |
| languageSupports | List | 支持语言（OneToMany） |

---

### FunctionalAiChatRoleType（角色分类）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| name | String | 分类名称 |
| sort | Integer | 排序 |
| sourceLanguageCode | String | 原生语言 |

---

### FunctionalAiChatRoleLabel（角色标签）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| roleId | Long | 关联角色 |
| text | String | 标签文字 |
| priority | Integer | 排序优先级 |

---

### FunctionalAiChatRoleLanguageSupport（角色支持语言）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| roleId | Long | 关联角色 |
| languageCode | String | 语言编码（zh_CN / en_US / ...） |

---

### FunctionalAiChatRolePresets（预设消息）

> 对应旧代码 `BusAiChatModelPresets`。支持多语言，支持配置多条（system prompt + few-shot 示例对话均可）。

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| roleId | Long | 关联角色 |
| languageCode | String | 语言编码 |
| role | String | system / user / assistant |
| content | String(TEXT) | 内容 |
| priority | Integer | 排序（决定插入上下文的顺序） |

---

### FunctionalAiChatDialog（会话）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键（Snowflake） |
| roleId | Long | 关联角色 |
| modelId | Long | 指定底层模型（可空，空则 fallback 到 Role 的默认 modelId） |
| userAccountId | Long | 所属用户 |
| languageCode | String | 会话语言（用于匹配 Presets） |

---

### FunctionalAiChatDialogMessage（消息）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键（Snowflake） |
| dialogId | Long | 所属会话 |
| role | String | user / assistant |
| content | String(TEXT) | 消息内容 |
| statusCode | String | thinking / finish / error |
| userAccountId | Long | 发送者（assistant 消息为空） |

---

## 推送方式

支持两种模式，前端按需选择：

| 模式 | 接口 | 适用场景 |
|------|------|----------|
| WebSocket | `sendMessage()` | 已建立长连接，消息跨页面共享 |
| 流式响应（SSE） | `sendMessageOfStream()` → `ResponseEntity<StreamingResponseBody>` | 简单场景，无需 WebSocket |

两种模式共用同一个 Slot（`FunctionalAiChatSlot`），差异只在 Service 层如何处理回调。

---

## 核心流程

### 模式一：WebSocket 推送

```
用户调用 sendMessage(dialogId, content)
  │
  ├─ 1. 加锁（防并发重复发送）
  ├─ 2. 保存 user 消息
  ├─ 3. 创建 assistant 消息（statusCode = thinking）
  ├─ 4. 异步线程 → FunctionalAiChatSlot.sendMessageHasStream()
  │       │
  │       ├─ dialog.modelId（优先）→ 若为空则 role.modelId → model（apiUrl / requestHeaders / modelCode）
  │       ├─ 按 languageCode 取 Presets，组装上下文（Presets + 历史消息）
  │       ├─ 发起 OpenAI 兼容流式请求
  │       │
  │       ├─ 每个 chunk → functionalMessageService 推送（WebSocket）
  │       │       emit: "aiChat"
  │       │       data: FunctionalAiChatTransmissionVO
  │       │               { text, content, done, dialogId, messageId }
  │       │
  │       └─ 完成 → 更新 assistant 消息 statusCode = finish → 解锁
  │
  └─ 立即返回
```

### 模式二：流式响应（SSE）

```
用户调用 sendMessageOfStream(dialogId, content)
  │
  ├─ 1. 加锁
  ├─ 2. 保存 user 消息
  ├─ 3. 创建 assistant 消息（statusCode = thinking）
  ├─ 4. 返回 ResponseEntity<StreamingResponseBody>
  │       │
  │       └─ StreamingResponseBody 内同步执行 Slot
  │               ├─ 组装上下文（Presets + 历史消息）
  │               ├─ 发起流式请求
  │               ├─ 每个 chunk 直接写入 HTTP 响应输出流
  │               │       格式：data: {JSON}\n\n
  │               └─ 完成 → 更新消息 statusCode = finish → 解锁
  │
  └─ 前端通过 EventSource 或 fetch ReadableStream 接收
```

### 模式三：透传代理（OpenAI 兼容接口）

```
调用 POST /functional-ai/proxy/{roleId}/chat/completions
  请求体：{ messages: [...], stream: true/false }
  │
  ├─ 1. 按 roleId 找 Role，可选传 modelId 指定底层模型（空则用 Role 默认 modelId）→ 找 Model
  ├─ 2. 按请求语言取 Presets，prepend 到 messages 头部
  ├─ 3. 直接透传给 AI 接口（/v1/chat/completions）
  └─ 4. 原样返回 AI 的响应（流式或非流式）
       （不存储消息，无会话管理，纯无状态）
```

---

## Slot 设计

Slot 本身与推送方式无关，只负责调用 AI 接口并回调，上层决定如何处理 chunk。

```java
// functional-ai 定义扩展点
FunctionalAiChatSlot
  └── sendMessageHasStream(FunctionalAiChatDTO dto, FunctionalAiChatMethod method)
          默认：throw BaseException（无插件实现）

// FunctionalAiChatMethod 抽象回调类
FunctionalAiChatMethod
  ├── run(String text, String content, Boolean done)   // 每个 chunk 触发
  └── finish(String content)                           // 全部完成触发

// official-plugin-functional-ai 覆盖实现
OfficialPluginFunctionalAiPlugin (@Configuration)
  └── @Bean FunctionalAiChatSlot
          → 读取 model 的 apiUrl / modelCode / requestHeaders
          → POST {apiUrl}/v1/chat/completions，将 requestHeaders 全部附加到请求头
          → 流式解析 SSE（data: {...}\n\n）
          → 逐 chunk 回调 method.run()
          → 完成后回调 method.finish()
```

### Service 层如何区分两种推送模式

```
WebSocket 模式：
  sendMessage()
    → 异步线程执行 Slot
    → method.run() 内调用 functionalMessageService.sendMessage()（WebSocket 推送）

SSE 模式：
  sendMessageOfStream()
    → 返回 StreamingResponseBody（同步执行，持续写输出流）
    → method.run() 内直接 outputStream.write(chunk)
```

---

## 开发任务清单

### functional-ai 模块

- [ ] 创建 Maven 模块，配置 pom.xml
- [ ] `FunctionalAiChatModel` 实体（底层技术配置）
- [ ] `FunctionalAiChatRole` 实体（角色/人设）
- [ ] `FunctionalAiChatRoleType` 实体（角色分类）
- [ ] `FunctionalAiChatRoleLabel` 实体（角色标签）
- [ ] `FunctionalAiChatRoleLanguageSupport` 实体（支持语言）
- [ ] `FunctionalAiChatRolePresets` 实体（多语言预设消息）
- [ ] `FunctionalAiChatDialog` 实体（会话）
- [ ] `FunctionalAiChatDialogMessage` 实体（消息）
- [ ] `FunctionalAiChatDTO` / `FunctionalAiChatRoleDTO`
- [ ] `FunctionalAiChatTransmissionVO` / `FunctionalAiChatMessageAllVO`
- [ ] `FunctionalAiDialogMessageStatusCodeConstants`
- [ ] `FunctionalAiException`
- [ ] Repository 接口（8 张表）
- [ ] `FunctionalAiChatSlot`（扩展点）
- [ ] `FunctionalAiChatMethod`（抽象回调类）
- [ ] `IFunctionalAiChatModelService` + impl（模型配置 CRUD）
- [ ] `IFunctionalAiChatRoleService` + impl（角色 CRUD + 语言/标签/预设管理）
- [ ] `IFunctionalAiChatDialogService` + impl（创建会话、WebSocket 发消息、SSE 流式发消息）
- [ ] `IFunctionalAiChatDialogMessageService` + impl（消息查询）
- [ ] Controller（模型管理、角色管理、会话管理、消息管理、透传代理）
- [ ] 注册到 `functional-all` pom

### official-plugin-functional-ai 模块

- [ ] 创建 Maven 模块，配置 pom.xml
- [ ] `OfficialPluginFunctionalAiPlugin`（OpenAI 兼容 HTTP 流式请求）
- [ ] 注册到 `official-plugin-all` pom
