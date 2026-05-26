package com.kantboot.user.location.service.impl;

import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.user.location.dao.repository.UserAccountLocationLogRepository;
import com.kantboot.user.location.dao.repository.UserAccountLocationRepository;
import com.kantboot.user.location.domain.entity.UserAccountLocation;
import com.kantboot.user.location.domain.entity.UserAccountLocationLog;
import com.kantboot.user.location.service.IUserAccountLocationService;
import com.kantboot.user.location.slot.UserAccountLocationSlot;
import com.kantboot.util.http.HttpRequestHeaderUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserAccountLocationServiceImpl implements IUserAccountLocationService {

    @Resource
    private UserAccountLocationRepository repository;

    @Resource
    private UserAccountLocationLogRepository logRepository;

    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private HttpRequestHeaderUtil httpRequestHeaderUtil;

    @Resource
    private UserAccountLocationSlot slot;

    @Override
    public UserAccountLocation save(UserAccountLocation entity) {
        String areaCode = slot.getAreaCode(entity.getLatitude(), entity.getLongitude(), entity.getIp());
        UserAccountLocation byUserAccountId = repository.findByUserAccountId(entity.getUserAccountId());
        if (byUserAccountId == null) {
            byUserAccountId = repository.save(entity);
            entity.setId(byUserAccountId.getId());
            UserAccountLocationLog userAccountLocationLog = new UserAccountLocationLog();
            userAccountLocationLog.setUserAccountId(entity.getUserAccountId());
            userAccountLocationLog.setLatitude(entity.getLatitude());
            userAccountLocationLog.setLongitude(entity.getLongitude());
            userAccountLocationLog.setIp(entity.getIp());
            userAccountLocationLog.setAreaCode(areaCode);
            logRepository.save(userAccountLocationLog);
            return byUserAccountId;
        }

        // 如果entity的latitude和longitude和byUserAccountId的latitude和longitude相同，则不更新
        if (entity.getLatitude()!=null&&entity.getLongitude()!=null&&entity.getLatitude().equals(byUserAccountId.getLatitude())
                && entity.getLongitude().equals(byUserAccountId.getLongitude())
                && entity.getIp().equals(byUserAccountId.getIp())
        ) {
            return byUserAccountId;
        }

        UserAccountLocationLog userAccountLocationLog = new UserAccountLocationLog();
        userAccountLocationLog.setUserAccountId(entity.getUserAccountId());
        userAccountLocationLog.setLatitude(entity.getLatitude());
        userAccountLocationLog.setLongitude(entity.getLongitude());
        userAccountLocationLog.setIp(entity.getIp());
        userAccountLocationLog.setAreaCode(areaCode);
        logRepository.save(userAccountLocationLog);
        entity.setId(byUserAccountId.getId());
        return repository.save(entity);
    }

    @Override
    public UserAccountLocation saveSelf() {
        UserAccountLocation entity = new UserAccountLocation();
        String ip = httpRequestHeaderUtil.getIp();
        BigDecimal longitude = httpRequestHeaderUtil.getLongitude();
        BigDecimal latitude = httpRequestHeaderUtil.getLatitude();
        entity.setUserAccountId(userAccountService.getSelfId());
        entity.setIp(ip);
        entity.setLongitude(longitude);
        entity.setLatitude(latitude);
        return repository.save(entity);
    }
}
