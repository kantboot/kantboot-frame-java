package com.kantboot.fp.community.web.admin.controller;

import com.kantboot.fp.community.domain.entity.FpCommunityPostType;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthInit(name="帖子类型管理",description = "帖子类型管理")
@RestController
@RequestMapping("/fp-community-web/admin/postType")
public class FpCommunityPostTypeControllerOfAdmin
    extends BaseAdminController<FpCommunityPostType,Long> {
}