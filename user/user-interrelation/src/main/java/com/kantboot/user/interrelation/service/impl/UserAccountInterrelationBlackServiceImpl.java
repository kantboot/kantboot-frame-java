package com.kantboot.user.interrelation.service.impl;

import com.kantboot.user.account.domain.entity.UserAccount;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.user.interrelation.dao.repository.UserAccountInterrelationBlackRepository;
import com.kantboot.user.interrelation.domain.entity.UserAccountInterrelationBlack;
import com.kantboot.user.interrelation.service.IUserAccountInterrelationBlackService;
import com.kantboot.user.interrelation.service.IUserAccountInterrelationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccountInterrelationBlackServiceImpl
    implements IUserAccountInterrelationBlackService {

    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private UserAccountInterrelationBlackRepository repository;

    @Resource
    private IUserAccountInterrelationService userAccountInterrelationService;


    @Override
    public boolean isBlack(Long blackUserAccountId) {
        UserAccount self = userAccountService.getSelf();
        return repository.existsByUserAccountIdAndBlackUserAccountId(self.getId(),blackUserAccountId);
    }

    @Override
    public boolean isBeBlack(Long userAccountId) {
        UserAccount self = userAccountService.getSelf();
        return repository.existsByUserAccountIdAndBlackUserAccountId(userAccountId,self.getId());
    }

    @Override
    public void black(Long blackUserAccountId) {
        UserAccount self = userAccountService.getSelf();
        if(!isBlack(blackUserAccountId)){
            try{
                userAccountInterrelationService.unFollow(blackUserAccountId);
            } catch (Exception e) {
            }
            repository.save(
                new com.kantboot.user.interrelation.domain.entity.UserAccountInterrelationBlack()
                    .setUserAccountId(self.getId())
                    .setBlackUserAccountId(blackUserAccountId)
            );
        }
    }

    @Override
    public List<UserAccountInterrelationBlack> getBySelf() {
        UserAccount self = userAccountService.getSelf();
        return repository.findByUserAccountId(self.getId());
    }

    @Override
    public void unBlack(Long blackUserAccountId) {
        UserAccount self = userAccountService.getSelf();
        UserAccountInterrelationBlack
            interrelationBlack =
            repository.findByUserAccountId(self.getId())
                .stream()
                .filter(e -> e.getBlackUserAccountId().equals(blackUserAccountId))
                .findFirst()
                .orElse(null);
        if(interrelationBlack!=null){
            repository.delete(interrelationBlack);
        }
    }
}
