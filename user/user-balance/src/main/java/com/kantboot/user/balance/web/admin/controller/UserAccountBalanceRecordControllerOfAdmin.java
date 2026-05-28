package com.kantboot.user.balance.web.admin.controller;

import com.kantboot.user.balance.domain.entity.UserAccountBalanceChangeRecord;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import com.kantboot.util.jpa.param.PageParam;
import com.kantboot.util.rest.result.RestResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AuthInit(name = "用户余额变动记录管理", description = "用户余额变动记录管理", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/user-balance-web/admin/userAccountBalanceChangeRecord")
public class UserAccountBalanceRecordControllerOfAdmin
        extends BaseAdminController<UserAccountBalanceChangeRecord,Long> {

    @Override
    @RequestMapping("/getBodyDataEasy")
    public RestResult<?> getBodyDataEasy(@RequestBody PageParam<Map<String, Object>> pageParam) {
        Map<String, Object> data = pageParam.getData();
        data.put("statusCode:and:eq","processed");
        return super.getBodyDataEasy(pageParam);
    }
}
