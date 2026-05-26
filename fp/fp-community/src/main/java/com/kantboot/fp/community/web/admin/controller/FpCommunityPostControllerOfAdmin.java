package com.kantboot.fp.community.web.admin.controller;

import com.kantboot.fp.community.domain.entity.FpCommunityPost;
import com.kantboot.fp.community.service.IFpCommunityPostService;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import com.kantboot.util.rest.consts.CommonSuccessStateConsts;
import com.kantboot.util.rest.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name="帖子管理",description = "帖子管理")
@RestController
@RequestMapping("/fp-community-web/admin/post")
public class FpCommunityPostControllerOfAdmin
    extends BaseAdminController<FpCommunityPost,Long> {

    @Resource
    private IFpCommunityPostService service;

    @AuthInit(name = "通过审核",description = "通过审核")
    @RequestMapping("/pass")
    public RestResult<?> pass(@RequestParam("id") Long id) {
        service.pass(id);
        return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
    }

    @AuthInit(name = "审核不通过",description = "审核不通过")
    @RequestMapping("/reject")
    public RestResult<?> reject(@RequestParam("id") Long id,
                                 @RequestParam("reason") String reason) {
          service.reject(id, reason);
          return RestResult.success(null, CommonSuccessStateConsts.OPERATION_SUCCESS);
     }

}