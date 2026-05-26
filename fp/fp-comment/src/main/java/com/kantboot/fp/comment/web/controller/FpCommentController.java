package com.kantboot.fp.comment.web.controller;

import com.kantboot.fp.comment.domain.entity.FpComment;
import com.kantboot.fp.comment.service.IFpCommentService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.jpa.param.PageParam;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fp-comment-web/comment")
@AuthInit(name = "评论",description = "评论")
public class FpCommentController {

    @Resource
    private IFpCommentService service;

    @AuthInit(name="获取请求体数据",description = "获取请求体数据",allPass = true)
    @RequestMapping("/getBodyData")
    public RestResult<?> getBodyData(@RequestBody PageParam<FpComment> pageParam) {
        return RestResult.success(service.getBodyData(pageParam), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name="获取评论列表",description = "获取评论列表",allPass = true)
    @RequestMapping("/getList")
    public RestResult<?> getList(@RequestBody FpComment fpComment) {
        return RestResult.success(service.getList(fpComment), CommonSuccessStateConsts.GET_SUCCESS);
    }

    @AuthInit(name="发布帖子",description = "发布帖子",allPass = true)
    @RequestMapping("/push")
    public RestResult<?> push(@RequestBody FpComment fpComment) {
        return RestResult.success(service.push(fpComment), CommonSuccessStateConsts.PUSH_SUCCESS);
    }

//    deleteById

    @AuthInit(name="删除评论",description = "删除评论",allPass = true)
    @RequestMapping("/deleteById")
    public RestResult<?> deleteById(@RequestParam("id") Long id) {
        service.deleteById(id);
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }


}
