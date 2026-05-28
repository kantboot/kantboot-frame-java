# KantBoot 框架开发规范

本文档用于指导团队在基于 KantBoot 多模块框架开展业务开发时的统一约定，涵盖工程结构、依赖管理、编码规范、配置策略、测试与发布流程等内容。

## 1. 框架结构与模块边界

- **父工程职责**：所有业务模块都必须继承根 `pom.xml` 中的父工程，保持 Java 21 目标版本、Spring Boot 3.4.3 以及统一的插件配置。父工程的 `<modules>` 列表定义了框架支持的全部子模块，新增模块前需评估是否已有对应能力可复用，避免重复建设。【F:pom.xml†L1-L77】
- **内部模块定位**：`global`、`dependencies` 等模块仅供框架内部复用，外部项目禁止直接引用，开发时注意遵循 README 中的边界说明，防止破坏整体装配关系。【F:global/README.md†L1-L35】【F:dependencies/README.md†L1-L18】
- **工具与能力复用**：`util` 聚合了认证、缓存、文件、国际化等通用能力，建议在 `util-all` 与按需依赖之间做权衡：快速原型可临时引入汇总包，正式项目按模块拆分以降低体积并便于维护。每个子模块都应补齐 README，用于记录能力范围与配置项，保持模块文档的一致性。【F:util/README.md†L1-L66】
- **示例工程参考**：`project-lxyz`、`project-fogg` 通过 `starter-parent` 继承全量依赖，是落地业务时的最佳实践示例。在新建项目时优先参照其结构进行骨架搭建，以确保引导类、配置目录与依赖声明符合框架预期。【F:pom.xml†L19-L37】

### 1.1 模块命名规范

- **顶层聚合模块**：根 POM 仅接受语义化的单词命名（如 `dependencies`、`util`、`system`、`thirdparty`、`starter-parent`），用于区分依赖管理、工具、系统能力、第三方集成与 Starter，禁止出现含义不明的缩写。【F:pom.xml†L19-L38】
- **领域前缀约定**：各聚合模块下的子模块必须携带统一前缀，以 `前缀-能力` 的形式命名：
    - `util-*`：基础工具类能力，当前包含 `util-auth`、`util-cache`、`util-file` 等，形成清晰的一致性，且 `util-all` 代表汇总模块。【F:util/pom.xml†L15-L41】
    - `system-*`：平台级系统配置（`system-setting`、`system-dict` 等），并约定 `system-all` 作为一次性聚合包。【F:system/pom.xml†L14-L24】
    - `user-*`：用户域模块（`user-account`、`user-balance` 等），由 `user-all` 汇总，避免与业务自定义用户模块混淆。【F:user/pom.xml†L14-L26】
    - `functional-*`、`engine-*`、`fp-*`、`tool-*`：分别用于通用业务功能、运行引擎、场景化产品与工具服务，每个聚合 POM 都以相同前缀命名子模块，并提供 `*-all` 汇总模块。【F:functional/pom.xml†L13-L25】【F:engine/pom.xml†L13-L23】【F:fp/pom.xml†L13-L21】【F:tool/pom.xml†L13-L21】
    - `thirdparty-*`、`official-plugin-*`：第三方服务与官方插件均以服务名/场景后缀命名（如 `thirdparty-wechat`、`official-plugin-tool-area`），统一由 `thirdparty-all`、`official-plugin-all` 汇总，以降低依赖选择成本。【F:thirdparty/pom.xml†L13-L26】【F:official-plugin/pom.xml†L13-L24】
- **客户端/服务端角色**：涉及不同运行角色时使用角色后缀，例如网关模块通过 `gateway-server`、`gateway-client` 区分服务端与客户端；若新增角色应遵循相同后缀策略。【F:gateway/pom.xml†L13-L21】
- **示例与项目模块**：示例工程以 `project-*` 命名（如 `project-lxyz`、`project-fogg`），保持可读性，并且所有 Starter/示例必须位于 `starter-parent` 聚合下，使用 `starter-*` 前缀表示产物类型。【F:pom.xml†L28-L38】【F:starter-parent/pom.xml†L1-L21】

