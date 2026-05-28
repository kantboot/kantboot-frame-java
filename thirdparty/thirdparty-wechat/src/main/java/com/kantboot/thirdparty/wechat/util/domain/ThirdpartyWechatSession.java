package com.kantboot.thirdparty.wechat.util.domain;

import lombok.Data;

@Data
public class ThirdpartyWechatSession {

    /**
     * 会话密钥
     */
    private String sessionKey;

    /**
     * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 UnionID 机制说明。
     */
    private String unionId;

    /**
     * 用户唯一标识
     */
    private String openId;

}
