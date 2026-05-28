package com.kantboot.user.interrelation.service.impl;

import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.user.interrelation.dao.repository.UserAccountInterrelationRemarkRepository;
import com.kantboot.user.interrelation.domain.entity.UserAccountInterrelationRemark;
import com.kantboot.user.interrelation.service.IUserAccountInterrelationRemarkService;
import com.kantboot.user.interrelation.service.IUserAccountInterrelationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccountInterrelationRemarkServiceImpl
    implements IUserAccountInterrelationRemarkService {

    @Resource
    private UserAccountInterrelationRemarkRepository userAccountInterrelationRemarkRepository;

    @Resource
    private IUserAccountService userAccountService;

    @Override
    public void setRemark(Long userAccountIdOfRemark, String remark) {
        UserAccountInterrelationRemark interrelationRemark = userAccountInterrelationRemarkRepository.findByUserAccountIdAndUserAccountIdOfRemark(
                userAccountService.getSelfId(),
                userAccountIdOfRemark
        );
        if(interrelationRemark==null){
            interrelationRemark = new UserAccountInterrelationRemark();
            interrelationRemark.setUserAccountId(userAccountService.getSelfId());
            interrelationRemark.setUserAccountIdOfRemark(userAccountIdOfRemark);
        }
        interrelationRemark.setRemark(remark);

        userAccountInterrelationRemarkRepository.save(interrelationRemark);
    }


    @Override
    public UserAccountInterrelationRemark getRemark(Long userAccountIdOfRemark) {
        return userAccountInterrelationRemarkRepository.findByUserAccountIdAndUserAccountIdOfRemark(
                userAccountService.getSelfId(),
                userAccountIdOfRemark
        );
    }

    @Override
    public List<UserAccountInterrelationRemark> getByUserAccountId(Long userAccountId) {
        return userAccountInterrelationRemarkRepository.findByUserAccountId(userAccountId);
    }

    @Override
    public List<UserAccountInterrelationRemark> getBySelf() {
        return getByUserAccountId(userAccountService.getSelfId());
    }
}
