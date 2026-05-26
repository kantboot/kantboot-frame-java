package com.kantboot.functional.chat.web.admin.controller;

import com.kantboot.functional.chat.domain.entity.FunctionalChatDialogMessage;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "对话", description = "对话", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/functional-chat-web/admin/dialogMessage")
public class FunctionalChatDialogMessageControllerOfAdmin
    extends BaseAdminController<FunctionalChatDialogMessage,Long> {
}
