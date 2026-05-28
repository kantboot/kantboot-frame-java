package com.kantboot.thirdparty.alicloud.market.web.admin.controller;

import com.kantboot.thirdparty.alicloud.market.domain.dto.ThirdpartyAlicloudMarketExecuteDTO;
import com.kantboot.thirdparty.alicloud.market.domain.entity.ThirdpartyAlicloudMarketRequest;
import com.kantboot.thirdparty.alicloud.market.service.IThirdpartyAlicloudmarketRequestService;
import com.kantboot.thirdparty.alicloud.market.setting.ThirdpartyAlicloudMarketSetting;
import com.kantboot.util.base.control.controller.BaseAdminController;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/thirdparty-alicloud-market-web/admin/request")
public class ThirdpartyAlicloudMarketRequestControllerOfAdmin
        extends BaseAdminController<ThirdpartyAlicloudMarketRequest,Long> {

    @Resource
    private IThirdpartyAlicloudmarketRequestService service;

    @RequestMapping("/execute")
    public RestResult<?> execute(@RequestBody ThirdpartyAlicloudMarketExecuteDTO executeDTO) {
        String code = executeDTO.getCode();
        Map<String, String> queries = executeDTO.getQueries();
        Map<String, String> bodies = executeDTO.getBodies();
        return RestResult.success(service.execute(code, queries, bodies), CommonSuccessStateConsts.EXECUTE_SUCCESS);
    }


    /**
     * getSetting
     */
    @RequestMapping("/getSetting")
    public RestResult<?> getSetting() {
        return RestResult.success(service.getSetting(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * setSetting
     */
    @RequestMapping("/setSetting")
    public RestResult<?> setSetting(
            @RequestBody ThirdpartyAlicloudMarketSetting setting) {
        service.setSetting(setting);
        return RestResult.success(null, CommonSuccessStateConsts.SET_SUCCESS);
    }

}
