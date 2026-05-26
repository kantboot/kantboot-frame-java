# util-jpa（JPA工具模块）

JPA工具模块，提供JPA增强功能，包括分页封装、ID生成、条件构建等。

## 功能特性

- **增强Repository**：扩展SimpleJpaRepository，实现不更新null字段
- **分页支持**：简化分页参数和结果处理
- **ID生成**：集成雪花ID生成器
- **条件构建**：支持复杂查询条件构建
- **命名策略**：提供无外键命名策略

## 快速开始

### 引入Maven依赖
```xml
<dependency>
    <groupId>com.kantboot</groupId>
    <artifactId>util-jpa</artifactId>
    <version>${revision}</version>
</dependency>
```

### 基本使用示例

1. **自定义Repository**：
```java
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    // 自定义查询方法...
}
```

2. **分页查询**：
```java
@GetMapping("/users")
public PageResult<User> getUsers(PageParam<User> pageParam) {
    Page<User> page = userRepository.findAll(pageParam.getPageable());
    return PageResult.of(page);
}
```

3. **使用雪花ID**：
```java
@Entity
public class User {
    @Id
    @GeneratedValue(generator = "snowflakeId")
    @GenericGenerator(name = "snowflakeId", strategy = "com.kantboot.util.jpa.id.GenerateSnowflakeId")
    private Long id;
    // 其他字段...
}
```

4. **条件查询**：
```java
public List<User> findUsers(Map<String, Object> conditionMap) {
    ConditionGlobeEntity condition = EasyConditionUtil.getConditionGlobeEntity(conditionMap);
    // 使用条件查询...
}
```

## API说明

### 核心类

#### ZeusJpaRepository
- **功能**：基础Repository实现
- **特性**：
  - 保存时不更新null字段
  - 自动处理乐观锁

#### PageParam
- **功能**：分页参数封装
- **字段**：
  - `pageSize`: 每页大小
  - `pageNumber`: 页码
  - `data`: 查询条件
  - `sort`: 排序字段
  - `orderBy`: 排序方式(ASC/DESC)

#### PageResult
- **功能**：分页结果封装
- **字段**：
  - `totalElements`: 总记录数
  - `totalPage`: 总页数
  - `content`: 数据列表
  - `number`: 当前页码
  - `size`: 每页大小

#### GenerateSnowflakeId
- **功能**：雪花ID生成器
- **使用方式**：
  - 通过`@GenericGenerator`注解配置

#### EasyConditionUtil
- **功能**：条件构建工具
- **方法**：
  - `getConditionGlobeEntity(Map<String, Object>)`: 从Map构建查询条件

## 配置说明

1. **JPA配置**：
```properties
spring.jpa.hibernate.naming.physical-strategy=com.kantboot.util.jpa.strategy.NoForeignKeyNamingStrategy
```

2. **ID生成配置**：
```java
// 在实体类上配置
@GenericGenerator(name = "snowflakeId", strategy = "com.kantboot.util.jpa.id.GenerateSnowflakeId")
```

## 最佳实践

1. **分页查询**：
   - 使用PageParam接收分页参数
   - 使用PageResult返回分页结果

2. **ID生成**：
   - 建议所有实体使用雪花ID
   - 避免使用自增ID

3. **条件查询**：
   - 使用EasyConditionUtil构建复杂条件
   - 保持条件参数命名规范

## 依赖

- Spring Data JPA
- Yitter IdGenerator (雪花ID)
- Hutool工具包
- Fastjson2

## 版本

${revision}
