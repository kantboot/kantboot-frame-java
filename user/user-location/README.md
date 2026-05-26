# 用户位置模块（user-location）

`user-location` 模块用于管理用户账户的地理位置信息，包括位置的保存和查询等功能。

## 功能
- 保存用户位置信息
- 查询用户位置信息

## 主要实体类
- `UserAccountLocation`: 用户账号位置实体类
- `UserAccountLocationLog`: 用户账号位置变动记录实体类

## 主要接口
- `IUserAccountLocationService`: 用户账号位置服务接口，定义了保存位置信息的方法。

## 控制器
- `UserAccountLocationController`: 提供保存用户位置信息的接口。

## 数据库操作
- `UserAccountLocationRepository`: 用户账号位置的数据库操作接口。
- `UserAccountLocationLogRepository`: 用户账号位置变动记录的数据库操作接口。

## 使用方法
1. 在项目中引入 `user-location` 模块的依赖。
2. 配置数据库连接。
3. 使用 `UserAccountLocationController` 提供的接口进行位置管理操作。

## 示例
### 保存用户位置信息
```java
@RestController
@RequestMapping("/user-location-web/userAccountLocation")
public class UserAccountLocationController {

    @Resource
    private IUserAccountLocationService userAccountLocationService;

    @RequestMapping("/saveSelf")
    public UserAccountLocation saveSelf(UserAccountLocation entity) {
        return userAccountLocationService.saveSelf(entity);
    }
}
```

### 查询用户位置信息
```java
public interface UserAccountLocationRepository extends JpaRepository<UserAccountLocation, Long> {
    UserAccountLocation findByUserAccountId(Long userAccountId);
}
```
