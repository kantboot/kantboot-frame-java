package com.kantboot.functional.email.web.admin.controller;

import com.kantboot.functional.email.domain.entity.FunctionalEmail;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邮件功能的控制器
 */
@AuthInit(name = "邮件管理",description = "邮件管理",sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/functional-email-web/admin/email")
public class FunctionalEmailControllerOfAdmin extends BaseAdminController<FunctionalEmail,Long> {
}
