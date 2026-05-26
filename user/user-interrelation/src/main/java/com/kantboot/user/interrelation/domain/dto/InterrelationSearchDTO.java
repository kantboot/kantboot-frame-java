package com.kantboot.user.interrelation.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class InterrelationSearchDTO implements Serializable {

    /**
     * 最大ID
     */
    private Long maxId;

    /**
     * 最小ID
     */
    private Long minId;


    /**
     * 用户ID
     */
    private Long userAccountId;

    /**
     * 关键词
     */
    private String keyword;

}
