package com.kantboot.thirdparty.wechat.mp.service;

import com.kantboot.thirdparty.wechat.mp.domain.entity.ThirdpartyWechatMiniprogramRequest;

import java.util.Map;

public interface IThirdpartyWechatMiniprogramRequestService {

    /**
     * 根据请求code获取实体
     */
    ThirdpartyWechatMiniprogramRequest getByCode(String code);

    /**
     * 执行请求
     */
    Object execute(String code, Map<String, String> queries, Map<String, String> bodies);

    /**
     * 执行请求
     */
    Object execute(String code, Map<String, String> params);


}