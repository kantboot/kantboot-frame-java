package com.kantboot.user.interrelation.web.controller;

import com.kantboot.user.interrelation.domain.dto.InterrelationSearchDTO;
import com.kantboot.user.interrelation.service.IUserAccountInterrelationService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name = "用户账号关系", description = "用户账号关系", sourceLanguageCode = "zh_CN")
@RestController
 @RequestMapping("/user-interrelation-web/interrelation")
public class UserAccountInterrelationController {

    @Resource
    private IUserAccountInterrelationService service;

    /**
     * 关注
     */
    @AuthInit(name = "关注", description = "关注", sourceLanguageCode = "zh_CN",allPass = true)
    @RequestMapping("/follow")
    public RestResult<?> follow(@RequestParam("userAccountId") Long userAccountId) {
        service.follow(userAccountId);
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    /**
     * 取消关注
     */
    @AuthInit(name = "取消关注", description = "取消关注", sourceLanguageCode = "zh_CN",allPass = true)
    @RequestMapping("/unFollow")
    public RestResult<?> unFollow(@RequestParam("userAccountId") Long userAccountId) {
        service.unFollow(userAccountId);
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    /**
     * 获取关系
     */
    @AuthInit(name = "根据当前用户获取关系", description = "根据当前用户获取关系", sourceLanguageCode = "zh_CN",allPass = true)
    @RequestMapping("/getBySelf")
    public RestResult<?> getBySelf() {
        return RestResult.success(service.getBySelf(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 根据ID获取关系
     */
    @AuthInit(name = "根据用户ID获取关系", description = "根据用户ID获取关系", sourceLanguageCode = "zh_CN",allPass = true)
    @RequestMapping("/getByUserAccountId")
    public RestResult<?> getById(@RequestParam("userAccountId") Long userAccountId) {
        return RestResult.success(service.getByUserAccountId(userAccountId), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 判断是否已关注
     */
    @AuthInit(name = "判断是否已关注", description = "判断是否已关注", sourceLanguageCode = "zh_CN",allPass = true)
    @RequestMapping("/isFollow")
    public RestResult<?> isFollow(@RequestParam("userAccountId") Long userAccountId) {
        return RestResult.success(service.isFollow(userAccountId), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 获取被关注列表（粉丝列表）
     */
    @AuthInit(name = "获取被关注列表（粉丝列表）", description = "获取被关注列表（粉丝列表）", sourceLanguageCode = "zh_CN",allPass = true)
    @RequestMapping("/getFollowedList")
    public RestResult<?> getFollowedList(@RequestBody InterrelationSearchDTO dto) {
        return RestResult.success(service.getFollowedList(dto), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 获取用户自身的被关注列表（粉丝列表）
     */
    @AuthInit(name = "获取用户自身的被关注列表（粉丝列表）", description = "获取用户自身的被关注列表（粉丝列表）", sourceLanguageCode = "zh_CN",allPass = true)
    @RequestMapping("/getSelfFollowedList")
    public RestResult<?> getSelfFollowedList(@RequestBody InterrelationSearchDTO dto){
        return RestResult.success(service.getSelfFollowedList(dto), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 获取关注列表
     */
    @AuthInit(name = "获取关注列表", description = "获取关注列表", sourceLanguageCode = "zh_CN",allPass = true)
    @RequestMapping("/getFollowerList")
    public RestResult<?> getFollowerList(@RequestBody InterrelationSearchDTO dto) {
        return RestResult.success(service.getFollowerList(dto), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 获取用户自身的关注列表
     */
    @AuthInit(name = "获取用户自身的关注列表", description = "获取用户自身的关注列表", sourceLanguageCode = "zh_CN",allPass = true)
    @RequestMapping("/getSelfFollowerList")
    public RestResult<?> getSelfFollowerList(@RequestBody InterrelationSearchDTO dto) {
        return RestResult.success(service.getSelfFollowerList(dto), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 获取互相关注列表
     * findMutualFollowPage
     */
    @AuthInit(name = "获取互相关注列表", description = "获取互相关注列表", sourceLanguageCode = "zh_CN",allPass = true)
    @RequestMapping("/getMutualFollowList")
    public RestResult<?> getMutualFollowList(@RequestBody InterrelationSearchDTO dto) {
        return RestResult.success(service.getMutualFollowList(dto), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 获取用户自身的互相关注列表
     */
    @AuthInit(name = "获取用户自身的互相关注列表", description = "获取用户自身的互相关注列表", sourceLanguageCode = "zh_CN",allPass = true)
    @RequestMapping("/getSelfMutualFollowList")
    public RestResult<?> getSelfMutualFollowList(@RequestBody InterrelationSearchDTO dto) {
        return RestResult.success(service.getSelfMutualFollowList(dto), CommonSuccessStateConsts.GET_SUCCESS);
    }



}
