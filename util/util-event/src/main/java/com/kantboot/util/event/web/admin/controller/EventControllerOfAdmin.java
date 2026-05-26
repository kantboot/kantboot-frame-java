package com.kantboot.util.event.web.admin.controller;

import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.event.service.IEventService;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/util-event-web/admin/event")
@AuthInit(name = "事件管理", description = "事件管理", sourceLanguageCode = "zh_CN")
public class EventControllerOfAdmin {

    @Resource
    private IEventService eventService;

    @RequestMapping("/getAll")
    @AuthInit(name = "获取所有事件", description = "获取所有事件", sourceLanguageCode = "zh_CN")
    public RestResult<?> getAll() {
        return RestResult.success(eventService.getAll(), CommonSuccessStateConsts.GET_SUCCESS);
    }

}
