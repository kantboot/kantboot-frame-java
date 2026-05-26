package com.kantboot.user.online.schedule;

import com.kantboot.user.online.dao.repository.UserAccountOnlineRepository;
import com.kantboot.user.online.domain.entity.UserAccountOnline;
import com.kantboot.user.online.service.IUserAccountOnlineService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
@Slf4j
public class UserOnlineSchedule {

    @Resource
    private UserAccountOnlineRepository userAccountOnlineRepository;

    @Resource
    private IUserAccountOnlineService userAccountOnlineService;

    /**
     * 半分钟执行一次
     */
    @Scheduled(fixedRate = 1000*30)
    public void matchUserAccountOnline() {
        // 获取三分钟之前的时间，查询出在线且三分钟未进行心跳的用户
        List<UserAccountOnline> byMaxGmtLastHeartbeatAndIsOnline = userAccountOnlineRepository.getByMaxGmtLastHeartbeatAndIsOnline(new Date(System.currentTimeMillis() - 1000 *180));
        int size = byMaxGmtLastHeartbeatAndIsOnline.size();
        if(size > 0){
            log.info("有{}个用户20秒没进行心跳，进行下线",size );
        }else{
            log.info("20秒内没有过期在线用户");
            return;
        }
        for (UserAccountOnline userAccountOnline : byMaxGmtLastHeartbeatAndIsOnline) {
            // 全部下线
            userAccountOnlineService.offline(userAccountOnline.getUserAccountId());
        }
    }

}
