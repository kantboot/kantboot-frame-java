package com.kantboot.thirdparty.alicloud.market.domain.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ThirdpartyAlicloudMarketExecuteDTO {

    private String code;

    private Map<String,String> queries;

    private Map<String,String> bodies;
}
