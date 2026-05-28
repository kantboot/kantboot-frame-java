# 数据变化工具模块（util-data-change）

`util-data-change` 模块提供了记录数据变化的功能，通过注解和切面实现数据变化的统一管理。

## 功能

- 记录单个数据变化
- 批量记录数据变化
- 根据键获取UUID

## 引入Maven依赖
```xml
<dependency>
    <groupId>com.kantboot</groupId>
    <artifactId>util-data-change</artifactId>
    <version>${revision}</version>
</dependency>
```

## 使用方法

### 数据变化的使用

#### 使用注解
```java
import com.kantboot.util.data.change.annotaion.DataChange;

public class ExampleService {

    @DataChange(key = "exampleKey")
    public void exampleMethod() {
        // 方法实现
    }
}
```

#### 使用方法
```java
import com.kantboot.util.data.change.service.IDataChangeService;
import jakarta.annotation.Resource;

public class ExampleController {

    @Resource
    private IDataChangeService dataChangeService;

    public void example() {
        dataChangeService.dataChange("exampleKey");
    }
}
```

## 获取数据变化UUID
```java
import com.kantboot.util.data.change.service.IDataChangeService;
import jakarta.annotation.Resource;

@RestController
@RequestMapping("/example")
public class ExampleController {
    
    @Resource
    private IDataChangeService dataChangeService;
    
    @GetMapping("/example")
    public void example() {
        String uuid = dataChangeService.getUUID("exampleKey");
        System.out.println(uuid);
    }
}
```

## 常量

使用 `DataChangeCommonKeyConstants` 类中的常用常量来避免硬编码键值。

```java
import com.kantboot.util.data.change.constants.DataChangeCommonKeyConsts;

public class ExampleService {

    @DataChange(key = DataChangeCommonKeyConsts.CLIENT_INIT)
    public void exampleMethod() {
        // 方法实现
    }
}
```
