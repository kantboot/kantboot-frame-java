# util-auth（认证工具模块）

认证和权限控制工具模块，提供基于注解的权限控制功能。

## 功能特性

- 基于注解的权限控制
  - 定义接口访问权限
  - 支持多语言描述
  - 灵活的权限配置

- 认证配置
  - 无需登录配置
  - 全员放行配置
  - 特定权限配置

## 快速开始

### 引入Maven依赖
```xml
<dependency>
    <groupId>com.kantboot</groupId>
    <artifactId>util-auth</artifactId>
    <version>${revision}</version>
</dependency>
```

### 基本使用示例

```java
// 无需登录的接口
@AuthInit(noNeedLogin = true)
@GetMapping("/public")
public String publicApi() {
    return "无需登录即可访问";
}

// 需要特定权限的接口
@AuthInit(
    name = "用户管理",
    description = "获取用户列表",
    permissionCodes = {"user:list"}
)
@GetMapping("/users")
public List<User> getUsers() {
    return userService.findAll();
}

// 全员可访问(需登录)
@AuthInit(allPass = true)
@GetMapping("/common")
public String commonApi() {
    return "所有登录用户均可访问";
}
```

## API说明

### @AuthInit 注解

- `name`: 接口名称(可选)
- `description`: 接口描述(可选)
- `sourceLanguageCode`: 源语言编码(默认zh_CN)
- `noNeedLogin`: 是否无需登录(默认false)
- `allPass`: 是否全员可访问(需登录，默认false)
- `permissionCodes`: 所需权限编码数组(默认空)

## 配置说明

1. 确保项目中已配置认证拦截器，示例：

```java
@Configuration
public class AuthConfig implements WebMvcConfigurer {
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor())
            .addPathPatterns("/**");
    }
    
    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }
}
```

2. 权限验证逻辑需要自行实现AuthInterceptor

## 注意事项

1. 权限编码规范：
   - 建议使用模块:操作 的格式，如 user:add
   - 保持编码简洁且有意义

2. 安全建议：
   - 敏感接口必须设置权限控制
   - 避免过度使用noNeedLogin
   - 定期审查权限配置

## 依赖

- Spring Web
- Spring Security (可选)

## 版本

${revision}
