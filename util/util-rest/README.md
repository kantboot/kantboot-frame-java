# util-rest（REST工具模块）

REST API工具模块，提供统一的响应格式和异常处理机制。

## 功能特性

- 统一REST响应格式
  - 标准化成功/失败响应结构
  - 包含状态码、消息和数据
  - 支持分页数据封装

- 异常处理
  - 基础异常类
  - 统一异常处理机制
  - 自定义异常状态码

- 状态常量
  - 预定义常用成功状态码
  - 可扩展的自定义状态码

## 快速开始

### 引入Maven依赖
```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>util-rest</artifactId>
    <version>${revision}</version>
</dependency>
```

### 基本使用示例


### 1. RestResult

`RestResult` 类用于封装接口的返回结果，提供了便捷的成功和错误返回方法。

```java
public class RestResult<T> implements Serializable {
    // ...existing code...
    public static <T> RestResult<T> success(T data, String code) {
        return new RestResult<T>()
                .setState(SUCCESS_STATE)
                .setData(data)
                .setMsgDictCode(code)
                .setIsSuccess(true)
                .setStateCode(SUCCESS_STATE_CODE);
    }

    public static RestResult<?> error(String msg) {
        return new RestResult<>()
                .setState(FAIL_STATE)
                .setErrMsg(msg)
                .setIsSuccess(false)
                .setMsgDictCode(FAIL_STATE_CODE)
                .setStateCode(FAIL_STATE_CODE);
    }
    // ...existing code...
}
```

### 2. BaseException

`BaseException` 类用于统一处理异常，并将异常信息转换为 `RestResult` 返回给客户端。

```java
@ControllerAdvice
public class BaseException extends RuntimeException {
    // ...existing code...
    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public RestResult<String> exceptionHandler(BaseException e){
        return new RestResult<String>().setState(e.getState()).setErrMsg(e.getMessage()).setStateCode(e.getStateCode())
                .setMsgDictCode(e.getStateCode());
    }
    // ...existing code...
}
```

### 3. StateEntity

`StateEntity` 类用于封装状态码和消息，方便在 `RestResult` 中使用。

```java
public class StateEntity {
    // ...existing code...
    public static StateEntity of(String stateCode, String msg) {
        return new StateEntity()
                .setStateCode(stateCode)
                .setMsg(msg);
    }
    // ...existing code...
}
```

### 4. CommonSuccessStateConstants

`CommonSuccessStateConstants` 类定义了常用的成功状态码和消息。

```java
public class CommonSuccessStateConstants {
    public static final StateEntity GET_SUCCESS = StateEntity.of("getSuccess", "Get Success");
    // ...existing code...
}
```

## 示例

```java
import com.kantboot.util.rest.result.RestResult;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

    @GetMapping("/example")
    public RestResult<?> example(@RequestParam("flag") Boolean flag) {
        if (!flag) {
            // 失败示例，参数1 为状态码，参数2 为消息，参数3 为语言代码
//            throw new BaseException("flagIsFalse", "Flag is false","zh_CN");
            throw new BaseException("flagIsFalse", "Flag is false");
        }

        Object data = new Object();
        // 成功示例
        // 参数1 为数据，参数2 为状态码，参数3 为消息，参数4 为语言代码
//        return RestResult.success(data, "getSuccess");
//        return RestResult.success(data, "getSuccess", "GET_SUCCESS", "zh_CN");
        return RestResult.success(data, CommonSuccessStateConsts.GET_SUCCESS);
    }

}
```

## API说明

### RestResult

- `success()`: 创建成功响应(无数据)
- `success(Object data)`: 创建带数据的成功响应
- `success(String message, Object data)`: 创建带消息和数据的成功响应
- `error(StateEntity state)`: 创建错误响应
- `error(String message, StateEntity state)`: 创建带消息的错误响应
- `of(StateEntity state, String message, Object data)`: 创建自定义响应

### StateEntity

- `of(int code, String msg)`: 创建状态实体
- `getCode()`: 获取状态码
- `getMsg()`: 获取状态消息

### BaseException

- 基础异常类，包含状态实体和错误消息
- 可用于自定义业务异常

### CommonSuccessStateConsts

- 预定义常用成功状态码
  - `SUCCESS`: 通用成功(200)
  - `CREATED`: 创建成功(201)
  - `ACCEPTED`: 请求已接受(202)

## 注意事项

1. 响应格式规范：
   - 成功响应应包含状态码和数据
   - 错误响应应包含错误状态码和消息
   - 业务异常应使用BaseException抛出

2异常处理建议：
   - 业务异常应提供明确的错误状态码
   - 系统异常应记录日志
   - 敏感信息不应直接返回给客户端

## 依赖

- Spring Web
- Jackson

## 版本

${revision}
