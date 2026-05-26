package com.kantboot.functional.icon.web.controller;

import com.kantboot.functional.icon.service.IFunctionalIconService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.jpa.param.PageParam;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/functional-icon-web/icon")
@AuthInit(
        name = "功能图标",
        description = "功能图标",
        sourceLanguageCode = "zh_CN"
)
public class FunctionalIconController {

    @Resource
    private IFunctionalIconService service;

    @Resource
    private HttpServletResponse response;

    @RequestMapping("/getAll")
    @AuthInit(
            name = "获取所有功能图标",
            description = "获取所有功能图标",
            sourceLanguageCode = "zh_CN",
            allPass = true
    )
    public RestResult<?> getAll() {
        return RestResult.success(service.getAll(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/getByCode")
    @AuthInit(
            name = "根据代码获取功能图标",
            description = "根据代码获取功能图标",
            sourceLanguageCode = "zh_CN",
            noNeedLogin = true
    )
    public RestResult<?> getByCode(@RequestParam("code") String code) {
        response.setHeader("Cache-Control", "public, max-age=31536000");
        return RestResult.success(service.getByCode(code), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @RequestMapping("/visitByCode")
    @AuthInit(
            name = "访问功能图标",
            description = "访问功能图标",
            sourceLanguageCode = "zh_CN",
            noNeedLogin = true
    )
    public void visitByCode(@RequestParam("code") String code,@RequestParam("color") String color,
                            @RequestParam(name = "strokeWidth",defaultValue = "0.5") String strokeWidth) {
        service.visitByCode(code,color,strokeWidth);
    }

    @RequestMapping("/getBodyData")
    @AuthInit(
            sourceLanguageCode = "zh_CN",
            noNeedLogin = true
    )
    public RestResult<?> getBodyData(@RequestBody PageParam<Map<String,Object>> pageParam) {
        return RestResult.success(service.getBodyData(pageParam), CommonSuccessStateConsts.GET_SUCCESS);
    }

}
