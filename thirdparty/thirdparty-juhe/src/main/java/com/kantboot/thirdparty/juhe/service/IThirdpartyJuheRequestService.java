package com.kantboot.thirdparty.juhe.service;

import com.kantboot.thirdparty.juhe.domain.entity.ThirdpartyJuheRequest;

import java.util.Map;

public interface IThirdpartyJuheRequestService {

    /**
     * 根据请求code获取实体
     */
    ThirdpartyJuheRequest getByCode(String code);


    /**
     * 执行请求
     */
    Object execute(String code, Map<String, Object> params);

}
