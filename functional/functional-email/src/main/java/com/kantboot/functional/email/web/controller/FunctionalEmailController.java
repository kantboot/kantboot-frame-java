package com.kantboot.functional.email.web.controller;

import com.kantboot.functional.email.dto.EmailMessageDTO;
import com.kantboot.functional.email.service.IFunctionalEmailService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "邮件",description = "邮件")
@RestController
@RequestMapping("/functional-email-web/email")
public class FunctionalEmailController {

    @Resource
    private IFunctionalEmailService service;

    @AuthInit(name = "发送邮件",description = "发送邮件")
    @RequestMapping("/send")
    public RestResult<?> send(@RequestBody EmailMessageDTO entity){
        service.send(entity);
        return RestResult.success(null, CommonSuccessStateConsts.SEND_SUCCESS);
    }

}
