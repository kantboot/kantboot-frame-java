package com.kantboot.util.rest.exception;

import com.kantboot.util.rest.result.RestResult;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * 异常处理的基类
 * Base class for exception handling
 *
 * @author 方某方
 */
@ControllerAdvice
@Setter
@Getter
@Accessors(chain = true)
public class BaseException extends RuntimeException
        implements Serializable, Supplier<BaseException> {

    /**
     * 便捷的错误码(数字编码)
     * Convenient error code (digital code)
     * 默认值为3000
     * Default value is 3000
     */
    private Integer state = 3000;

    /**
     * 便捷的错误码(字符串编码)
     * Convenient error code (string code)
     * 默认值为"fail"
     * Default value is "fail"
     */
    private String stateCode = "fail";

    /**
     * 便捷的错误信息
     * Convenient error message
     */
    private String message;

    /**
     * 语言编码
     * Language code
     */
    private String languageCode;

    public BaseException() {
    }

    /**
     * 便捷的创建BaseException对象的方法
     * Convenient method to create BaseException object
     *
     *  @param stateCode 状态码，此处为用来标识错误的字符串编码
     *                   State code, used to identify the error
     *
     *  @param message 错误信息，转换成RestResult获取时，使用“errMsg”字段
     *                 Error message, when converted to RestResult, use the "errMsg" field
     */
    public static BaseException of(String stateCode, String message) {
        return new BaseException().setStateCode(stateCode).setMessage(message);
    }

    /**
     * 便捷的创建BaseException对象的方法
     * Convenient method to create BaseException object
     *
     *  @param stateCode 状态码，此处为用来标识错误的字符串编码
     *                   State code, used to identify the error
     *
     *  @param message 错误信息，转换成RestResult获取时，使用“errMsg”字段
     *                 Error message, when converted to RestResult, use the "errMsg" field
     *
     *  @param languageCode 语言编码
     *                      Language code
     */
    public static BaseException of(String stateCode, String message, String languageCode) {
        return new BaseException()
                .setStateCode(stateCode)
                .setMessage(message)
                .setLanguageCode(languageCode);
    }


    /**
     * 配置全局的异常处理
     * 当有BaseException异常抛出时，会自动调用这个方法，并给客户端返回一个RestResult<String>对象
     * Configure global exception handling
     * When a BaseException exception is thrown, this method will be called automatically,
     * and a RestResult<String> object will be returned to the client
     *
     * @param e 异常
     *          Exception
     *
     * @return RestResult 返回结果
     *                  Return result
     */
    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public RestResult<String> exceptionHandler(BaseException e){
        return new RestResult<String>().setState(e.getState()).setErrMsg(e.getMessage()).setStateCode(e.getStateCode())
                .setMsgDictCode(e.getStateCode());
    }

    /**
     * 获取一个结果
     * Gets a result.
     *
     * @return a result
     *         一个结果
     */
    @Override
    public BaseException get() {
        return this;
    }

    public RestResult<?> toRestResult() {
        return new RestResult<>().setState(state).setErrMsg(message).setStateCode(stateCode)
                .setMsgDictCode(stateCode);
    }

}
