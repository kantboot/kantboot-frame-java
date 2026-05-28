package com.kantboot.thirdparty.discord.domain.entity;

import com.kantboot.util.base.control.domian.entity.BaseEntity;
import com.kantboot.util.i18n.annotation.I18nTopKey;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "thirdparty_discord_user_info")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@I18nTopKey
public class ThirdpartyDiscordUserInfo
        extends BaseEntity
        implements Serializable {

    private Long userAccountId;

    @Column(unique = true)
    private String discordId;

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
