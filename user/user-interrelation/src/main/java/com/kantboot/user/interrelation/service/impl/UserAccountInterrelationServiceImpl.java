package com.kantboot.user.interrelation.service.impl;

import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.user.interrelation.dao.repository.UserAccountInterrelationFollowRepository;
import com.kantboot.user.interrelation.dao.repository.UserAccountInterrelationRepository;
import com.kantboot.user.interrelation.domain.dto.InterrelationSearchDTO;
import com.kantboot.user.interrelation.domain.entity.UserAccountInterrelation;
import com.kantboot.user.interrelation.domain.entity.UserAccountInterrelationFollow;
import com.kantboot.user.interrelation.service.IUserAccountInterrelationService;
import com.kantboot.util.event.emit.EventEmit;
import com.kantboot.util.jpa.param.PageParam;
import com.kantboot.util.rest.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccountInterrelationServiceImpl
    implements IUserAccountInterrelationService {

    @Resource
    private UserAccountInterrelationRepository repository;

    @Resource
    private UserAccountInterrelationFollowRepository followRepository;

    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private EventEmit eventEmit;

    @Override
    public void follow(Long userAccountId) {
        Long selfId = userAccountService.getSelfId();
        boolean b = followRepository.existsByUserAccountIdOfFollowerAndUserAccountIdOfFollowed(
                selfId,
                userAccountId
        );
        if (b) {
            throw BaseException.of("alreadyFollowed", "已经关注了","zh_CN");
        }
        followRepository.save(
                new UserAccountInterrelationFollow()
                        .setUserAccountIdOfFollower(selfId)
                        .setUserAccountIdOfFollowed(userAccountId)
        );
        // 发送事件，告知用户账号被关注
        eventEmit.to("UserAccountInterrelation:followed",userAccountId);
        // 更新关系数量
        updateRelationCount(userAccountId);
        updateRelationCount(selfId);
    }

    @Override
    public void unFollow(Long userAccountId) {
        Long selfId = userAccountService.getSelfId();
        boolean b = followRepository.existsByUserAccountIdOfFollowerAndUserAccountIdOfFollowed(
                selfId,
                userAccountId
        );
        if (!b) {
            throw BaseException.of("notFollowed", "没有关注过","zh_CN");
        }
        followRepository.deleteByUserAccountIdOfFollowerAndUserAccountIdOfFollowed(
                selfId,
                userAccountId
        );
        // 发送事件，告知用户账号取消关注
        eventEmit.to("UserAccountInterrelation:unfollowed",userAccountId);
        // 更新关系数量
        updateRelationCount(userAccountId);
        updateRelationCount(selfId);
    }

    @Override
    public UserAccountInterrelation updateRelationCount(Long userAccountId) {
        UserAccountInterrelation byUserAccountId = repository.findByUserAccountId(userAccountId);
        if (byUserAccountId == null) {
            byUserAccountId = new UserAccountInterrelation();
            byUserAccountId.setUserAccountId(userAccountId);
            byUserAccountId.setFollowCount(0L);
            byUserAccountId.setFollowedCount(0L);
            byUserAccountId.setMutualFollowCount(0L);
            byUserAccountId = repository.save(byUserAccountId);
        }
        Long followCount = followRepository.countByUserAccountIdOfFollower(userAccountId);
        Long followedCount = followRepository.countByUserAccountIdOfFollowed(byUserAccountId.getUserAccountId());
        // 互相关注数量
        Long mutualFollowCount = followRepository.countMutualFollows(userAccountId);
        byUserAccountId.setFollowCount(followCount);
        byUserAccountId.setFollowedCount(followedCount);
        byUserAccountId.setMutualFollowCount(mutualFollowCount);
        repository.save(byUserAccountId);
        return byUserAccountId;
    }

    @Override
    public UserAccountInterrelation getByUserAccountId(Long userAccountId) {
        return repository.findByUserAccountId(userAccountId);
    }

    @Override
    public UserAccountInterrelation getBySelf() {
        return getByUserAccountId(userAccountService.getSelfId());
    }

    @Override
    public boolean isFollow(Long userAccountId) {
        Long selfId = userAccountService.getSelfId();
        return followRepository.existsByUserAccountIdOfFollowerAndUserAccountIdOfFollowed(
                selfId,
                userAccountId
        );
    }

    @Override
    public List<UserAccountInterrelationFollow> getFollowedList(InterrelationSearchDTO dto) {
        PageParam<InterrelationSearchDTO> pageParam = new PageParam<>();
        pageParam.setPageNumber(1);
        pageParam.setPageSize(1000);
        Page<UserAccountInterrelationFollow> bySearch =
                followRepository.findFollowedPage(dto, pageParam.getPageable());
        return bySearch.getContent();
    }

    @Override
    public List<UserAccountInterrelationFollow> getSelfFollowedList(InterrelationSearchDTO dto) {
        PageParam<InterrelationSearchDTO> pageParam = new PageParam<>();
        pageParam.setPageNumber(1);
        pageParam.setPageSize(1000);
        dto.setUserAccountId(userAccountService.getSelfId());
        Page<UserAccountInterrelationFollow> bySearch =
                followRepository.findFollowedPage(dto, pageParam.getPageable());
        return bySearch.getContent();
    }

    @Override
    public List<UserAccountInterrelationFollow> getFollowerList(InterrelationSearchDTO dto) {
        PageParam<InterrelationSearchDTO> pageParam = new PageParam<>();
        pageParam.setPageNumber(1);
        pageParam.setPageSize(1000);
        Page<UserAccountInterrelationFollow> bySearch =
                followRepository.findFollowerPage(dto, pageParam.getPageable());
        return bySearch.getContent();
    }

    @Override
    public List<UserAccountInterrelationFollow> getSelfFollowerList(InterrelationSearchDTO dto) {
        PageParam<InterrelationSearchDTO> pageParam = new PageParam<>();
        pageParam.setPageNumber(1);
        pageParam.setPageSize(1000);
        dto.setUserAccountId(userAccountService.getSelfId());
        Page<UserAccountInterrelationFollow> bySearch =
                followRepository.findFollowerPage(dto, pageParam.getPageable());
        return bySearch.getContent();
    }

    @Override
    public List<UserAccountInterrelationFollow> getMutualFollowList(InterrelationSearchDTO dto) {
        PageParam<InterrelationSearchDTO> pageParam = new PageParam<>();
        pageParam.setPageNumber(1);
        pageParam.setPageSize(1000);
        Page<UserAccountInterrelationFollow> bySearch =
                followRepository.findMutualFollowPage(dto, pageParam.getPageable());
        return bySearch.getContent();
    }

    @Override
    public List<UserAccountInterrelationFollow> getSelfMutualFollowList(InterrelationSearchDTO dto) {
        PageParam<InterrelationSearchDTO> pageParam = new PageParam<>();
        pageParam.setPageNumber(1);
        pageParam.setPageSize(1000);
        dto.setUserAccountId(userAccountService.getSelfId());
        Page<UserAccountInterrelationFollow> bySearch =
                followRepository.findMutualFollowPage(dto, pageParam.getPageable());
        return bySearch.getContent();
    }
}
