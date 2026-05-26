package com.kantboot.user.account.domain.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户属性扩展
 */
@Getter
@Setter
@MappedSuperclass
public class UserAccountAttrExt implements Serializable {
}
