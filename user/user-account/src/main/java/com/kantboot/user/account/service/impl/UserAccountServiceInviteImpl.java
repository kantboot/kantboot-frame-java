package com.kantboot.user.account.service.impl;

import cn.hutool.core.util.StrUtil;
import com.kantboot.user.account.dao.repository.UserAccountRepository;
import com.kantboot.user.account.domain.entity.UserAccount;
import com.kantboot.user.account.exception.UserAccountException;
import com.kantboot.user.account.service.IUserAccountInviteService;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.user.account.util.SnowflakeConverter;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class UserAccountServiceInviteImpl implements IUserAccountInviteService {

    @Resource
    private UserAccountRepository repository;

    @Resource
    private IUserAccountService userAccountService;


    @Override
    public void initDirectCode(Long userAccountId) {
        UserAccount userAccount = repository.findById(userAccountId).orElse(null);
        if (userAccount != null) {
            String directCode = userAccount.getDirectCode();
            if (StrUtil.isEmpty(directCode)) {
                userAccount.setDirectCode(SnowflakeConverter.snowflakeToInvite(BigInteger.valueOf(userAccountId)));
                repository.save(userAccount);
            }
            return;
        }
        throw UserAccountException.NOT_EXIST;
    }

    @Override
    public void initDirectCodeSelf() {
        Long selfId = userAccountService.getSelfId();
        initDirectCode(selfId);
    }

    @Override
    public void setInviter(Long userAccountId, Long userAccountIdOfInviter) {
        UserAccount userAccount = repository.findById(userAccountId).orElse(null);
        if (userAccount == null) {
            throw UserAccountException.NOT_EXIST;
        }
        // 如果有了邀请人
        if (userAccount.getUserAccountIdOfInviter() != null) {
            throw UserAccountException.HAS_INVITER;
        }
        // 邀请人和被邀请人不能是同一个人
        if (userAccountId.equals(userAccountIdOfInviter)) {
            throw UserAccountException.INVITER_IS_SELF;
        }
        // 被邀请人的注册时间不能早于邀请人的注册时间
        UserAccount userAccountOfInviter = repository.findById(userAccountIdOfInviter).orElse(null);
        if (userAccountOfInviter == null) {
            throw UserAccountException.NOT_EXIST;
        }
//        if (userAccountOfInviter.getGmtCreate().after(userAccount.getGmtCreate())) {
//            throw UserAccountException.INVITER_JOIN_TIME_ERROR;
//        }
        // 被邀请人的注册不能超过24小时
//        if (System.currentTimeMillis() - userAccount.getGmtCreate().getTime() > 1000 * 60 * 60 * 24) {
//            throw UserAccountException.INVITER_JOIN_TIME_ERROR;
//        }

        userAccount.setUserAccountIdOfInviter(userAccountIdOfInviter);
        repository.save(userAccount);
    }

    @Override
    public void setInviterSelf(Long userAccountIdOfInviter) {
        Long selfId = userAccountService.getSelfId();
        setInviter(selfId, userAccountIdOfInviter);
    }

    @Override
    public List<UserAccount> getAllInviterByUserAccountId(Long userAccountId) {
        return repository.getByUserAccountIdOfInviter(userAccountId);
    }

    @Override
    public List<UserAccount> getAllInviterBySelf() {
        Long selfId = userAccountService.getSelfId();
        return getAllInviterByUserAccountId(selfId);
    }
}
