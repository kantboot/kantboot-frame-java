package com.kantboot.global.web.admin.controller;

import com.kantboot.global.domain.entity.GlobalWebControllerLog;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "全局网站日志管理")
@RestController
@RequestMapping("/global-web/admin/webControllerLog")
public class GlobalWebControllerLogControllerOfAdmin
    extends BaseAdminController<GlobalWebControllerLog,Long> {
}
