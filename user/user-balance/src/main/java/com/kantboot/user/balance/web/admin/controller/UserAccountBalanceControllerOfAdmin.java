package com.kantboot.user.balance.web.admin.controller;

import com.kantboot.user.balance.domain.dto.ChangeHandleDTO;
import com.kantboot.user.balance.domain.entity.UserAccountBalance;
import com.kantboot.user.balance.service.IUserAccountBalanceService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "用户余额管理", description = "用户余额管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/user-balance-web/admin/userAccountBalance")
public class UserAccountBalanceControllerOfAdmin
        extends BaseAdminController<UserAccountBalance,Long> {

    @Resource
    private IUserAccountBalanceService service;

    @AuthInit(name = "添加用户余额变动记录", description = "添加用户余额变动记录", sourceLanguageCode = "zh_CN")
    @RequestMapping("/add")
    public RestResult<Object> add(@RequestBody ChangeHandleDTO dto) {
        return RestResult.success(service.add(dto), CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    @AuthInit(name = "用户余额变动", description = "用户余额变动", sourceLanguageCode = "zh_CN")
    @RequestMapping("/change")
    public RestResult<Object> change(@RequestBody ChangeHandleDTO dto) {
        return RestResult.success(service.change(dto), CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    @AuthInit(name = "用户余额清零", description = "用户余额清零", sourceLanguageCode = "zh_CN")
    @RequestMapping("/clear")
    public RestResult<Object> clear(@RequestBody ChangeHandleDTO dto) {
        return RestResult.success(service.clear(dto), CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

}
