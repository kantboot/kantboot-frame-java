package com.kantboot.fp.community.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.fp.community.consts.FpCommunityAuditStatusConsts;
import com.kantboot.fp.community.dao.repository.FpCommunityPostCollectRepository;
import com.kantboot.fp.community.dao.repository.FpCommunityPostRepository;
import com.kantboot.fp.community.domain.entity.FpCommunityPost;
import com.kantboot.fp.community.domain.entity.FpCommunityPostCollect;
import com.kantboot.fp.community.domain.entity.FpCommunityPostRelationship;
import com.kantboot.fp.community.service.IFpCommunityPostService;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.util.cache.CacheUtil;
import com.kantboot.util.event.emit.EventEmit;
import com.kantboot.util.rest.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class FpCommunityPostServiceImpl
    implements IFpCommunityPostService {

    @Resource
    private FpCommunityPostRepository repository;

    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private EventEmit eventEmit;

    @Resource
    private FpCommunityPostCollectRepository postCollectRepository;

    @Resource
    private CacheUtil cacheUtil;


    @Override
    public FpCommunityPost push(FpCommunityPost post) {
        post.setId(null);

        // 如果ktFormatOfView不为空，则获取所有type为text的内容，放置searchStr中
        Object ktFormatOfView = post.getKtFormatOfView();
        if(ktFormatOfView!=null){
            StringBuilder sb = new StringBuilder();
            JSONArray contents = JSONArray.parseArray(JSON.toJSONString(ktFormatOfView));
            for (int i = 0; i < contents.size(); i++) {
                JSONObject content = contents.getJSONObject(i);
                String type = content.getString("type");
                if ("text".equals(type)) {
                    String value = content.getString("content");
                    sb.append(value);
                }
            }
            post.setSearchStr(sb.toString());
            post.setTextContent(sb.toString());
        }

        Long selfId = userAccountService.getSelfId();
        // 设置发布者ID
        post.setUserAccountIdOfPusher(selfId);
        // 设置帖子为非删除
        post.setIsDelete(false);
        // 默认审核中
//        post.setAuditStatus(FpCommunityAuditStatusConsts.AUDITING);
        FpCommunityPost result = repository.save(post);
        result.setPriority(post.getId());
        result = repository.save(result);
        // 初始化帖子关系
        post.setRelationship(new FpCommunityPostRelationship()
                .setCollectCount(0L)
                .setLikeCount(0L)
                .setCommentCount(0L)
        );
        // 发送帖子发布事件
        eventEmit.to("FpCommunityPost:push",result);
        return result;
    }

    @Override
    public FpCommunityPost edit(FpCommunityPost oldPost) {
        Long selfId = userAccountService.getSelfId();
        // 根据用户id获取帖子
        FpCommunityPost existingPost = repository.findById(oldPost.getId()).orElse(null);
        if(existingPost==null){
            throw BaseException.of("postNotFound","帖子未找到","zh_CN");
        }
        if(!existingPost.getUserAccountIdOfPusher().equals(selfId)){
            throw BaseException.of("illegalOperation","非法操作","zh_CN");
        }

        FpCommunityPost post = BeanUtil.copyProperties(existingPost, FpCommunityPost.class);
        post.setKtFormatOfView(oldPost.getKtFormatOfView());
        post.setTitle(oldPost.getTitle());
        post.setFileIdOfCoverImage(oldPost.getFileIdOfCoverImage());


        // 如果ktFormatOfView不为空，则获取所有type为text的内容，放置searchStr中
        Object ktFormatOfView = post.getKtFormatOfView();
        if(ktFormatOfView!=null){
            StringBuilder sb = new StringBuilder();
            JSONArray contents = JSONArray.parseArray(JSON.toJSONString(ktFormatOfView));
            for (int i = 0; i < contents.size(); i++) {
                JSONObject content = contents.getJSONObject(i);
                String type = content.getString("type");
                if ("text".equals(type)) {
                    String value = content.getString("content");
                    sb.append(value);
                }
            }
            post.setSearchStr(sb.toString());
            post.setTextContent(sb.toString());
        }

        // 设置发布者ID
        post.setUserAccountIdOfPusher(selfId);
        // 设置帖子为非删除
        post.setIsDelete(false);
        Integer editCount = existingPost.getEditCount();
        if(editCount==null){
            editCount = 0;
        }
        post.setEditCount(editCount+1);
        // 默认审核中
//        post.setAuditStatus(FpCommunityAuditStatusConsts.AUDITING);
        FpCommunityPost result = repository.save(post);
//        result.setPriority(post.getId());
        result = repository.save(result);
        // 发送帖子发布事件
        eventEmit.to("FpCommunityPost:edit",result);
        return result;
    }

    @Override
    public FpCommunityPost getById(Long id) {
        FpCommunityPost fpCommunityPost = repository.findById(id).orElse(null);
        // 判断当前用户是否收藏该帖子
        if(fpCommunityPost!=null){
            Long selfId = userAccountService.getSelfIdNoThrow();
            if(selfId==null){
                fpCommunityPost.setIsCollectedBySelf(false);
                return fpCommunityPost;
            }
            FpCommunityPostCollect byPostIdAndUserAccountId = postCollectRepository.findByPostIdAndUserAccountId(id, selfId);
            if(byPostIdAndUserAccountId!=null){
                fpCommunityPost.setIsCollectedBySelf(true);
            }else{
                fpCommunityPost.setIsCollectedBySelf(false);
            }
            return fpCommunityPost;
        }
        return null;
    }

    @Override
    public FpCommunityPost pass(Long id) {
        FpCommunityPost post = repository.findById(id).orElse(null);
        if(post!=null){
            post.setAuditStatus(FpCommunityAuditStatusConsts.PASS);
            repository.save(post);
        }
        return post;
    }

    @Override
    public FpCommunityPost reject(Long id,String reason) {
        FpCommunityPost post = repository.findById(id).orElse(null);
        if(post!=null){
            post.setAuditStatus(FpCommunityAuditStatusConsts.FAIL);
            post.setAuditFailReason(reason);
            repository.save(post);
        }
        return post;
    }

    @Override
    public FpCommunityPost update(FpCommunityPost post) {
        FpCommunityPost existingPost = repository.findById(post.getId()).orElse(null);
        if(existingPost!=null){
            return repository.save(post);
        }
        return null;
    }

    @Override
    public List<FpCommunityPost> getByUserAccountIdOfPusher(Long userAccountIdOfPusher) {
        return repository.findByUserAccountIdOfPusher(userAccountIdOfPusher);
    }

    public List<FpCommunityPost> getBySelfUserAccountIdOfPusher() {
        Long selfId = userAccountService.getSelfId();
        return getByUserAccountIdOfPusher(selfId);
    }

    @Override
    public void deleteById(Long id) {
        FpCommunityPost post = repository.findById(id).orElse(null);
        if(post!=null){
            // 要看是不是自己的帖子
            Long selfId = userAccountService.getSelfId();
            if(!post.getUserAccountIdOfPusher().equals(selfId)){
                // 提示非法操作
                throw BaseException.of("illegalOperation","非法操作");
            }

            post.setIsDelete(true);
            repository.save(post);
        }
    }

    @Override
    public void collect(Long postId) {
        Long selfId = userAccountService.getSelfId();
        // cacheUtil加锁
        if(!cacheUtil.lock("FpCommunityPost:collect:"+postId+":"+selfId,30, TimeUnit.SECONDS)){
            throw BaseException.of("repeatOperation","请勿重复操作","zh-CN");
        }

        FpCommunityPostCollect byPostIdAndUserAccountId = postCollectRepository.findByPostIdAndUserAccountId(postId, selfId);
        if(byPostIdAndUserAccountId==null) {
            // 如果没有收藏，则进行收藏
            FpCommunityPostCollect collect = new FpCommunityPostCollect();
            collect.setPostId(postId);
            collect.setUserAccountId(selfId);
            postCollectRepository.save(collect);
            cacheUtil.unlock("FpCommunityPost:collect:"+postId+":"+selfId);
            return;
        }

        throw BaseException.of("alreadyCollected","您已收藏该帖子","zh-CN");

    }

    @Override
    public void unCollect(Long postId) {
        // 获取当前用户ID
        Long selfId = userAccountService.getSelfId();
        // cacheUtil加锁
        if(!cacheUtil.lock("FpCommunityPost:unCollect:"+postId+":"+selfId,30, TimeUnit.SECONDS)){
            throw BaseException.of("repeatOperation","请勿重复操作","zh-CN");
        }
        FpCommunityPostCollect byPostIdAndUserAccountId = postCollectRepository.findByPostIdAndUserAccountId(postId, selfId);
        if(byPostIdAndUserAccountId!=null) {
            postCollectRepository.delete(byPostIdAndUserAccountId);
            cacheUtil.unlock("FpCommunityPost:unCollect:"+postId+":"+selfId);
            return;
        }
        throw BaseException.of("notCollected","您未收藏该帖子","zh-CN");
    }

    @Override
    public List<FpCommunityPostCollect> getCollectList() {
        Long selfId = userAccountService.getSelfId();
        return postCollectRepository.findByUserAccountId(selfId);
    }
}
