package com.kantboot.thirdparty.discord.dao.repository;

import com.kantboot.thirdparty.discord.domain.entity.ThirdpartyDiscordUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThirdpartyDiscordUserInfoRepository
    extends JpaRepository<ThirdpartyDiscordUserInfo, Long> {

    ThirdpartyDiscordUserInfo findByDiscordId(String discordId);

}