| 聚合目录/前缀 | 适用范围 | 命名要求 | 示例 |
| --- | --- | --- | --- |
| `dependencies` | 统一依赖管理 | 仅允许语义化的单体聚合名称；内部 BOM 通过 `<dependencyManagement>` 管理 | `dependencies` |
| `global` | 框架级全局能力 | 聚合 POM 使用单词名 `global`，子模块以 `global-*` 拓展 | `global-standalone` |
| `util-*` | 工具类模块 | 使用 `util-能力` 格式，并保留 `util-all` 作为汇总 | `util-cache`、`util-file`、`util-all` |
| `system-*` | 平台系统配置 | `system-域名`，并提供 `system-all` | `system-setting`、`system-dict` |
| `user-*` | 用户域能力 | `user-领域` + `user-all` | `user-account`、`user-balance` |
| `functional-*` | 通用业务功能 | `functional-场景` + `functional-all` | `functional-icon`、`functional-email` |
| `engine-*` | 引擎与监控 | `engine-领域` + `engine-all` | `engine-jvm`、`engine-database` |
| `fp-*` | 场景化产品 | `fp-产品` + `fp-all` | `fp-carousel`、`fp-community` |
| `tool-*` | 工具服务 | `tool-业务` + `tool-all` | `tool-area`、`tool-ip` |
| `thirdparty-*` | 三方集成 | `thirdparty-服务` + `thirdparty-all` | `thirdparty-wechat`、`thirdparty-github` |
| `official-plugin-*` | 官方插件 | `official-plugin-场景` + `official-plugin-all` | `official-plugin-tool-area` |
| `gateway-*` | 客户端/服务端角色 | `gateway-角色`，角色使用 `server`/`client` 等结尾 | `gateway-server`、`gateway-client` |
| `starter-*` | Starter 产物 | 统一 `starter-parent` 聚合，子模块 `starter-*` | `starter-parent`、`starter-standalone` |
| `project-*` | 示例项目 | `project-客户或场景` | `project-lxyz`、`project-fogg` |
| `developer` | 开发工具 | 单独命名，必要时以 `developer-*` 扩展 | `developer` |

上述表格需在新增模块评审时同步更新，以便追踪所有聚合目录与命名规则之间的对应关系，确保任何子模块都能被快速定位并避免重复建设。

### 1.2 模块命名审核流程

1. **提出命名方案**：在编写 POM 之前，需求方必须提交命名方案，包含聚合目录、前缀、能力描述和计划输出的 `*-all`/`*-standalone` 模块，方便评审人员确认其落位位置。
2. **校验前缀冲突**：评审时需要比对现有聚合目录是否已有同名能力，若命名会与既有模块混淆，则要求调整描述词或拆分子域，严禁在不同聚合下复用相同后缀。
3. **更新矩阵**：命名获批后，先更新上方矩阵表格，再创建 POM 与 README，保证在代码变更提交之前，规范文档已经反映新的命名信息。
4. **清单式自检**：发起合并请求前使用以下清单自检：
    - `pom.xml` 中模块目录与命名方案一致，且遵循聚合前缀；
    - README 中注明模块前缀、角色（server/client 等）与是否提供 `*-all`；
    - 评审记录附带命名决策截图或链接，方便后续追溯。

满足以上步骤后才允许提交 PR，从流程上杜绝“先编码再命名”的情况，确保整个仓库的模块前缀始终保持一致。

## 2. 版本与依赖管理

