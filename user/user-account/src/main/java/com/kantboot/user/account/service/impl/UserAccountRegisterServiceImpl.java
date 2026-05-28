package com.kantboot.user.account.service.impl;

import com.kantboot.user.account.dao.repository.UserAccountRepository;
import com.kantboot.user.account.domain.entity.UserAccount;
import com.kantboot.user.account.domain.vo.LoginVO;
import com.kantboot.user.account.exception.UserAccountException;
import com.kantboot.user.account.service.IUserAccountRegisterService;
import com.kantboot.user.account.service.IUserAccountService;
import com.kantboot.user.account.service.IUserAccountTokenService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserAccountRegisterServiceImpl implements IUserAccountRegisterService {

    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private UserAccountRepository repository;

    @Resource
    private IUserAccountTokenService userAccountTokenService;

    @Override
    public LoginVO registerByUsernameAndPassword(String username, String password) {
        // 查询用户名是否已存在
        UserAccount userAccountByUsername = repository.findByUsername(username);
        if (userAccountByUsername != null) {
            // 用户名已存在
            throw UserAccountException.USERNAME_EXIST;
        }
        UserAccount userAccountToSave = new UserAccount();
        userAccountToSave.setUsername(username);
        // 此处无需加密，在调用createUserAccount时会加密
        userAccountToSave.setPassword(userAccountService.encryptPassword(password));
        // 创建用户账号
        UserAccount userAccount = userAccountService.createUserAccount(userAccountToSave);
        // 生成令牌
        String token = userAccountTokenService.generateToken(userAccount.getId());
        return new LoginVO()
                .setToken(token)
                .setUserAccount(userAccount);
    }


}
