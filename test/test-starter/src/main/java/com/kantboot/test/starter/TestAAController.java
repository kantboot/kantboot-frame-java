package com.kantboot.test.starter;

import com.kantboot.test.starter.domain.entity.TestAA;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import com.kantboot.util.event.emit.EventEmit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "测试事件")
@RestController
@RequestMapping("/aa")
public class TestAAController {

    @Resource
    private EventEmit emit;

    @AuthInit(name = "测试事件",description = "测试事件",sourceLanguageCode = "zh_CN",noNeedLogin = true)
    @RequestMapping("/aa")
    public RestResult<?> aa(){
        emit.to("testEvent","hello");
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

}
