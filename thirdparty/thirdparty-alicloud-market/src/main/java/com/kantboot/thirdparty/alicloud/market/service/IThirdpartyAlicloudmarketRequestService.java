package com.kantboot.thirdparty.alicloud.market.service;

import com.kantboot.thirdparty.alicloud.market.domain.entity.ThirdpartyAlicloudMarketRequest;
import com.kantboot.thirdparty.alicloud.market.setting.ThirdpartyAlicloudMarketSetting;

import java.util.Map;

public interface IThirdpartyAlicloudmarketRequestService {

    /**
     * 根据请求code获取实体
     */
    ThirdpartyAlicloudMarketRequest getByCode(String code);

    /**
     * 执行请求
     */
    Object execute(String code, Map<String, String> queries, Map<String, String> bodies);

    /**
     * 执行请求
     */
    Object execute(String code, Map<String, String> params);


    ThirdpartyAlicloudMarketSetting getSetting();

    void setSetting(ThirdpartyAlicloudMarketSetting setting);

}