- **版本占位符**：统一使用 `${revision}` 作为内部模块版本号，且保持 `${kantboot.version}` 与 `${revision}` 同源，禁止在子模块中写死具体版本号。若需发布新版本，先在父工程属性中调整占位符，再执行版本对齐。【F:README.md†L9-L15】
- **依赖集中维护**：新增三方依赖时应优先录入 `dependencies` 模块的 BOM，利用占位符管理版本，避免直接在业务模块声明固定版本号。提交前需更新该模块的 README，说明依赖用途与版本，以便团队共享知识。【F:dependencies/README.md†L1-L68】
- **私有仓库使用**：构建流程默认走阿里云公共仓库镜像（`alimaven`），若需使用公司私服，请在父工程或企业级 `settings.xml` 中扩展配置，避免直接修改子模块 POM，以免破坏统一仓库策略。【F:pom.xml†L78-L101】

## 3. 编码与包结构规范

- **包命名约定**：统一使用 `com.kantboot.<业务域>` 前缀；框架级通用代码位于 `com.kantboot.global` 或对应模块包名下。业务工程扩展时，应置于自有顶级包（如 `com.company.project`），并通过 `@EnableKantboot` 激活框架能力。【F:global/README.md†L11-L34】
- **组件设计**：新增通用组件时优先封装在各自 util 或 system 子模块内，并提供自动配置、配置属性、异常模型等配套结构，保持对外 API 简洁一致。若模块需要对外暴露注解或启动器，确保命名与现有注解（如 `@KantbootApplication`、`@EnableKantboot`）风格一致。【F:global/README.md†L17-L34】
- **文档要求**：所有新模块、核心组件与公共配置必须编写 README，包含功能概述、依赖、配置示例与最佳实践。文档需支持中英文或至少中文说明，保持和现有 util 模块一致的表格化结构。【F:util/README.md†L1-L90】

## 4. 配置与环境管理

- **全局配置约束**：框架级公共配置集中在 `application-global.yml` 或全局属性工具类，修改前需评估对其它模块的影响，严格履行评审流程。建议通过环境变量或外部化配置覆盖环境差异，避免直接改动默认值。【F:global/README.md†L17-L35】
- **模块配置命名**：模块自定义配置应使用模块名前缀（如 `i18n.*`、`cache.*`），并在 README 中列出默认值及覆盖方式，确保配置扫描与文档一致。业务项目引用 util 子模块后需在 `application.yml` 中补齐对应配置段。【F:util/README.md†L47-L66】
- **敏感信息管理**：数据库、消息队列等凭据必须通过环境变量或安全配置中心注入，禁止提交到版本库。涉及第三方平台的密钥配置应落在 `system-setting` 或类似配置模块提供的安全存储机制中。

## 5. 质量保障

- **单元与集成测试**：公共模块必须配套单元测试，覆盖核心工具类与自动配置。业务模块至少在 CI 中执行 `mvn test`，必要时补充容器化集成测试。新增功能应提供示例或演示用例，便于示例工程复现。
- **代码审查**：所有合并请求需通过双人 Code Review，重点检查依赖是否纳入 BOM、配置是否符合命名约定、是否影响全局模块耦合。
- **静态检查**：推荐接入 Checkstyle/Spotless、SonarQube 等工具，保持代码风格统一，避免潜在缺陷进入主干。

## 6. 发布流程

1. 更新 `pom.xml` 中的 `${kantboot.version}` / `${revision}` 属性，同步所有子模块版本。
2. 在 `CHANGELOG` 或相应发布说明中记录本次变更范围与影响面。
3. 执行 `mvn clean install` 验证模块编译与打包，通过后方可推送标签与发布制品。
4. 对外提供的 Starter 或示例工程需同步更新依赖版本及文档，确保使用者能够复现构建环境。

## 7. 协作约定

- 建议按照模块职能划分责任人，确保每个目录有明确维护者。
- 提交信息需包含模块名、变更动机与影响说明，方便后续追溯。
- 遇到跨模块需求时，优先在 `developer` 模块中提供脚手架或工具支持，降低重复劳动。

通过以上规范，可确保团队在 KantBoot 框架下协同开发时保持一致的模块边界、依赖策略与工程质量，加速业务能力的交付与演化。