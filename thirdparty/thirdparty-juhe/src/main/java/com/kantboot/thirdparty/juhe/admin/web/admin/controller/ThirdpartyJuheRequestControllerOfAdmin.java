package com.kantboot.thirdparty.juhe.admin.web.admin.controller;

import com.kantboot.thirdparty.juhe.domain.entity.ThirdpartyJuheRequest;
import com.kantboot.thirdparty.juhe.service.IThirdpartyJuheRequestService;
import com.kantboot.util.base.control.controller.BaseAdminController;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/thirdparty-juhe-web/admin/request")
public class ThirdpartyJuheRequestControllerOfAdmin
        extends BaseAdminController<ThirdpartyJuheRequest,Long> {

    @Resource
    private IThirdpartyJuheRequestService service;

    @RequestMapping("/execute")
    public RestResult<?> execute(
            @RequestParam("code") String code,
            @RequestParam("params") Map<String,Object> params) {
        return RestResult.success(service.execute(code,params), CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

}
