package com.kantboot.fp.comment.service;

import com.kantboot.fp.comment.domain.entity.FpComment;
import com.kantboot.util.jpa.param.PageParam;
import com.kantboot.util.jpa.result.PageResult;

import java.util.List;

public interface IFpCommentService {

    PageResult getBodyData(PageParam<FpComment> pageParam);

    List<FpComment> getList(FpComment param);

    /**
     * 发布评论
     */
    FpComment push(FpComment fpComment);

    /**
     * 通过
     */
    void pass(Long id);

    /**
     * 审核拒绝
     */
    void reject(Long id, String reason);

    void update(FpComment fpComment);

    /**
     * 删除评论
     */
    void deleteById(Long id);

}
