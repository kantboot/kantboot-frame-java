package com.kantboot.util.rest.result.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StateEntity {

    /**
     * 状态码
     * State code
     */
    private String stateCode;

    /**
     * 消息
     * Message
     */
    private String msg;

    /**
     * 语言编码
     * Language code
     */
    private String languageCode = "en";

    /**
     * 创建一个状态码和消息对象
     * Create a status code and message object
     *
     * @param stateCode 状态码
     *                  State code
     * @param msg      消息
     *                  Message
     * @return 状态码和消息对象
     *         Status code and message object
     */
    public static StateEntity of(String stateCode, String msg) {
        return new StateEntity()
                .setStateCode(stateCode)
                .setMsg(msg);
    }

    public static StateEntity of(String stateCode,
                                 String msg,
                                 String languageCode){
        return new StateEntity()
                .setStateCode(stateCode)
                .setMsg(msg)
                .setLanguageCode(languageCode);
    }


}
