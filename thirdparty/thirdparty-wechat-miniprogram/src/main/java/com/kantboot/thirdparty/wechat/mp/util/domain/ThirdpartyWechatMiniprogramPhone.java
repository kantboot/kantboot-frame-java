package com.kantboot.thirdparty.wechat.mp.util.domain;

import lombok.Data;

@Data
public class ThirdpartyWechatMiniprogramPhone {

    private String unionId;

    private String openId;

    private String phoneNumber;

    private String purePhoneNumber;

    private String countryCode;

    private Watermark watermark;

    @Data
    public static class Watermark {
        private Long timestamp;
        private String appid;
    }

}
