package com.kantboot.fp.comment.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.fp.comment.consts.FpCommentAuditStatusConsts;
import com.kantboot.fp.comment.domain.entity.FpComment;
import com.kantboot.fp.comment.repository.FpCommentRepository;
import com.kantboot.fp.comment.service.IFpCommentService;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.util.event.emit.EventEmit;
import com.kantboot.util.jpa.param.PageParam;
import com.kantboot.util.jpa.result.PageResult;
import com.kantboot.util.rest.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FpCommentServiceImpl
    implements IFpCommentService {

    @Resource
    private FpCommentRepository repository;

    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private EventEmit eventEmit;

    @Override
    public PageResult getBodyData(PageParam<FpComment> pageParam) {
        FpComment data = pageParam.getData();
        if(data == null){
            pageParam.setData(new FpComment());
        }
        Page<FpComment> bodyData = repository.getBodyData(pageParam.getData(), pageParam.getPageable());
        return PageResult.of(bodyData);
    }

    @Override
    public List<FpComment> getList(FpComment param) {
        PageParam<FpComment> pageParam = new PageParam<>();
        pageParam.setData(param);
        FpComment data = pageParam.getData();
        if(data == null){
            pageParam.setData(new FpComment());
            data = pageParam.getData();
        }
        data.setAuditStatus(FpCommentAuditStatusConsts.PASS);
        pageParam.setData(data);
        pageParam.setSort("gmtCreate");
        pageParam.setOrderBy("DESC");

        // 只取第1页，前100条
        pageParam.setPageNumber(1);
        pageParam.setPageSize(3000);
        Page<FpComment> bodyData = repository.getBodyData(pageParam.getData(), pageParam.getPageable());
        return bodyData.getContent();
    }

    @Override
    public FpComment push(FpComment fpComment) {
        Long selfId = userAccountService.getSelfId();
        fpComment.setAuditStatus(FpCommentAuditStatusConsts.AUDITING);
        fpComment.setId(null);
        fpComment.setIsDelete(false);
        fpComment.setUserAccountIdOfPusher(selfId);
        // 获取fpComment的ktFormatOfView字段的值
        Object ktFormatOfView = fpComment.getKtFormatOfView();
        if(ktFormatOfView == null){
            fpComment.setKtFormatOfView(new JSONArray());
        }
        JSONArray ktFormatOfViewJson = JSONArray.parse(JSON.toJSONString(ktFormatOfView));
        String textContent = "";
        // 获取所有textContent字段的值，拼接成一个字符串
        for (int i = 0; i < ktFormatOfViewJson.size(); i++) {
            JSONObject obj = ktFormatOfViewJson.getJSONObject(i);
            if("text".equals(obj.getString("type"))){
                textContent += obj.getString("content");
            }
        }
        fpComment.setTextContent(textContent);
        FpComment save = repository.save(fpComment);
        eventEmit.to("FpComment:push",save);

        return save;
    }

    @Override
    public void pass(Long id) {
        FpComment fpComment = repository.findById(id).orElseThrow(() -> BaseException.of("commentIsNotExist","评论不存在"));
        fpComment.setAuditStatus(FpCommentAuditStatusConsts.PASS);
        repository.save(fpComment);
        eventEmit.to("FpComment:pass",fpComment);
    }

    @Override
    public void reject(Long id, String reason) {
        FpComment fpComment = repository.findById(id).orElseThrow(() -> BaseException.of("commentIsNotExist","评论不存在"));
        fpComment.setAuditStatus(FpCommentAuditStatusConsts.FAIL);
        fpComment.setAuditFailReason(reason);
        repository.save(fpComment);
        eventEmit.to("FpComment:reject",fpComment);
    }

    @Override
    public void update(FpComment fpComment) {
        FpComment dbFpComment = repository.findById(fpComment.getId()).orElseThrow(() -> BaseException.of("commentIsNotExist","评论不存在"));
        // 只能更新ktFormatOfView字段
        dbFpComment.setKtFormatOfView(fpComment.getKtFormatOfView());
        // 更新textContent字段
        Object ktFormatOfView = dbFpComment.getKtFormatOfView();
        if(ktFormatOfView == null){
            dbFpComment.setKtFormatOfView(new JSONArray());
        }
        JSONArray ktFormatOfViewJson = JSONArray.parse(JSON.toJSONString(ktFormatOfView));
        String textContent = "";
        // 获取所有textContent字段的值，拼接成一个字符串
        for (int i = 0; i < ktFormatOfViewJson.size(); i++) {
            JSONObject obj = ktFormatOfViewJson.getJSONObject(i);
            if("text".equals(obj.getString("type"))){
                textContent += obj.getString("content");
            }
        }
        dbFpComment.setRelatedContent(fpComment.getRelatedContent());
        dbFpComment.setTextContent(textContent);
        repository.save(dbFpComment);
        eventEmit.to("FpComment:update",dbFpComment);
    }

    @Override
    public void deleteById(Long id) {
        FpComment fpComment = repository.findById(id).orElseThrow(() -> BaseException.of("commentIsNotExist","评论不存在"));
        fpComment.setIsDelete(true);
        repository.save(fpComment);
        eventEmit.to("FpComment:delete",fpComment);
    }
}
