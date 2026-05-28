package com.kantboot.util.rest.result;

import com.kantboot.util.rest.result.entity.StateEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 这是一个方便用户在rest操作的工具类
 * This is a utility class for users to operate in rest
 *
 * @param <T> 泛型，用于返回的主体数据
 *           Generic, used for the main body data returned
 * @author 方某方
 */
@Data
@Accessors(chain = true)
public class RestResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息字典编码
     * Message dictionary code
     */
    private String msgDictCode;

    /**
     * 操作编码
     * Operation code
     */
    private String operationCode;

    /**
     * 便捷错误码(数字编码)
     * Convenient error code (digital code)
     */
    public static final Integer FAIL_STATE = 3000;

    /**
     * 便捷错误码(字符串编码)
     * Convenient error code (string code)
     */
    public static final String FAIL_STATE_CODE = "FAIL";

    /**
     * 便捷成功码(数字编码)
     * Convenient success code (digital code)
     */
    public static final Integer SUCCESS_STATE = 2000;

    /**
     * 便捷成功码(字符串编码)
     * Convenient success code (string code)
     */
    public static final String SUCCESS_STATE_CODE = "SUCCESS";


    /**
     * 返回的状态(数字编码)
     * Return status (digital code)
     */
    private Integer state;

    /**
     * 返回的消息编码(字符串编码)
     * Message code returned
     */
    private String stateCode;

    /**
     * 语言编码
     * Language code
     */
    private String languageCode = "en";

    /**
     * 返回的消息，在请求判定为成功的时候，使用这个字段，失败的时候使用errMsg字段
     * The message returned, use this field when the request is determined to be successful,
     * and use the errMsg field when it fails
     */
    private String msg;

    /**
     * 返回的错误消息，在请求判定为失败的时候，使用这个字段，成功的时候使用msg字段
     * The error message returned, use this field when the request is determined to fail,
     */
    private String errMsg;

    /**
     * 返回的主体数据(泛型)
     * Main body data returned (generic)
     */
    private T data;

    /**
     * 错误编码
     * Error code
     */
    private String errorCode;

    /**
     * 是否成功
     * Whether it is successful
     */
    private Boolean isSuccess;

    /**
     * 便捷的成功返回
     * Convenient success return
     *
     * @param data 返回的数据
     *             Data returned
     * @param code 消息编码，也可用作消息的字典编码（方便于国际化）
     *             Message code, can also be used as the dictionary code of the message (convenient for internationalization)
     * @return 返回结果
     *        Return result
     */
    public static <T> RestResult<T> success(T data, String code) {
        return new RestResult<T>()
                .setState(SUCCESS_STATE)
                .setData(data)
                .setMsgDictCode(code)
                .setIsSuccess(true)
                .setStateCode(SUCCESS_STATE_CODE);
    }


    public static <T> RestResult<T> success(T data, String code, String msg) {
        return new RestResult<T>()
                .setState(SUCCESS_STATE)
                .setData(data)
                .setMsg(msg)
                .setMsgDictCode(code)
                .setIsSuccess(true)
                .setStateCode(SUCCESS_STATE_CODE);
    }

    public static <T> RestResult<T> success(T data, String code, String msg, String languageCode) {
        return new RestResult<T>()
                .setState(SUCCESS_STATE)
                .setData(data)
                .setMsg(msg)
                .setMsgDictCode(code)
                .setIsSuccess(true)
                .setStateCode(SUCCESS_STATE_CODE)
                .setLanguageCode(languageCode);
    }

    public static <T> RestResult<T> success(T data, StateEntity stateCodeAndMsg) {
        return new RestResult<T>()
                .setState(SUCCESS_STATE)
                .setData(data)
                .setMsg(stateCodeAndMsg.getMsg())
                .setMsgDictCode(stateCodeAndMsg.getStateCode())
                .setIsSuccess(true)
                .setStateCode(SUCCESS_STATE_CODE)
                .setLanguageCode(stateCodeAndMsg.getLanguageCode());
    }

    /**
     * 便捷的错误返回
     *
     * @param msg 错误信息
     * @return 返回结果
     */
    public static RestResult<?> error(String msg) {
        return new RestResult<>()
                .setState(FAIL_STATE)
                .setErrMsg(msg)
                .setIsSuccess(false)
                .setMsgDictCode(FAIL_STATE_CODE)
                .setStateCode(FAIL_STATE_CODE);
    }

    /**
     * 通用的错误返回
     * Common error return
     *
     * @param msg       错误信息
     *                  Error message
     * @param stateCode 错误状态编码
     *                  Error status code
     * @return 返回结果
     *        Return result
     */
    public static RestResult<?> error(String stateCode,String msg) {
        return new RestResult<>()
                .setErrMsg(msg)
                .setIsSuccess(false)
                .setMsgDictCode(stateCode)
                .setStateCode(stateCode);
    }

    public static RestResult<?> error(String stateCode,
                                      String msg,
                                      String languageCode) {
        return new RestResult<>()
                .setState(FAIL_STATE)
                .setErrMsg(msg)
                .setIsSuccess(false)
                .setMsgDictCode(FAIL_STATE_CODE)
                .setStateCode(FAIL_STATE_CODE)
                .setLanguageCode(languageCode);
    }

    public static RestResult<?> error(StateEntity stateCodeAndMsg) {
        return new RestResult<>()
                .setState(FAIL_STATE)
                .setErrMsg(stateCodeAndMsg.getMsg())
                .setIsSuccess(false)
                .setMsgDictCode(stateCodeAndMsg.getStateCode())
                .setStateCode(stateCodeAndMsg.getStateCode())
                .setLanguageCode(stateCodeAndMsg.getLanguageCode());
    }



    /**
     * 检测是否成功
     */
    public boolean isSuccess() {
        return SUCCESS_STATE_CODE.equals(this.stateCode);
    }
}
