package com.kantboot.thirdparty.wechat.util.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ThirdpartyWechatToken implements Serializable {

    private String accessToken;

    private Integer expiresIn;

}
