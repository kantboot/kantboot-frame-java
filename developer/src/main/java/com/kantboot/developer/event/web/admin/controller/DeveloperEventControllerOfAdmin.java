package com.kantboot.developer.event.web.admin.controller;

import com.kantboot.developer.event.domain.entity.DeveloperEvent;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/developer-event-web/admin/event")
@AuthInit(name = "事件管理")
public class DeveloperEventControllerOfAdmin
        extends BaseAdminController<DeveloperEvent,Long> {
}
