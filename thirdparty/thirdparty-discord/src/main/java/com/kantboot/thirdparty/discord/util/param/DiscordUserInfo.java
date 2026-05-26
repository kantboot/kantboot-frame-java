package com.kantboot.thirdparty.discord.util.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class DiscordUserInfo
    implements Serializable {

    private String id;
    private String username;
    private String avatar;
    private String discriminator;
    private Integer publicFlags;
    private Integer flags;
    private String banner;
    private String accentColor;
    private String globalName;
    private String collectibles;
    private String bannerColor;
    private String clan;
    private String primaryGuild;
    private Boolean mfaEnabled;
    private String locale;
    private Integer premiumType;
    private String email;
    private Boolean verified;


}
