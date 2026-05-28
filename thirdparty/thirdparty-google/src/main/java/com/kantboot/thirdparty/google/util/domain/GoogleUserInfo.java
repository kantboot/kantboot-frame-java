package com.kantboot.thirdparty.google.util.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoogleUserInfo implements Serializable {

    private String id;

    private String email;

    private Boolean verifiedEmail;

    private String name;

    private String givenName;

    private String picture;

}
