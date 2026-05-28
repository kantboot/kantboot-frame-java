package com.kantboot.system.dict.web.admin.controller;

import com.kantboot.system.dict.domain.entity.SysDictGroup;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "字典分组管理",description = "字典分组管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/system-dict-web/admin/dictGroup")
public class SysDictGroupControllerOfAdmin
        extends BaseAdminController<SysDictGroup,Long> {
}
