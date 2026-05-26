package com.kantboot.thirdparty.wechat.mp.service.impl;

import com.kantboot.thirdparty.wechat.mp.dao.repository.ThirdpartyWechatMiniprogramRequestRepository;
import com.kantboot.thirdparty.wechat.mp.domain.entity.ThirdpartyWechatMiniprogramRequest;
import com.kantboot.thirdparty.wechat.mp.domain.entity.ThirdpartyWechatMiniprogramRequestBody;
import com.kantboot.thirdparty.wechat.mp.domain.entity.ThirdpartyWechatMiniprogramRequestQuery;
import com.kantboot.thirdparty.wechat.mp.service.IThirdpartyWechatMiniprogramRequestService;
import com.kantboot.thirdparty.wechat.mp.service.IThirdpartyWechatMiniprogramService;
import com.kantboot.thirdparty.wechat.mp.setting.ThirdpartyWechatMiniprogramSetting;
import com.kantboot.thirdparty.wechat.util.ThirdpartyWechatRequestUtil;
import com.kantboot.thirdparty.wechat.util.domain.ThirdpartyWechatExecute;
import com.kantboot.util.rest.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ThirdpartyWechatMiniprogramRequestServiceImpl
    implements IThirdpartyWechatMiniprogramRequestService {

    @Resource
    private ThirdpartyWechatMiniprogramRequestRepository repository;

    @Resource
    private ThirdpartyWechatMiniprogramSetting setting;

    @Resource
    private IThirdpartyWechatMiniprogramService service;

    @Override
    public ThirdpartyWechatMiniprogramRequest getByCode(String code) {
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
        ThirdpartyWechatMiniprogramRequest byCode = getByCode(code);
        if (byCode == null) {
            return null;
        }
        String host = byCode.getHost();
        String path = byCode.getPath();
        String method = byCode.getRequestMethod();
        String appCode = setting.getAppId();
        List<ThirdpartyWechatMiniprogramRequestBody> bodyList = byCode.getBodies();
        List<ThirdpartyWechatMiniprogramRequestQuery> queryList = byCode.getQueries();
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
            for (ThirdpartyWechatMiniprogramRequestBody body : bodyList) {
                String field = body.getField();
                if ((body.getRequired()!=null&&body.getRequired()) && bodies!=null && !bodies.containsKey(field)) {
                    throw BaseException.of("wechatMiniprogram.request.missingBodyParameter:" + field,
                            "请求参数缺少必填的body参数:" + field, "zh_CN");
                }
            }
        }
        // 检测是否有query参数是否有必填的字段未填
        if (queryList != null) {
            for (ThirdpartyWechatMiniprogramRequestQuery query : queryList) {
                String field = query.getField();
                if ((query.getRequired()!=null&&query.getRequired()) && !queries.containsKey(field)) {
                    throw BaseException.of("wechatMiniprogram.request.missingQueryParameter:" + field,
                            "请求参数缺少必填的query参数:" + field, "zh_CN");
                }
            }
        }

        String accessToken = service.getAccessToken();

        return ThirdpartyWechatRequestUtil.execute(
                new ThirdpartyWechatExecute()
                        .setPath(path)
                        .setAccessToken(accessToken)
                        .setMethod(method)
                        .setBodies(bodies)
                        .setQueries(queries)
        );
    }

    @Override
    public Object execute(String code, Map<String, String> params) {
        ThirdpartyWechatMiniprogramRequest byCode = getByCode(code);
        if (byCode == null) {
            return null;
        }
        List<ThirdpartyWechatMiniprogramRequestQuery> queryList = byCode.getQueries();
        List<ThirdpartyWechatMiniprogramRequestBody> bodyList = byCode.getBodies();
        if("post".equals(byCode.getRequestMethod())){
            return execute(code, null, params);
        }
        if("get".equals(byCode.getRequestMethod())){
            return execute(code, params, null);
        }
        return null;
    }


}
