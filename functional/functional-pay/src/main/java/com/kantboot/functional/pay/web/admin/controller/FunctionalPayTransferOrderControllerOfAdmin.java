package com.kantboot.functional.pay.web.admin.controller;

import com.kantboot.functional.pay.domain.entity.FunctionalPayTransferOrder;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "转账订单管理",description = "转账订单管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/functional-pay-web/admin/transferOrder")
public class FunctionalPayTransferOrderControllerOfAdmin
    extends BaseAdminController<FunctionalPayTransferOrder,Long> {
}
