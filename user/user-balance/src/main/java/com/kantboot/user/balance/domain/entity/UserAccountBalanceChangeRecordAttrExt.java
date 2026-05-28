package com.kantboot.user.balance.domain.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@MappedSuperclass
public class UserAccountBalanceChangeRecordAttrExt
    implements Serializable {

}
