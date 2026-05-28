package com.kantboot.user.account.service.impl;

import com.kantboot.user.account.dao.repository.UserAccountRepository;
import com.kantboot.user.account.domain.entity.UserAccount;
import com.kantboot.user.account.domain.vo.LoginVO;
import com.kantboot.user.account.exception.UserAccountException;
import com.kantboot.user.account.service.IUserAccountInitService;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.user.account.service.IUserAccountTokenService;
import com.kantboot.util.crypto.password.IBasePassword;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserAccountInitServiceServiceImpl
    implements IUserAccountInitService {

    @Resource
    private UserAccountRepository repository;

    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private IUserAccountTokenService userAccountTokenService;

    @Resource
    private IBasePassword basePassword;

    @Override
    public LoginVO initByEmailAndPassword(String email, String password) {
        if(userAccountService.getTotal()>0L){
            throw UserAccountException.USER_ACCOUNT_EXIST_IN_INIT;
        }

        UserAccount userAccount = userAccountService.createUserAccount(new UserAccount().setEmail(email).setPassword(password));
        userAccount.setEmail(email);
        userAccount.setIsAdmin(true);
        userAccount.setIsSystemAdmin(true);
        userAccount.setPassword(userAccountService.encryptPassword(password));
        repository.save(userAccount);
        userAccountTokenService.generateToken(userAccount.getId());
        return new LoginVO()
                .setToken(userAccountTokenService.generateToken(userAccount.getId()))
                .setUserAccount(userAccount);
    }

    @Override
    public LoginVO initByUsernameAndPassword(String username, String password) {
        if(userAccountService.getTotal()>0L){
            throw UserAccountException.USER_ACCOUNT_EXIST_IN_INIT;
        }

        UserAccount userAccount = userAccountService.createUserAccount(new UserAccount().setUsername(username).setPassword(password));
        userAccount.setUsername(username);
        userAccount.setIsAdmin(true);
        userAccount.setIsSystemAdmin(true);
        userAccount.setPassword(userAccountService.encryptPassword(password));
        repository.save(userAccount);
        userAccountTokenService.generateToken(userAccount.getId());
        return new LoginVO()
                .setToken(userAccountTokenService.generateToken(userAccount.getId()))
                .setUserAccount(userAccount);


    }
}
