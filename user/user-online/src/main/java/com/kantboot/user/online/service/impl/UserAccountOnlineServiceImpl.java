package com.kantboot.user.online.service.impl;

import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.user.online.dao.repository.UserAccountOnlineRepository;
import com.kantboot.user.online.dao.repository.UserAccountOnlineShowRepository;
import com.kantboot.user.online.domain.entity.UserAccountOnline;
import com.kantboot.user.online.domain.entity.UserAccountOnlineShow;
import com.kantboot.user.online.service.IUserAccountOnlineService;
import com.kantboot.util.event.emit.EventEmit;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class UserAccountOnlineServiceImpl implements IUserAccountOnlineService {

    @Resource
    private UserAccountOnlineRepository repository;

    @Resource
    private UserAccountOnlineShowRepository showRepository;

    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private EventEmit eventEmit;

    /**
     * 操作前的处理
     * @param userAccountId 用户账号id
     */
    private void before(Long userAccountId) {
        Boolean b = repository.existsByUserAccountId(userAccountId);
        Boolean b1 = showRepository.existsByUserAccountId(userAccountId);
        Date now = new Date();
        if (!b) {
            repository.save(new UserAccountOnline()
                    .setUserAccountId(userAccountId)
                    .setGmtLastHeartbeat(now)
                    .setGmtLastOnline(now)
                    .setGmtLastHeartbeat(now)
                    .setIsOnline(true)
                    .setIsInvisible(false)
                    .setIsOnlineOfShow(true)
            );
        }
        if(!b1){
                showRepository.save(new UserAccountOnlineShow()
                        .setUserAccountId(userAccountId)
                        .setIsOnlineOfShow(true)
                        .setGmtLastOnlineOfShow(now)
                );
        }
    }


    /**
     * 隐身
     * @param userAccountId 用户账号id
     */
    @Override
    public void invisible(Long userAccountId, Boolean isInvisible) {
        before(userAccountId);
        UserAccountOnline byUserAccountId = repository.findByUserAccountId(userAccountId);
        byUserAccountId.setIsInvisible(isInvisible);
        repository.save(byUserAccountId);
        UserAccountOnlineShow userAccountOnlineShow = showRepository.findByUserAccountId(userAccountId);
        Date now = new Date();
        // 如果是隐身状态，且用来展示的在线状态为在线，则修改用来展示的在线状态为离线
        if(isInvisible && userAccountOnlineShow.getIsOnlineOfShow()){
            userAccountOnlineShow
                    .setUserAccountId(userAccountId)
                    .setIsOnlineOfShow(false)
                    .setGmtLastOfflineOfShow(now);
            showRepository.save(userAccountOnlineShow);
        }
        // 如果是非隐身状态，且用来展示的在线状态为离线，则修改用来展示的在线状态为在线
        if(!isInvisible && !userAccountOnlineShow.getIsOnlineOfShow()){
            userAccountOnlineShow
                    .setUserAccountId(userAccountId)
                    .setIsOnlineOfShow(true)
                    .setGmtLastOnlineOfShow(now);
            showRepository.save(userAccountOnlineShow);
        }
    }

    /**
     * 对自己隐身
     */
    @Override
    public void invisibleSelf(Boolean isInvisible) {
        invisible(userAccountService.getSelfId(), isInvisible);
    }

    /**
     * 心跳
     * @param userAccountId 用户账号ID
     */
    @Override
    public void heartbeat(Long userAccountId) {
        before(userAccountId);
        UserAccountOnline byUserAccountId = repository.findByUserAccountId(userAccountId);
        if(!Boolean.TRUE.equals(byUserAccountId.getIsOnline())){
            online(userAccountId);
        }
        // 更新心跳时间
        byUserAccountId.setIsOnline(true);
        byUserAccountId.setGmtLastHeartbeat(new Date());
        repository.save(byUserAccountId);
    }

    /**
     * 对自己心跳
     */
    @Override
    public void heartbeatSelf() {
        heartbeat(userAccountService.getSelfId());
    }

    /**
     * 上线
     */
    @Override
    public void online(Long userAccountId) {
        before(userAccountId);
        Date now = new Date();
        UserAccountOnline byUserAccountId = repository.findByUserAccountId(userAccountId);
        byUserAccountId.setIsOnline(true);
        byUserAccountId.setGmtLastOnline(now);
        byUserAccountId.setGmtLastHeartbeat(now);
        repository.save(byUserAccountId);
        // 如果自己非隐身状态，便修改用来展示的在线状态
        if(!byUserAccountId.getIsInvisible()){
            UserAccountOnlineShow userAccountOnlineShow = showRepository.findByUserAccountId(userAccountId);
            userAccountOnlineShow
                    .setUserAccountId(userAccountId)
                    .setIsOnlineOfShow(true)
                    .setGmtLastOnlineOfShow(now);
            showRepository.save(userAccountOnlineShow);
        }
        eventEmit.to("UserOnline:online",userAccountId);
    }

    @Override
    public void online(Long userAccountId, Map<String, Object> event) {
        online(userAccountId);
        event.put("userAccountId",userAccountId);
        eventEmit.to("UserOnline:online:map", event);
    }

    @Override
    public void onlineSelf() {
        online(userAccountService.getSelfId());
    }

    @Override
    public void offline(Long userAccountId) {
        before(userAccountId);
        UserAccountOnline byUserAccountId = repository.findByUserAccountId(userAccountId);
        byUserAccountId.setIsOnline(false);
        byUserAccountId.setGmtLastOffline(new Date());
        repository.save(byUserAccountId);
        if(!byUserAccountId.getIsInvisible()){
            UserAccountOnlineShow userAccountOnlineShow = showRepository.findByUserAccountId(userAccountId);
            userAccountOnlineShow
                    .setUserAccountId(userAccountId)
                    .setIsOnlineOfShow(false)
                    .setGmtLastOfflineOfShow(new Date());
            showRepository.save(userAccountOnlineShow);
        }
        eventEmit.to("UserOnline:offline", userAccountId);
    }


    @Override
    public void offlineSelf() {
        offline(userAccountService.getSelfId());
    }
}
