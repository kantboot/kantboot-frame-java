package com.kantboot.thirdparty.juhe.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.kantboot.thirdparty.juhe.dao.repository.ThirdpartyJuheRequestRepository;
import com.kantboot.thirdparty.juhe.domain.entity.ThirdpartyJuheRequest;
import com.kantboot.thirdparty.juhe.domain.entity.ThirdpartyJuheRequestParam;
import com.kantboot.thirdparty.juhe.service.IThirdpartyJuheRequestService;
import com.kantboot.util.http.HttpSendUtil;
import com.kantboot.util.http.domain.config.HttpSendConfig;
import com.kantboot.util.rest.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ThirdpartyJuheRequestServiceImpl
    implements IThirdpartyJuheRequestService {

    @Resource
    private ThirdpartyJuheRequestRepository repository;

    @Override
    public ThirdpartyJuheRequest getByCode(String code) {
        return repository.findByCode(code);
    }

    @Override
    public Object execute(String code, Map<String, Object> params) {
        if(params==null){
            params = new HashMap<>();
        }
        params = new HashMap<>(params);

        ThirdpartyJuheRequest byCode = repository.findByCode(code);
        List<ThirdpartyJuheRequestParam> requestParams = byCode.getParams();
        // 查看必填的数据是否齐全
        for (ThirdpartyJuheRequestParam requestParam : requestParams) {
            // 如果有默认值，则添加到params中
            if (requestParam.getDefaultValue() != null && !params.containsKey(requestParam.getField())) {
                params.put(requestParam.getField(), requestParam.getDefaultValue());
            }
            if (((requestParam.getRequired()!=null&&requestParam.getRequired())) && !params.containsKey(requestParam.getField())) {
                throw BaseException.of("thirdparty.juhe.request.param.required:"+requestParam.getRequestCode(),
                        "请求参数缺失：" + requestParam.getField(), "zh_CN");
            }
        }


        String url = byCode.getUrl();

        // 如果不存在key，则添加key
        if (!params.containsKey("key")) {
            params.put("key", byCode.getKey());
        }

        String send = HttpSendUtil.send(new HttpSendConfig()
                .setUrl(url)
                .setMethod(byCode.getRequestMethod())
                .setContentType(byCode.getContentType())
                .setBody(params)
        );
        JSONObject jsonObject = JSONObject.parseObject(send);

        // 错误示例： {\"reason\":\"不存在\\/不支持的货币种类\",\"result\":null,\"error_code\":208005}"
        // 成功示例：{"reason":"查询成功!","result":[{"currencyF":"USD","currencyF_Name":"美元","currencyT":"CNY","currencyT_Name":"人民币","currencyFD":"1","exchange":"7.18210000","result":"7.18210000","updateTime":"2025-08-17 02:23:36"},{"currencyF":"CNY","currencyF_Name":"人民币","currencyT":"USD","currencyT_Name":"美元","currencyFD":"1","exchange":"0.13923500","result":"0.13923500","updateTime":"2025-08-17 02:23:36"}],"error_code":0}
        // 如果error_code不为0，则抛出异常
        if (jsonObject.getIntValue("error_code") != 0) {
            String reason = jsonObject.getString("reason");
            if (reason == null) {
                reason = "未知错误";
            }
            throw BaseException.of("thirdparty.juhe.request.error:" + jsonObject.get("error_code"),
                    reason, "zh_CN");
        }

        return jsonObject.get("result");
    }
}
