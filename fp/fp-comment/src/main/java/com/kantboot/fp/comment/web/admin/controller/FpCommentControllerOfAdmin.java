package com.kantboot.fp.comment.web.admin.controller;

import com.kantboot.fp.comment.domain.entity.FpComment;
import com.kantboot.util.auth.annotation.AuthInit;
import com.kantboot.util.base.control.controller.BaseAdminController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fp-comment-web/admin/comment")
@AuthInit(name = "评论管理",description = "评论管理")
public class FpCommentControllerOfAdmin
    extends BaseAdminController<FpComment,Long> {

}
