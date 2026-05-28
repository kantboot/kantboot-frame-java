package com.kantboot.functional.pay.web.admin.controller;

import com.kantboot.functional.pay.domain.entity.FunctionalPayMethod;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "支付方式管理",description = "支付方式管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/functional-pay-web/admin/method")
public class FunctionalPayMethodControllerOfAdmin
    extends BaseAdminController<FunctionalPayMethod,Long> {
}
