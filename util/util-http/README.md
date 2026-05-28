# util-http（HTTP工具模块）

HTTP工具模块，提供HTTP客户端和请求头处理功能。

## 功能特性

- 基于OkHttp3的HTTP客户端
  - 支持GET/POST/PUT/DELETE/PATCH/HEAD/OPTIONS方法
  - 支持流式响应处理
  - 自动处理JSON和form-urlencoded内容类型
  - 内置超时设置(连接30分钟，读取60分钟)
  - 支持自定义请求头和内容类型

- 请求头处理工具
  - 获取Token/Authorization
  - 解析IP地址
  - 解析用户代理(设备类型、浏览器信息)
  - 获取语言编码、场景编码等业务信息

## 快速开始

### 引入Maven依赖

```xml
<dependency>
    <groupId>com.kantboot</groupId>
    <artifactId>util-http</artifactId>
    <version>${revision}</version>
</dependency>
```

### HTTP客户端使用

```java
// 基本用法
HttpSendConfig config = new HttpSendConfig()
    .setUrl("https://api.example.com")
    .setMethod("POST")
    .setContentType("application/json")
    .setBody(new HashMap<String, Object>() {{
        put("key", "value");
    }});

String response = HttpSendUtil.send(config);

// 流式处理
HttpSendUtil.send(config, new HttpResponseStreamCallback() {
    @Override
    public void run(String chunk) {
        System.out.println("Received chunk: " + chunk);
    }

    @Override
    public void finish(String fullResponse) {
        System.out.println("Full response: " + fullResponse);
    }
});
```

### 请求头处理

```java
@Autowired
private HttpRequestHeaderUtil headerUtil;

// 获取IP地址
String ip = headerUtil.getIp();

// 获取设备信息
String device = headerUtil.getDevice();

// 获取浏览器信息
String browser = headerUtil.getBrowser();
```

## API说明

### HttpSendUtil

- `send(HttpSendConfig config)`: 发送HTTP请求并返回完整响应
- `send(HttpSendConfig config, HttpResponseStreamCallback callback)`: 发送HTTP请求并使用回调处理流式响应

### HttpSendConfig

| 参数 | 类型 | 说明 |
|------|------|------|
| url | String | 请求URL |
| method | String | HTTP方法(GET/POST等) |
| contentType | String | 内容类型(如application/json) |
| headers | Map<String, Object> | 请求头 |
| body | Object | 请求体 |

### HttpRequestHeaderUtil

提供从请求头中获取各种信息的方法，包括：
- getToken(): 获取Token
- getAuthorization(): 获取Authorization
- getIp(): 获取客户端IP
- getDevice(): 获取设备信息
- getBrowser(): 获取浏览器信息
- getLanguageCode(): 获取语言编码
- getSceneCode(): 获取场景编码
- getUserId(): 获取用户ID
- getProjectCode(): 获取项目代码

## 依赖

- OkHttp3
- Hutool
- Fastjson2
- Spring Web
- Jakarta Servlet API

## 版本

${revision}
