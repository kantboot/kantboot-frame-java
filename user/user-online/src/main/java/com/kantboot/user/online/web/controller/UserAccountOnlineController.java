package com.kantboot.user.online.web.controller;

import com.kantboot.user.online.service.IUserAccountOnlineService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "用户在线", description = "用户在线", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/user-online-web/userAccountOnline")
public class UserAccountOnlineController {

    @Resource
    private IUserAccountOnlineService service;

    /**
     * 心跳
     */
    @AuthInit(name = "心跳", description = "心跳", sourceLanguageCode = "zh_CN", allPass = true)
    @RequestMapping("/heartbeatSelf")
    public RestResult<Void> heartbeatSelf() {
        service.heartbeatSelf();
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    /**
     * 隐身
     * @param isInvisible 是否隐身
     * @return RestResult<Void>
     */
    @AuthInit(name = "隐身", description = "隐身", sourceLanguageCode = "zh_CN", allPass = true)
    @RequestMapping("/invisibleSelf")
    public RestResult<Void> invisibleSelf(@RequestParam Boolean isInvisible) {
        service.invisibleSelf(isInvisible);
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    /**
     * 上线
     */
    @AuthInit(name = "上线", description = "上线", sourceLanguageCode = "zh_CN", allPass = true)
    @RequestMapping("/onlineSelf")
    public RestResult<Void> onlineSelf() {
        service.onlineSelf();
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    /**
     * 离线
     */
    @AuthInit(name = "离线", description = "离线", sourceLanguageCode = "zh_CN", allPass = true)
    @RequestMapping("/offlineSelf")
    public RestResult<Void> offlineSelf() {
        service.offlineSelf();
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

}
