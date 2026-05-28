# util-base-control（服务控制工具模块）

该模块提供了一些常用的服务控制工具类，包括基础服务实现、分页处理和条件查询。

## 主要功能

- **基础服务实现**：提供通用的增删改查服务实现。
- **分页处理**：提供分页参数和分页结果的封装类。
- **条件查询**：提供基于条件的动态查询功能。

## 引入Maven依赖
```xml
<dependency>
    <groupId>com.kantboot</groupId>
    <artifactId>util-base-contr</artifactId>
</dependency>
```

## 使用方式

### Service 实现

```java
public interface IMyService extends IBaseService<MyEntity, Long> {
}
```

```java
public class MyServiceImpl extends BaseServiceImpl<MyEntity, Long> {
    // 继承 BaseServiceImpl 即可使用通用查询方法
}
```

### Controller 实现
```java
@RestController
@RequestMapping("/my")
public class MyController extends BaseAdminController<MyEntity, Long> {
    // 继承 BaseAdminController 即可使用通用的增删改查接口
}
```



## 类

### BaseServiceImpl

`BaseServiceImpl` 类实现了 `IBaseService` 接口，提供了通用的增删改查服务实现。

#### 方法

- `getAllHasCondition(ConditionGlobeEntity operatorGlobe)`: 查询所有符合条件的数据。
- `getAll()`: 查询所有数据。
- `getBodyData(PageParam<ConditionGlobeEntity> pageParam)`: 分页查询数据。
- `save(T entity)`: 保存数据。
- `saveBatch(List<T> entityList)`: 批量保存数据。
- `remove(T entity)`: 删除数据。
- `removeBatch(List<T> entityList)`: 批量删除数据。

### BaseAdminServiceImplInBaseAdminController

`BaseAdminServiceImplInBaseAdminController` 类实现了 `IBaseAdminServiceInBaseAdminController` 接口，提供了通用的增删改查服务实现，适用于管理控制器。

#### 方法

- `getAll(ConditionGlobeEntity operatorGlobe, Class<T> tClass)`: 查询所有符合条件的数据。
- `getBodyData(PageParam<ConditionGlobeEntity> pageParam, Class<T> tClass)`: 分页查询数据。
- `save(T entity, Class<T> tClass)`: 保存数据。
- `saveBatch(List<T> entityList, Class<T> tClass)`: 批量保存数据。
- `remove(T entity, Class<T> tClass)`: 删除数据。
- `removeBatch(List<T> entityList, Class<T> tClass)`: 批量删除数据。
- `getById(ID id, Class<T> tClass)`: 根据 ID 获取数据。

### BaseAdminController

`BaseAdminController` 类提供了通用的管理控制器，包含增删改查的 RESTful 接口。

#### 方法

- `getAll(ConditionGlobeEntity param)`: 查询所有符合条件的数据。
- `getBodyData(PageParam<ConditionGlobeEntity> pageParam)`: 分页查询数据。
- `save(T t)`: 保存数据。
- `saveBatch(List<T> tList)`: 批量保存数据。
- `remove(T t)`: 删除数据。
- `removeBatch(List<T> tList)`: 批量删除数据。
- `getById(ID id)`: 根据 ID 获取数据。

### IBaseService

`IBaseService` 接口定义了通用的增删改查服务方法。

#### 方法

- `getAllHasCondition(ConditionGlobeEntity operatorGlobe)`: 查询所有符合条件的数据。
- `getAll()`: 查询所有数据。
- `getBodyData(PageParam<ConditionGlobeEntity> pageParam)`: 分页查询数据。
- `save(T entity)`: 保存数据。
- `saveBatch(List<T> entityList)`: 批量保存数据。
- `remove(T entity)`: 删除数据。
- `removeBatch(List<T> entityList)`: 批量删除数据。

### IBaseAdminServiceInBaseAdminController

`IBaseAdminServiceInBaseAdminController` 接口定义了通用的增删改查服务方法，适用于管理控制器。

#### 方法

- `getAll(ConditionGlobeEntity operatorGlobe, Class<T> tClass)`: 查询所有符合条件的数据。
- `getBodyData(PageParam<ConditionGlobeEntity> pageParam, Class<T> tClass)`: 分页查询数据。
- `save(T entity, Class<T> tClass)`: 保存数据。
- `saveBatch(List<T> entityList, Class<T> tClass)`: 批量保存数据。
- `remove(T entity, Class<T> tClass)`: 删除数据。
- `removeBatch(List<T> entityList, Class<T> tClass)`: 批量删除数据。
- `getById(ID id, Class<T> tClass)`: 根据 ID 获取数据。

