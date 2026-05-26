package com.kantboot.user.account.domain.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户模块的扩展属性（在user模块内部使用）
 * @author 方某方
 */
@Getter
@Setter
@MappedSuperclass
public class UserAccountAttrExtInUserModule
    extends UserAccountAttrExt
    implements Serializable {
}
