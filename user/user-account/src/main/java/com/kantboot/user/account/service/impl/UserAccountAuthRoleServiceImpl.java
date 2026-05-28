package com.kantboot.user.account.service.impl;

import com.kantboot.user.account.dao.repository.UserAccountAuthRoleRepository;
import com.kantboot.user.account.service.IUserAccountAuthRoleService;
import com.kantboot.user.account.service.IUserAccountService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccountAuthRoleServiceImpl implements IUserAccountAuthRoleService {

    @Resource
    private UserAccountAuthRoleRepository repository;

    @Resource
    private IUserAccountService userAccountService;

    @Override
    public List<Long> getRoleIdsByUserAccountId(Long userAccountId) {
        return repository.getRoleIdsByUserAccountId(userAccountId);
    }

    @Override
    public List<Long> getRoleIdsBySelf() {
        return getRoleIdsByUserAccountId(userAccountService.getSelfId());
    }

}
