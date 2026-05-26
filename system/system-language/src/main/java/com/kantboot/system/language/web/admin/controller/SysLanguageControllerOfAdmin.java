package com.kantboot.system.language.web.admin.controller;

import com.kantboot.system.language.domain.entity.SysLanguage;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "语言管理",description = "语言管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/system-language-web/admin/language")
public class SysLanguageControllerOfAdmin extends BaseAdminController<SysLanguage,Long> {
}
