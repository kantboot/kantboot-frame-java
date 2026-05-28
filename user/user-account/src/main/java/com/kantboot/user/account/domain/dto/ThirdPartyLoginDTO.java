package com.kantboot.user.account.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ThirdPartyLoginDTO implements Serializable {

    private String thirdPartyCode;

    private String key;

    private String value;

}
