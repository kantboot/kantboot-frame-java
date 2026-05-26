package com.kantboot.functional.chat.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DialogSearchDTO implements Serializable {

    /**
     * 用户ID
     */
    private Long userAccountId;

    /**
     * 最大ID
     */
    private Long maxId;

    /**
     * 最小ID
     */
    private Long minId;

}
