package com.kantboot.user.account.web.controller;

import com.kantboot.user.account.domain.entity.UserAccount;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.event.emit.EventEmit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AuthInit(name = "用户账号", description = "用户账号", sourceLanguageCode = "zh_CN")
@RestController
@RequestMapping("/user-account-web/userAccount")
public class UserAccountController {

    @Resource
    private IUserAccountService service;

    @Resource
    private EventEmit emit;

    /**
     * 根据邮箱查询
     */
    @AuthInit(name = "根据邮箱获取用户账号", description = "根据邮箱获取用户账号", sourceLanguageCode = "zh_CN")
    @RequestMapping("/getByEmail")
    public RestResult<Object> getByEmail(@RequestParam("email") String email) {
        return RestResult.success(service.getByEmail(email), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 获取自己的用户账号
     *
     * @return 自身的用户账号信息
     */
    @AuthInit(name = "获取自己的用户账号", description = "获取自己的用户账号", sourceLanguageCode = "zh_CN", allPass = true)
    @RequestMapping("/getSelf")
    public RestResult<Object> getSelf() {
        UserAccount self = service.getSelf();
        emit.to("UserAccount:requestGetSelf",self);
        return RestResult.success(self, CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "编辑用户自己的基本信息", description = "编辑用户自己的基本信息", sourceLanguageCode = "zh_CN", allPass = true)
    @RequestMapping("/editCommonInfoSelf")
    public RestResult<Object> editCommonInfo(@RequestBody UserAccount userAccount) {
        return RestResult.success(service.editCommonInfoSelf(userAccount), CommonSuccessStateConsts.SAVE_SUCCESS);
    }

    /**
     * 根据id获取用户账号
     * getById
     */
    @AuthInit(name = "根据id获取用户账号", description = "根据id获取用户账号", sourceLanguageCode = "zh_CN", allPass = true,noNeedLogin = true)
    @RequestMapping("/getById")
    public RestResult<Object> getById(@RequestParam("id") Long id) {
        return RestResult.success(service.getById(id), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @Data
    public static class GetByIdsParam {
       private List<Long> ids;
    }

    /**
     * 根据ids获取用户账号
     */
    @AuthInit(name = "根据ids获取用户账号", description = "根据ids获取用户账号", sourceLanguageCode = "zh_CN", allPass = true)
    @RequestMapping("/getByIds")
    public RestResult<Object> getByIds(@RequestBody GetByIdsParam param) {
        return RestResult.success(service.getByIds(param.getIds()), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 保存自己的用户账号
     *
     * @param userAccount 用户账号信息
     * @return 保存后的用户账号信息
     */
    @AuthInit(name = "保存自己的用户账号", description = "保存自己的用户账号", sourceLanguageCode = "zh_CN", allPass = true)
    @RequestMapping("/saveSelf")
    public RestResult<Object> saveSelf(@RequestBody UserAccount userAccount) {
        return RestResult.success(service.saveSelf(userAccount), CommonSuccessStateConsts.SAVE_SUCCESS);
    }

    /**
     * 修改密码
     */
    @AuthInit(name = "修改密码", description = "修改密码", sourceLanguageCode = "zh_CN", allPass = true)
    @RequestMapping("/changePassword")
    public RestResult<Void> changePassword(
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword) {
        service.changePassword(oldPassword, newPassword);
        return RestResult.success(null, CommonSuccessStateConsts.CHANGE_SUCCESS);
    }

//    changePasswordNoHasOldPassword
    @AuthInit(name = "修改密码(不验证旧密码)", description = "修改密码(不验证旧密码)", sourceLanguageCode = "zh_CN", allPass = true)
    @RequestMapping("/changePasswordNoHasOldPassword")
     public RestResult<Void> changePasswordNoHasOldPassword(
            @RequestParam("newPassword") String newPassword) {
        service.changePasswordNoHasOldPassword(newPassword);
        return RestResult.success(null, CommonSuccessStateConsts.CHANGE_SUCCESS);
    }


    /**
     * 判断是否存在手机号
     */
    @AuthInit(name = "判断是否存在手机号", description = "判断是否存在手机号", sourceLanguageCode = "zh_CN")
    @RequestMapping("/existsByPhone")
    public RestResult<Boolean> existsByPhone(
            @RequestParam("phoneAreaCode") String phoneAreaCode,
            @RequestParam("phone") String phone) {
        return RestResult.success(service.existsByPhone(phoneAreaCode, phone), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 是否存在密码
     */
    @AuthInit(name = "用户自身是否存在密码", description = "用户自身是否存在密码", sourceLanguageCode = "zh_CN")
    @RequestMapping("/isHasPasswordSelf")
    public RestResult<Boolean> isHasPasswordSelf() {
        return RestResult.success(service.isHasPasswordSelf(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 设置密码
     */
    @AuthInit(name = "用户自身设置密码", description = "用户自身设置密码", sourceLanguageCode = "zh_CN")
    @RequestMapping("/setPasswordSelf")
    public RestResult<Void> setPasswordSelf(@RequestParam("password") String password) {
        service.setPasswordSelf(password);
        return RestResult.success(null, CommonSuccessStateConsts.CHANGE_SUCCESS);
    }

    @AuthInit(name = "根据用户名获取用户账号", description = "根据用户名获取用户账号", sourceLanguageCode = "zh_CN")
    @RequestMapping("/getByUsername")
    public RestResult<UserAccount> getByUsername(@RequestParam("username") String username) {
        return RestResult.success(service.getByUsername(username), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name = "根据用户名模糊获取用户账号", description = "根据用户名模糊获取用户账号", sourceLanguageCode = "zh_CN")
    @RequestMapping("/getByUsernameVague")
    public RestResult<List<UserAccount>> getByUsernameVague(@RequestParam("username") String username) {
        return RestResult.success(service.getByUsernameVague(username), CommonSuccessStateConsts.GET_SUCCESS);
    }

}
