package com.kantboot.developer.timer.web.admin.controller;

import com.kantboot.developer.timer.domain.entity.DeveloperTimer;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/developer-timer-web/admin/timer")
@AuthInit(name = "定时器管理")
public class DeveloperTimerControllerOfAdmin
        extends BaseAdminController<DeveloperTimer,Long> {
}
