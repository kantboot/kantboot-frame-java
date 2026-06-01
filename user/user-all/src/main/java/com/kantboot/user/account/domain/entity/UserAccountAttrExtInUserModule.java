package com.kantboot.user.account.domain.entity;

import com.kantboot.user.interrelation.domain.entity.UserAccountInterrelation;
import com.kantboot.user.location.domain.entity.UserAccountLocation;
import com.kantboot.user.online.domain.entity.UserAccountOnline;
import com.kantboot.user.online.domain.entity.UserAccountOnlineShow;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class UserAccountAttrExtInUserModule
    extends UserAccountAttrExt {

}
