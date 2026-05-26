package com.kantboot.user.balance.domain.dto;

import com.kantboot.user.balance.domain.entity.UserAccountBalanceChangeRecordAttrExt;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
public class ChangeHandleDTO
    extends UserAccountBalanceChangeRecordAttrExt
{

    /**
     * 事由编码
     */
    private String reasonCode;

    /**
     * 余额类型编码
     */
    private String balanceCode;

    /**
     * 用户账号id
     */
    private Long userAccountId;

    /**
     * 数量
     */
    private BigDecimal number;

    /**
     * 入账场景编码
     */
    private String rechargeSceneCode;

    /**
     * 消费场景编码
     */
    private String consumeSceneCode;

    /**
     * 描述
     */
    private String description;

}
