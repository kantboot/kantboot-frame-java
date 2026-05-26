package com.kantboot.thirdparty.discord.util.param;

import lombok.Data;

@Data
public class DiscordAccessToken {

    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private String refreshToken;
    private String scope;

}
