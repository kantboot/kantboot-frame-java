package com.kantboot.fp.community.web.controller;

import com.kantboot.fp.community.domain.entity.FpCommunityPost;
import com.kantboot.fp.community.service.IFpCommunityPostService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name="帖子",description = "帖子")
@RestController
@RequestMapping("/fp-community-web/post")
public class FpCommunityPostController {

    @Resource
    private IFpCommunityPostService service;

    @AuthInit(name = "帖子发布",description = "帖子发布",allPass = true)
    @RequestMapping("/push")
    public RestResult<?> push(@RequestBody FpCommunityPost post) {
        return RestResult.success(service.push(post), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name="编辑帖子",description = "编辑帖子",allPass = true)
    @RequestMapping("/edit")
    public RestResult<?> edit(@RequestBody FpCommunityPost post) {
        return RestResult.success(service.edit(post), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name="获取当前用户发布的帖子",description = "获取当前用户的帖子",allPass = true)
    @RequestMapping("/getBySelfUserAccountIdOfPusher")
    public RestResult<?> getBySelfUserAccountIdOfPusher() {
        return RestResult.success(service.getBySelfUserAccountIdOfPusher(), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name="删除帖子",description = "删除帖子",allPass = true)
    @RequestMapping("/deleteById")
    public RestResult<?> deleteById(@RequestParam("id") Long id) {
        service.deleteById(id);
        return RestResult.success(null,CommonSuccessStateConsts.REMOVE_SUCCESS);
    }

    @AuthInit(name="收藏帖子",description = "收藏帖子",allPass = true)
    @RequestMapping("/collect")
    public RestResult<?> collect(@RequestParam("postId") Long postId) {
        service.collect(postId);
        return RestResult.success(null,CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    /**
     * 根据ID取消收藏
     */
    @AuthInit(name="取消收藏帖子",description = "取消收藏帖子",allPass = true)
    @RequestMapping("/unCollect")
    public RestResult<?> unCollect(@RequestParam("postId") Long postId) {
        service.unCollect(postId);
        return RestResult.success(null,CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    /**
     * 根据ID获取帖子
     */
    @AuthInit(name="根据ID获取帖子",description = "根据ID获取帖子",allPass = true,noNeedLogin = true)
    @RequestMapping("/getById")
    public RestResult<?> getById(@RequestParam("id") Long id) {
        return RestResult.success(service.getById(id), CommonSuccessStateConsts.GET_SUCCESS);
    }

    /**
     * 获取收藏聊表
     */
    @AuthInit(name="获取当前用户收藏的帖子",description = "获取当前用户收藏的帖子",allPass = true)
    @RequestMapping("/getCollectList")
    public RestResult<?> getCollectList() {
        return RestResult.success(service.getCollectList(), CommonSuccessStateConsts.GET_SUCCESS);
    }

}
