package com.kantboot.thirdparty.alicloud.market.service.impl;

import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.thirdparty.alicloud.market.domain.entity.ThirdpartyAlicloudMarketRequest;
import com.kantboot.thirdparty.alicloud.market.domain.entity.ThirdpartyAlicloudMarketRequestBody;
import com.kantboot.thirdparty.alicloud.market.domain.entity.ThirdpartyAlicloudMarketRequestQuery;
import com.kantboot.thirdparty.alicloud.market.param.ThirdpartyAlicloudMarketParam;
import com.kantboot.thirdparty.alicloud.market.dao.repository.ThirdpartyAlicloudMarketRequestRepository;
import com.kantboot.thirdparty.alicloud.market.service.IThirdpartyAlicloudmarketRequestService;
import com.kantboot.thirdparty.alicloud.market.setting.ThirdpartyAlicloudMarketSetting;
import com.kantboot.thirdparty.alicloud.market.util.CallUtil;
import com.kantboot.util.rest.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ThirdpartyAlicloudMarketRequestServiceImpl
        implements IThirdpartyAlicloudmarketRequestService {

    @Resource
    private ThirdpartyAlicloudMarketRequestRepository repository;

    @Resource
    private ThirdpartyAlicloudMarketSetting setting;

    @Resource
    private ISysSettingService settingService;


    @Override
    public ThirdpartyAlicloudMarketRequest getByCode(String code) {
        return repository.findByCode(code);
    }

    @Override
    public Object execute(String code, Map<String, String> queries, Map<String, String> bodies) {
        if(queries!=null){
            queries = new HashMap<>(queries);
        }
        if(bodies!=null){
            bodies = new HashMap<>(bodies);
        }
        ThirdpartyAlicloudMarketRequest byCode = getByCode(code);
        if (byCode == null) {
            return null;
        }
        String host = byCode.getHost();
        String path = byCode.getPath();
        String method = byCode.getRequestMethod();
        String appCode = setting.getAppCode();
        List<ThirdpartyAlicloudMarketRequestBody> bodyList = byCode.getBodies();
        List<ThirdpartyAlicloudMarketRequestQuery> queryList = byCode.getQueries();
        // 检测host是否为空
        if (host == null || host.isBlank()) {
            throw BaseException.of("alicloudmarket.request.hostEmpty",
                    "请求参数host不能为空", "zh_CN");
        }
        // 检测path是否为空
        if (path == null || path.isBlank()) {
            throw BaseException.of("alicloudmarket.request.pathEmpty",
                    "请求参数path不能为空", "zh_CN");
        }
        // 检测appCode是否设置
        if (appCode == null || appCode.isBlank()) {
            throw BaseException.of("alicloudmarket.request.appCodeNotSet",
                    "请求参数appCode未设置", "zh_CN");
        }

        // 检测是否有body参数是否有必填的字段未填
        if (bodyList != null) {
            for (ThirdpartyAlicloudMarketRequestBody body : bodyList) {
                String field = body.getField();
                if ((body.getRequired()!=null&&body.getRequired()) && bodies!=null && !bodies.containsKey(field)) {
                    throw BaseException.of("alicloudmarket.request.missingBodyParameter:" + field,
                            "请求参数缺少必填的body参数:" + field, "zh_CN");
                }
            }
        }
        // 检测是否有query参数是否有必填的字段未填
        if (queryList != null) {
            for (ThirdpartyAlicloudMarketRequestQuery query : queryList) {
                String field = query.getField();
                if ((query.getRequired()!=null&&query.getRequired()) && !queries.containsKey(field)) {
                    throw BaseException.of("alicloud.market.request.missingQueryParameter:" + field,
                            "请求参数缺少必填的query参数:" + field, "zh_CN");
                }
            }
        }


        return CallUtil.call(new ThirdpartyAlicloudMarketParam()
                .setHost(host)
                .setPath(path)
                .setAppCode(appCode)
                .setMethod(method)
                .setQueries(queries)
                .setBodies(bodies)
        );
    }

    @Override
    public Object execute(String code, Map<String, String> params) {
        ThirdpartyAlicloudMarketRequest byCode = getByCode(code);
        if (byCode == null) {
            return null;
        }
        List<ThirdpartyAlicloudMarketRequestQuery> queryList = byCode.getQueries();
        List<ThirdpartyAlicloudMarketRequestBody> bodyList = byCode.getBodies();
        if("post".equals(byCode.getRequestMethod())){
            return execute(code, null, params);
        }
        if("get".equals(byCode.getRequestMethod())){
            return execute(code, params, null);
        }
        return null;
    }

    @Override
    public ThirdpartyAlicloudMarketSetting getSetting() {
        return new ThirdpartyAlicloudMarketSetting()
                .setAppKey(settingService.getValue("thirdpartyAlicloudMarket.appKey"))
                .setAppSecret(settingService.getValue("thirdpartyAlicloudMarket.appSecret"))
                .setAppCode(settingService.getValue("thirdpartyAlicloudMarket.appCode"));
    }

    @Override
    public void setSetting(ThirdpartyAlicloudMarketSetting setting) {
        settingService.setValue("thirdpartyAlicloudMarket.appKey", setting.getAppKey());
        settingService.setValue("thirdpartyAlicloudMarket.appSecret", setting.getAppSecret());
        settingService.setValue("thirdpartyAlicloudMarket.appCode", setting.getAppCode());
    }
}
