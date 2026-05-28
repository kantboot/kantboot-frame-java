package com.kantboot.functional.icon.web.controller;

import com.kantboot.functional.icon.service.IFunctionalIconGroupService;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/functional-icon-web/iconGroup")
public class FunctionalIconGroupController {

    @Resource
    private IFunctionalIconGroupService service;

    @RequestMapping("/getAll")
    public RestResult<?> getAll() {
        return RestResult.success(service.getAll(), CommonSuccessStateConsts.GET_SUCCESS);
    }

}
